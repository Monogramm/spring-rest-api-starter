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
	echo "# Initial configuration generated at $(date +%Y-%m-%dT%H:%M:%S%z)" > ${APP_CONFIG}

	echo "# ~~~~~" >> "${APP_CONFIG}"
	echo "# Application Configuration" >> "${APP_CONFIG}"
	echo "# ~~~~~" >> "${APP_CONFIG}"
	echo "# Email" >> "${APP_CONFIG}"
	echo "application.email.app_title=${APP_TITLE}" >> "${APP_CONFIG}"
	echo "application.email.no_reply=no_reply@${APP_DOMAIN_NAME}" >> "${APP_CONFIG}"

	echo "server.context-path=${APP_SERVER_CONTEXT_PATH}" >> "${APP_CONFIG}"
	echo "server.port=${APP_SERVER_PORT}" >> "${APP_CONFIG}"

	echo "spring.http.multipart.max-file-size=${APP_MAX_FILE_SIZE}" >> "${APP_CONFIG}"
	echo "spring.http.multipart.max-request-size=${APP_MAX_REQUEST_SIZE}" >> "${APP_CONFIG}"

	echo "# Default domain name" >> "${APP_CONFIG}"
	echo "application.data.domain_name=${APP_DOMAIN_NAME}" >> "${APP_CONFIG}"
	echo "# Default admin password" >> "${APP_CONFIG}"
	echo "application.data.admin_password=${APP_ADMIN_PASSWORD}" >> "${APP_CONFIG}"

	echo "# disable demo data import" >> "${APP_CONFIG}"
	echo "application.data.demo=false" >> "${APP_CONFIG}"

	echo "# Media storage directory" >> "${APP_CONFIG}"
	mkdir -p /srv/app/data/media
	echo "application.file.upload_dir=/srv/app/data/media" >> "${APP_CONFIG}"

	if [ ! -z "${APP_SIGNING_KEYPAIR_PASS}" ]; then
		mkdir -p /srv/app/keys

		log "Generating RSA Java Key Store for verifying access tokens..."
		keytool -genkeypair -alias "${APP_SIGNING_KEYPAIR_ALIAS}" -dname "CN=${APP_DOMAIN_NAME}, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown" -keystore /srv/app/keys/.private.jks -keyalg RSA -keypass "$APP_SIGNING_KEYPAIR_PASS" -storepass "$APP_SIGNING_KEYPAIR_PASS"

		keytool -importkeystore -srckeystore /srv/app/keys/.private.jks -srcstorepass "${APP_SIGNING_KEYPAIR_PASS}" -destkeystore /srv/app/keys/.private.jks -deststoretype pkcs12
		chmod 600 /srv/app/keys/.private.jks
		log "RSA JKS generated in /srv/app/keys/"

		keytool -list -rfc --keystore /srv/app/keys/.private.jks -storepass "${APP_SIGNING_KEYPAIR_PASS}" | openssl x509 -inform pem -pubkey -noout > /srv/app/keys/public.txt
		log "Public key extracted in /srv/app/keys/"

		echo "# Access token signing key" >> "${APP_CONFIG}"
		echo "application.security.key-pair-path=/srv/app/keys/.private.jks" >> "${APP_CONFIG}"
		echo "application.security.key-pair-password=${APP_SIGNING_KEYPAIR_PASS}" >> "${APP_CONFIG}"
		echo "application.security.key-pair-alias=${APP_SIGNING_KEYPAIR_ALIAS}" >> "${APP_CONFIG}"
		echo "application.security.verifying-key-path=/srv/app/keys/public.txt" >> "${APP_CONFIG}"
		log "RSA JKS configured"
	elif [ -z "${APP_SIGNING_KEY}" ]; then
		mkdir -p /srv/app/keys

		log "Generating RSA SSH key for signing access tokens..."
		ssh-keygen -b 2048 -m PEM -t rsa -f /srv/app/keys/.id_rsa -q -N ""
		log "RSA SSH key generated in /srv/app/keys/"

		echo "# Access token signing key" >> "${APP_CONFIG}"
		echo "application.security.signing-key-path=/srv/app/keys/.id_rsa.pub" >> "${APP_CONFIG}"
		echo "application.security.verifying-key-path=/srv/app/keys/.id_rsa" >> "${APP_CONFIG}"
		log "RSA SSH key configured"
	else
		log "Setting signing key (not recommended for production)..."
		echo "# Access token signing key" >> "${APP_CONFIG}"
		echo "application.security.signing-key=${APP_SIGNING_KEY}" >> "${APP_CONFIG}"
	fi


	echo "# ~~~~~" >> "${APP_CONFIG}"
	echo "# Database Configuration" >> "${APP_CONFIG}"
	echo "# ~~~~~" >> "${APP_CONFIG}"
	echo "spring.datasource.platform=${DB_PLATFORM}" >> "${APP_CONFIG}"

	if [ ! -z "${DB_DRIVER}" ]; then
		echo "spring.datasource.driver-class-name=${DB_DRIVER}" >> "${APP_CONFIG}"
	elif [ "${DB_PLATFORM}" = 'h2' ]; then
		echo "spring.datasource.driver-class-name=org.h2.Driver" >> "${APP_CONFIG}"
	elif [ "${DB_PLATFORM}" = 'mariadb' ]; then
		echo "spring.datasource.driver-class-name=org.mariadb.jdbc.Driver" >> "${APP_CONFIG}"
	elif [ "${DB_PLATFORM}" = 'mysql' ]; then
		echo "spring.datasource.driver-class-name=com.mysql.jdbc.Driver" >> "${APP_CONFIG}"
	elif [ "${DB_PLATFORM}" = 'postgresql' ]; then
		echo "spring.datasource.driver-class-name=org.postgresql.Driver" >> "${APP_CONFIG}"
	fi

	if [ ! -z "${DB_DIALECT}" ]; then
		echo "spring.jpa.properties.hibernate.dialect=${DB_DIALECT}" >> "${APP_CONFIG}"
	elif [ "${DB_PLATFORM}" = 'h2' ]; then
		echo "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect" >> "${APP_CONFIG}"
	elif [ "${DB_PLATFORM}" = 'mariadb' ]; then
		echo "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDB53Dialect" >> "${APP_CONFIG}"
	elif [ "${DB_PLATFORM}" = 'mysql' ]; then
		echo "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect" >> "${APP_CONFIG}"
	elif [ "${DB_PLATFORM}" = 'postgresql' ]; then
		echo "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect" >> "${APP_CONFIG}"
	fi

	if [ ! -z "${DB_STORAGE}" ]; then
		echo "spring.jpa.properties.hibernate.dialect.storage_engine=${DB_STORAGE}" >> "${APP_CONFIG}"
	elif [ "${DB_PLATFORM}" = 'mariadb' ]; then
		echo "spring.jpa.properties.hibernate.dialect.storage_engine=innodb" >> "${APP_CONFIG}"
	elif [ "${DB_PLATFORM}" = 'mysql' ]; then
		echo "spring.jpa.properties.hibernate.dialect.storage_engine=innodb" >> "${APP_CONFIG}"
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

	if [ ! -z "${DB_HOST}" ]; then
		if [ "${DB_PLATFORM}" = 'mariadb' ]; then
			echo "spring.datasource.url=jdbc:${DB_PLATFORM}://${DB_HOST}:${DB_PORT}/${DB_NAME}?zeroDateTimeBehavior=convertToNull" >> "${APP_CONFIG}"
		elif [ "${DB_PLATFORM}" = 'mysql' ]; then
			echo "spring.datasource.url=jdbc:${DB_PLATFORM}://${DB_HOST}:${DB_PORT}/${DB_NAME}?zeroDateTimeBehavior=convertToNull&verifyServerCertificate=false&useSSL=false" >> "${APP_CONFIG}"
		else
			echo "spring.datasource.url=jdbc:${DB_PLATFORM}://${DB_HOST}:${DB_PORT}/${DB_NAME}" >> "${APP_CONFIG}"
		fi
	elif [ "${DB_PLATFORM}" = 'h2' ]; then
		mkdir -p /srv/app/data/h2

		log "In memory H2 database will be stored in /srv/app/data/h2"
		echo "spring.datasource.url=jdbc:h2:file:/srv/app/data/h2/${DB_NAME}" >> "${APP_CONFIG}"
	else
		echo "spring.datasource.url=jdbc:${DB_PLATFORM}://localhost:${DB_PORT}/${DB_NAME}" >> "${APP_CONFIG}"
	fi

	echo "spring.datasource.username=${DB_USER}" >> "${APP_CONFIG}"
	echo "spring.datasource.password=${DB_PASSWORD}" >> "${APP_CONFIG}"


	echo "# ~~~~~" >> "${APP_CONFIG}"
	echo "# Mail Configuration" >> "${APP_CONFIG}"
	echo "# ~~~~~" >> "${APP_CONFIG}"
	echo "spring.mail.host=${MAIL_HOST}" >> "${APP_CONFIG}"
	echo "spring.mail.port=${MAIL_PORT}" >> "${APP_CONFIG}"
	echo "spring.mail.username=${MAIL_USER}" >> "${APP_CONFIG}"
	echo "spring.mail.password=${MAIL_PASSWORD}" >> "${APP_CONFIG}"
	echo "spring.mail.protocol=${MAIL_PROTOCOL}" >> "${APP_CONFIG}"
	echo "spring.mail.properties.mail.transport.protocol=${MAIL_PROTOCOL}" >> "${APP_CONFIG}"
	echo "spring.mail.properties.mail.smtp.auth=true" >> "${APP_CONFIG}"
	echo "spring.mail.properties.mail.smtp.ssl.enable=${MAIL_SSL}" >> "${APP_CONFIG}"
	echo "spring.mail.properties.mail.smtp.starttls.enable=${MAIL_STARTTLS}" >> "${APP_CONFIG}"


	# Coming features
	#echo "# ~~~~~" >> "${APP_CONFIG}"
	#echo "# LDAP Configuration" >> "${APP_CONFIG}"
	#echo "# ~~~~~" >> "${APP_CONFIG}"
	#echo "# something like 'ldap://localhost:389'" >> "${APP_CONFIG}"
	#echo "ldap.server.url=${LDAP_URL}" >> "${APP_CONFIG}"

	#echo "# can be 'simple' or 'CRAM-MD5'" >> "${APP_CONFIG}"
	#echo "ldap.auth.type=${LDAP_AUTH_TYPE}" >> "${APP_CONFIG}"

	#echo "ldap.system.user=${LDAP_ADMIN_LOGIN}" >> "${APP_CONFIG}"
	#echo "ldap.system.password=${LDAP_ADMIN_PASS}" >> "${APP_CONFIG}"

	#echo "# user search base" >> "${APP_CONFIG}"
	#echo "ldap.user.base=${LDAP_USER_BASE}" >> "${APP_CONFIG}"

	#echo "# a template to search user by user login id" >> "${APP_CONFIG}"
	#echo "ldap.user.filter=${LDAP_USER_FILTER}" >> "${APP_CONFIG}"

	#echo "# group search base" >> "${APP_CONFIG}"
	#echo "ldap.group.base=${LDAP_GROUP_BASE}" >> "${APP_CONFIG}"

	#echo "# a template to search groups by user login id" >> "${APP_CONFIG}"
	#echo "ldap.group.filter=${LDAP_GROUP_FILTER}" >> "${APP_CONFIG}"

	#echo "# if set, create groups on ldap server under ldap.group.base" >> "${APP_CONFIG}"
	#echo "ldap.group.object.class=${LDAP_GROUP_CLASS}" >> "${APP_CONFIG}"

	log "Configuration generated."
else
	log "Configuration found."
fi

log "Launching application..."
exec "$@"
