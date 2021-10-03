#!/bin/sh
set -e

###########################################################
# Functions

log() {
    MESSAGE=$1

    echo "[$0] [$(date +%Y-%m-%dT%H:%M:%S)] ${MESSAGE}"
}

wait_for_host() {
    WAIT_FOR_ADDR=${1}
    if [ -z "${WAIT_FOR_ADDR}" ]; then
        log "Missing host's address to wait for!"
        exit 1
    fi

    WAIT_FOR_PORT=${2}
    if [ -z "${WAIT_FOR_PORT}" ]; then
        log "Missing host's port to wait for!"
        exit 1
    fi

    WAIT_TIME=0
    WAIT_STEP=${3:-10}
    WAIT_TIMEOUT=${4:--1}

    while ! nc -z "${WAIT_FOR_ADDR}" "${WAIT_FOR_PORT}" ; do
        if [ "${WAIT_TIMEOUT}" -gt 0 ] && [ "${WAIT_TIME}" -gt "${WAIT_TIMEOUT}" ]; then
            log "Host '${WAIT_FOR_ADDR}:${WAIT_FOR_PORT}' was not available on time!"
            exit 1
        fi

        log "Waiting host '${WAIT_FOR_ADDR}:${WAIT_FOR_PORT}'..."
        sleep "${WAIT_STEP}"
        WAIT_TIME=$(( WAIT_TIME + WAIT_STEP ))
    done
    log "Host '${WAIT_FOR_ADDR}:${WAIT_FOR_PORT}' available."
}

wait_for_hosts() {
    WAIT_FOR_HOSTS=${1}
    if [ -z "${WAIT_FOR_HOSTS}" ]; then
        log "Missing hosts to wait for!"
        exit 1
    fi

    for H in ${WAIT_FOR_HOSTS}; do
        WAIT_FOR_ADDR=$(echo "${H}" | cut -d: -f1)
        WAIT_FOR_PORT=$(echo "${H}" | cut -d: -f2)

        wait_for_host "${WAIT_FOR_ADDR}" "${WAIT_FOR_PORT}" "${WAIT_STEP}" "${WAIT_TIMEOUT}"
    done

}

# date_greater A B returns whether A > B
date_greater() {
    [ $(date -u -d "$1" -D "%Y-%m-%dT%H:%M:%SZ" +%s) -gt $(date -u -d "$2" -D "%Y-%m-%dT%H:%M:%SZ" +%s) ];
}


###########################################################
# Wait for it...

if [ -n "${WAIT_FOR}" ]; then
    wait_for_hosts "${WAIT_FOR}" "${WAIT_STEP}" "${WAIT_TIMEOUT}"
fi


###########################################################
# Runtime

log "Application entrypoint initialization..."

# If no config provided in volume, setup a default config from environment variables
if [ ! -f "${APP_CONFIG}" ]; then
	log "Setting up initial application configuration..."
	echo "# Initial configuration generated at $(date +%Y-%m-%dT%H:%M:%S%z)" > "${APP_CONFIG}.temp"

	{
		echo "# ~~~~~"
		echo "# Application Configuration"
		echo "# ~~~~~"
		echo "# Email"
		echo "application.email.app_title=${APP_TITLE}"
		echo "application.email.no_reply=no_reply@${APP_DOMAIN_NAME}"

		echo "# Server"
		echo "server.context-path=${APP_SERVER_CONTEXT_PATH}"
		echo "server.port=${APP_SERVER_PORT}"

		echo "# Spring HTTP"
		echo "spring.http.multipart.max-file-size=${APP_MAX_FILE_SIZE}"
		echo "spring.http.multipart.max-request-size=${APP_MAX_REQUEST_SIZE}"

		echo "# Default domain name"
		echo "application.data.domain_name=${APP_DOMAIN_NAME}"
		echo "# Default admin password"
		echo "application.data.admin_password=${APP_ADMIN_PASSWORD}"
		echo "# Disable demo data import"
		echo "application.data.demo=false"
	} >> "${APP_CONFIG}.temp"

	mkdir -p /srv/app/data/media
	{
		echo "# Media storage directory"
		echo "application.file.upload_dir=/srv/app/data/media"
	} >> "${APP_CONFIG}.temp"

	if [ -n "${APP_SIGNING_KEYPAIR_PASS}" ]; then
		mkdir -p /srv/app/keys
		rm -rf /srv/app/keys/.private.jks /srv/app/keys/.private.jks.old /srv/app/keys/public.txt

		log "Generating RSA Java Key Store for verifying access tokens..."
		keytool -genkeypair -alias "${APP_SIGNING_KEYPAIR_ALIAS}" -dname "CN=${APP_DOMAIN_NAME}, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown" -keystore /srv/app/keys/.private.jks -keyalg RSA -keypass "$APP_SIGNING_KEYPAIR_PASS" -storepass "$APP_SIGNING_KEYPAIR_PASS"

		keytool -importkeystore -srckeystore /srv/app/keys/.private.jks -srcstorepass "${APP_SIGNING_KEYPAIR_PASS}" -destkeystore /srv/app/keys/.private.jks -deststoretype pkcs12
		chmod 600 /srv/app/keys/.private.jks
		log "RSA JKS generated in /srv/app/keys/"

		keytool -list -rfc --keystore /srv/app/keys/.private.jks -storepass "${APP_SIGNING_KEYPAIR_PASS}" | openssl x509 -inform pem -pubkey -noout > /srv/app/keys/public.txt
		log "Public key extracted in /srv/app/keys/"

		log "Setting RSA JKS key for signing access tokens..."
		{
			echo "# Access token signing key"
			echo "application.security.key-pair-path=/srv/app/keys/.private.jks"
			echo "application.security.key-pair-password=${APP_SIGNING_KEYPAIR_PASS}"
			echo "application.security.key-pair-alias=${APP_SIGNING_KEYPAIR_ALIAS}"
			echo "application.security.verifying-key-path=/srv/app/keys/public.txt"
		} >> "${APP_CONFIG}.temp"
		log "RSA JKS configured"
	elif [ -z "${APP_SIGNING_KEY}" ]; then
		mkdir -p /srv/app/keys
		rm -rf /srv/app/keys/.id_rsa.pub /srv/app/keys/.id_rsa

		log "Generating RSA SSH key for signing access tokens..."
		ssh-keygen -b 2048 -m PEM -t rsa -f /srv/app/keys/.id_rsa -q -N ""
		log "RSA SSH key generated in /srv/app/keys/"

		log "Setting RSA SSH key for signing access tokens..."
		{
			echo "# Access token signing key"
			echo "application.security.signing-key-path=/srv/app/keys/.id_rsa.pub"
			echo "application.security.verifying-key-path=/srv/app/keys/.id_rsa"
		} >> "${APP_CONFIG}.temp"
		log "RSA SSH key configured"
	else
		log "Setting signing key (not recommended for production)..."
		{
			echo "# Access token signing key"
			echo "application.security.signing-key=${APP_SIGNING_KEY}"
		} >> "${APP_CONFIG}.temp"
	fi


	{
		echo "# ~~~~~"
		echo "# Log Configuration"
		echo "# ~~~~~"
		echo "logging.level.com.monogramm=${LOG_LEVEL}"
	} >> "${APP_CONFIG}.temp"


	{
		echo "# ~~~~~"
		echo "# Database Configuration"
		echo "# ~~~~~"
		echo "spring.datasource.platform=${DB_PLATFORM}"
	} >> "${APP_CONFIG}.temp"

	if [ -n "${DB_DRIVER}" ]; then
		echo "spring.datasource.driver-class-name=${DB_DRIVER}" >> "${APP_CONFIG}.temp"
	elif [ "${DB_PLATFORM}" = 'h2' ]; then
		echo "spring.datasource.driver-class-name=org.h2.Driver" >> "${APP_CONFIG}.temp"
	elif [ "${DB_PLATFORM}" = 'mariadb' ]; then
		echo "spring.datasource.driver-class-name=org.mariadb.jdbc.Driver" >> "${APP_CONFIG}.temp"
	elif [ "${DB_PLATFORM}" = 'mysql' ]; then
		echo "spring.datasource.driver-class-name=com.mysql.jdbc.Driver" >> "${APP_CONFIG}.temp"
	elif [ "${DB_PLATFORM}" = 'postgresql' ]; then
		echo "spring.datasource.driver-class-name=org.postgresql.Driver" >> "${APP_CONFIG}.temp"
	fi

	if [ -n "${DB_DIALECT}" ]; then
		echo "spring.jpa.properties.hibernate.dialect=${DB_DIALECT}" >> "${APP_CONFIG}.temp"
	elif [ "${DB_PLATFORM}" = 'h2' ]; then
		echo "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect" >> "${APP_CONFIG}.temp"
	elif [ "${DB_PLATFORM}" = 'mariadb' ]; then
		echo "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDB53Dialect" >> "${APP_CONFIG}.temp"
	elif [ "${DB_PLATFORM}" = 'mysql' ]; then
		echo "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect" >> "${APP_CONFIG}.temp"
	elif [ "${DB_PLATFORM}" = 'postgresql' ]; then
		echo "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect" >> "${APP_CONFIG}.temp"
	fi

	if [ -n "${DB_STORAGE}" ]; then
		echo "spring.jpa.properties.hibernate.dialect.storage_engine=${DB_STORAGE}" >> "${APP_CONFIG}.temp"
	elif [ "${DB_PLATFORM}" = 'mariadb' ]; then
		echo "spring.jpa.properties.hibernate.dialect.storage_engine=innodb" >> "${APP_CONFIG}.temp"
	elif [ "${DB_PLATFORM}" = 'mysql' ]; then
		echo "spring.jpa.properties.hibernate.dialect.storage_engine=innodb" >> "${APP_CONFIG}.temp"
	fi

	if [ -z "${DB_PORT}" ]; then
		if [ "${DB_PLATFORM}" = 'mariadb' ]; then
			DB_PORT=3306
		elif [ "${DB_PLATFORM}" = 'mysql' ]; then
			DB_PORT=3306
		elif [ "${DB_PLATFORM}" = 'postgresql' ]; then
			DB_PORT=5432
		fi
	fi

	if [ -n "${DB_HOST}" ]; then
		if [ "${DB_PLATFORM}" = 'mariadb' ]; then
			echo "spring.datasource.url=jdbc:${DB_PLATFORM}://${DB_HOST}:${DB_PORT}/${DB_NAME}?zeroDateTimeBehavior=convertToNull" >> "${APP_CONFIG}.temp"
		elif [ "${DB_PLATFORM}" = 'mysql' ]; then
			echo "spring.datasource.url=jdbc:${DB_PLATFORM}://${DB_HOST}:${DB_PORT}/${DB_NAME}?zeroDateTimeBehavior=convertToNull&verifyServerCertificate=false&useSSL=false" >> "${APP_CONFIG}.temp"
		else
			echo "spring.datasource.url=jdbc:${DB_PLATFORM}://${DB_HOST}:${DB_PORT}/${DB_NAME}" >> "${APP_CONFIG}.temp"
		fi
	elif [ "${DB_PLATFORM}" = 'h2' ]; then
		mkdir -p /srv/app/data/h2

		log "In memory H2 database will be stored in /srv/app/data/h2"
		echo "spring.datasource.url=jdbc:h2:file:/srv/app/data/h2/${DB_NAME}" >> "${APP_CONFIG}.temp"
	else
		echo "spring.datasource.url=jdbc:${DB_PLATFORM}://localhost:${DB_PORT}/${DB_NAME}" >> "${APP_CONFIG}.temp"
	fi

	if [ -n "${DB_USER}" ] && [ -n "${DB_PASSWORD}" ]; then
		{
			echo "spring.datasource.username=${DB_USER}"
			echo "spring.datasource.password=${DB_PASSWORD}"
		} >> "${APP_CONFIG}.temp"
	fi


	{
		echo "# ~~~~~"
		echo "# Mail Configuration"
		echo "# ~~~~~"
		echo "spring.mail.host=${MAIL_HOST}"
		echo "spring.mail.port=${MAIL_PORT}"
		echo "spring.mail.protocol=${MAIL_PROTOCOL}"
		echo "spring.mail.properties.mail.transport.protocol=${MAIL_PROTOCOL}"
		echo "spring.mail.properties.mail.${MAIL_PROTOCOL}.ssl.enable=${MAIL_SSL}"
		echo "spring.mail.properties.mail.${MAIL_PROTOCOL}.starttls.enable=${MAIL_STARTTLS}"
	} >> "${APP_CONFIG}.temp"

	if [ -n "${MAIL_USER}" ]; then
		{
			echo "spring.mail.username=${MAIL_USER}"
			echo "spring.mail.password=${MAIL_PASSWORD}"
			echo "spring.mail.properties.mail.${MAIL_PROTOCOL}.auth=true"
		} >> "${APP_CONFIG}.temp"
	else
		echo "spring.mail.properties.mail.${MAIL_PROTOCOL}.auth=false" >> "${APP_CONFIG}.temp"
	fi


	# Coming features
	if [ -n "${LDAP_URL}" ]; then
		{
			echo "# ~~~~~"
			echo "# LDAP Configuration"
			echo "# ~~~~~"
			echo "# something like 'ldap://localhost:389'"
			echo "ldap.server.url=${LDAP_URL}"

			echo "# can be 'simple' or 'CRAM-MD5'"
			echo "ldap.auth.type=${LDAP_AUTH_TYPE}"
			echo "ldap.system.user=${LDAP_ADMIN_LOGIN}"
			echo "ldap.system.password=${LDAP_ADMIN_PASS}"

			echo "# user search base"
			echo "ldap.user.base=${LDAP_USER_BASE}"
			echo "# a template to search user by user login id"
			echo "ldap.user.filter=${LDAP_USER_FILTER}"

			if [ -n "${LDAP_GROUP_BASE}" ]; then
				echo "# group search base"
				echo "ldap.group.base=${LDAP_GROUP_BASE}"
				echo "# a template to search groups by user login id"
				echo "ldap.group.filter=${LDAP_GROUP_FILTER}"
				echo "# if set, create groups on ldap server under ldap.group.base"
				echo "ldap.group.object.class=${LDAP_GROUP_CLASS}"
			fi
		} >> "${APP_CONFIG}.temp"
	fi

	mv "${APP_CONFIG}.temp" "${APP_CONFIG}"

	log "Configuration generated."
else
	log "Configuration found."
fi

log "Launching application..."
exec "$@"
