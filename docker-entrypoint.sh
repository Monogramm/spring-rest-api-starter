#!/bin/bash
set -e

# If no config provided in volume, setup a default config from environment variables
if [ ! -f $APP_CONFIG ]; then
	echo "Setting up initial application config"
	echo "# Initial configuration for version $VERSION generated at $(date --iso-8601=seconds)" > $APP_CONFIG

	echo "# ~~~~~" >>  $APP_CONFIG
	echo "# Application Configuration" >>  $APP_CONFIG
	echo "# ~~~~~" >>  $APP_CONFIG
	echo "# Default domain name" >>  $APP_CONFIG
	echo "application.data.domain_name=${APP_DOMAIN_NAME}" >>  $APP_CONFIG
	echo "application.email.no_reply=no_reply@${APP_DOMAIN_NAME}" >>  $APP_CONFIG

	echo "server.context-path=${APP_SERVER_CONTEXT_PATH}" >>  $APP_CONFIG
	echo "server.port=${APP_SERVER_PORT}" >>  $APP_CONFIG

	echo "# Default admin password" >>  $APP_CONFIG
	echo "application.data.admin_password=${APP_ADMIN_PASSWORD}" >>  $APP_CONFIG

	echo "# Access token signing key" >>  $APP_CONFIG
	echo "application.security.signing-key=${APP_SIGNING_KEY}" >>  $APP_CONFIG

	if [ ! -z $APP_VERIFIER_KEY_PASS ]; then
		mkdir -p /srv/app/keys

		keytool -genkeypair -alias $APP_VERIFIER_KEY_ALIAS -keyalg RSA -keypass $APP_VERIFIER_KEY_PASS -keystore /srv/app/keys/private.jks -storepass $APP_VERIFIER_KEY_PASS
		keytool -importkeystore -srckeystore /srv/app/keys/private.jks -destkeystore /srv/app/keys/private.jks -deststoretype pkcs12
		keytool -list -rfc --keystore /srv/app/keys/private.jks | openssl x509 -inform pem -pubkey -noout > /srv/app/config/public.txt

		echo "# Access token verifier key" >>  $APP_CONFIG
		echo "application.security.signing-key=${APP_SIGNING_KEY}" >>  $APP_CONFIG

		echo "application.security.public-key-path=/srv/app/config/public.txt" >>  $APP_CONFIG
		echo "application.security.private-key-path=/srv/app/keys/private.jks" >>  $APP_CONFIG
		echo "application.security.private-key-password=$APP_VERIFIER_KEY_PASS" >>  $APP_CONFIG
		echo "application.security.private-key-pair=$APP_VERIFIER_KEY_ALIAS" >>  $APP_CONFIG
	fi

	echo "# ~~~~~" >>  $APP_CONFIG
	echo "# DEMO Configuration" >>  $APP_CONFIG
	echo "# ~~~~~" >>  $APP_CONFIG
	echo "# disable demo data import" >>  $APP_CONFIG
	echo "application.data.demo=false" >>  $APP_CONFIG


	echo "# ~~~~~" >>  $APP_CONFIG
	echo "# Database Configuration" >>  $APP_CONFIG
	echo "# ~~~~~" >>  $APP_CONFIG
	echo "spring.datasource.platform=${DB_PLATFORM}" >>  $APP_CONFIG
	echo "spring.datasource.driver-class-name=${DB_DRIVER}" >>  $APP_CONFIG
	echo "spring.jpa.properties.hibernate.dialect=${DB_DIALECT}" >>  $APP_CONFIG
	if [ ! -z $DB_STORAGE ]; then
		echo "spring.jpa.properties.hibernate.dialect.storage_engine=${DB_STORAGE}" >>  $APP_CONFIG
	fi
	echo "spring.datasource.url=jdbc:${DB_PLATFORM}://${DB_HOST}:${DB_PORT}/${DB_NAME}" >>  $APP_CONFIG
	echo "spring.datasource.username=${DB_USER}" >>  $APP_CONFIG
	echo "spring.datasource.password=${DB_PASSWORD}" >>  $APP_CONFIG


	echo "# ~~~~~" >>  $APP_CONFIG
	echo "# Mail Configuration" >>  $APP_CONFIG
	echo "# ~~~~~" >>  $APP_CONFIG
	echo "spring.mail.host=${MAIL_HOST}" >>  $APP_CONFIG
	echo "spring.mail.port=${MAIL_PORT}" >>  $APP_CONFIG
	echo "spring.mail.protocol=${MAIL_PROTOCOL}" >>  $APP_CONFIG
	echo "spring.mail.properties.mail.transport.protocol=${MAIL_PROTOCOL}" >>  $APP_CONFIG
	echo "spring.mail.username=${SMTP_USER_NAME}" >>  $APP_CONFIG
	echo "spring.mail.password=${SMTP_PASSWORD}" >>  $APP_CONFIG
	echo "spring.mail.properties.mail.smtps.starttls.enable=${MAIL_STARTTLS}" >>  $APP_CONFIG


	# Coming features
	#echo "# ~~~~~" >>  $APP_CONFIG
	#echo "# LDAP Configuration" >>  $APP_CONFIG
	#echo "# ~~~~~" >>  $APP_CONFIG
	#echo "# something like 'ldap://localhost:389'" >>  $APP_CONFIG
	#echo "ldap.server.url=${LDAP_URL}" >>  $APP_CONFIG

	#echo "# can be 'simple' or 'CRAM-MD5'" >>  $APP_CONFIG
	#echo "ldap.auth.type=${LDAP_AUTH_TYPE}" >>  $APP_CONFIG

	#echo "ldap.system.user=${LDAP_ADMIN_LOGIN}" >>  $APP_CONFIG
	#echo "ldap.system.password=${LDAP_ADMIN_PASS}" >>  $APP_CONFIG

	#echo "# user search base" >>  $APP_CONFIG
	#echo "ldap.user.base=${LDAP_USER_BASE}" >>  $APP_CONFIG

	#echo "# a template to search user by user login id" >>  $APP_CONFIG
	#echo "ldap.user.filter=${LDAP_USER_FILTER}" >>  $APP_CONFIG

	#echo "# group search base" >>  $APP_CONFIG
	#echo "ldap.group.base=${LDAP_GROUP_BASE}" >>  $APP_CONFIG

	#echo "# a template to search groups by user login id" >>  $APP_CONFIG
	#echo "ldap.group.filter=${LDAP_GROUP_FILTER}" >>  $APP_CONFIG

	#echo "# if set, create groups on ldap server under ldap.group.base" >>  $APP_CONFIG
	#echo "ldap.group.object.class=${LDAP_GROUP_CLASS}" >>  $APP_CONFIG

fi

exec "$@"