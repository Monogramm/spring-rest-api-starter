FROM openjdk:8-jre-alpine

# Expected JAR file path as argument
ARG JAR_FILE

ENV \
	# Application configuration
	APP_CONFIG=/srv/app/config/application.properties \
	APP_SERVER_CONTEXT_PATH=/api \
	APP_SERVER_PORT=8080 \
	APP_MAX_FILE_SIZE=20MB \
	APP_MAX_REQUEST_SIZE=20MB \
	APP_DOMAIN_NAME=company.com \
	APP_ADMIN_PASSWORD=youshouldoverwritethiswithsomethingelse \
	APP_SIGNING_KEY=youshouldoverwritethiswithsomethingelse \
	APP_SIGNING_KEYPAIR_PASS='' \
	APP_SIGNING_KEYPAIR_ALIAS=spring_rest_api_starter_key \
	APP_DEMO_DATA=false \
	# Database configuration
	DB_DIALECT='' \
	DB_STORAGE='' \
	DB_DRIVER='' \
	DB_PLATFORM='h2' \
	DB_HOST='' \
	DB_PORT='' \
	DB_NAME='spring_rest_api_starter' \
	DB_USER='spring_rest_api_starter' \
	DB_PASSWORD='spring_rest_api_starter_password' \
	# Mail configuration
	MAIL_HOST=smtp.company.com \
	MAIL_PORT=465 \
	MAIL_PROTOCOL=smtps \
	MAIL_USER=USERNAME@company.com \
	MAIL_PASSWORD=PASSWORD \
	MAIL_SSL=true \
	MAIL_STARTTLS=false

# Setup application folders and tools
RUN set -ex; \
	mkdir -p /srv/app; \
	chmod 755 /srv/app; \
	mkdir -p /srv/app/data; \
	mkdir -p /srv/app/logs; \
	mkdir -p /srv/app/keys; \
	mkdir -p /srv/app/config; \
	# install dependencies
	apk add --update \
		openssh-keygen \
		openssl \
	; \
	rm -rf /var/cache/apk/*

VOLUME /srv/app/config /srv/app/keys /srv/app/logs /srv/app/data

WORKDIR /srv/app/

EXPOSE 8080 8443

# Healthcheck
HEALTHCHECK --start-period=10m --interval=3m --timeout=30s \
	CMD curl -v --silent http://localhost:$APP_SERVER_PORT$APP_SERVER_CONTEXT_PATH/health 2>&1 | grep UP


# Copy entrypoint and expected JAR file in container
COPY docker-entrypoint.sh /entrypoint.sh
COPY ${JAR_FILE} /srv/app/app.jar

RUN set -ex; \
	chmod 755 /entrypoint.sh /srv/app/app.jar

ENTRYPOINT ["sh", "/entrypoint.sh"]
CMD ["java", "-jar", "app.jar"]
