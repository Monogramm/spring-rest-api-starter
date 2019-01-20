FROM openjdk:8-jre-alpine


# Application configuration
ENV VERSION 0.5.0-SNAPSHOT
ENV APP_CONFIG /srv/app/config/application.properties
ENV APP_SERVER_CONTEXT_PATH /spring-rest-api-starter/api
ENV APP_SERVER_PORT 8080

ENV APP_DOMAIN_NAME company.com

ENV APP_ADMIN_PASSWORD youshouldoverwritethiswithsomethingelse

ENV APP_SIGNING_KEY youshouldoverwritethiswithsomethingelse
ENV APP_VERIFIER_KEY_PASS youshouldoverwritethiswithsomethingelse
ENV APP_VERIFIER_KEY_ALIAS spring_rest_api_starter_key

ENV APP_DEMO_DATA false


# Database configuration
ENV DB_DIALECT ''
ENV DB_STORAGE ''
ENV DB_DRIVER ''
ENV DB_PLATFORM 'h2'
ENV DB_HOST ''
ENV DB_PORT ''
ENV DB_NAME 'spring_rest_api_starter'
ENV DB_USER 'spring_rest_api_starter'
ENV DB_PASSWORD 'spring_rest_api_starter_password'


# Mail configuration
ENV MAIL_HOST smtp.company.com
ENV MAIL_PORT 465
ENV MAIL_PROTOCOL smtps
ENV MAIL_USER USERNAME@company.com
ENV MAIL_PASSWORD PASSWORD
ENV MAIL_STARTTLS true

# Copy application
RUN set -ex; \
	mkdir -p /srv/app; \
	chmod 755 /srv/app; \
	mkdir -p /srv/app/data; \
	mkdir -p /srv/app/logs; \
	mkdir -p /srv/app/keys; \
	mkdir -p /srv/app/config;
COPY target/spring-rest-api-starter-$VERSION.jar /srv/app/spring-rest-api-starter.jar


EXPOSE 8080 8443

VOLUME /srv/app/config
VOLUME /srv/app/keys
VOLUME /srv/app/logs
VOLUME /srv/app/data

WORKDIR /srv/app/


# Copy entrypoint
COPY docker-entrypoint.sh /entrypoint.sh
RUN set -ex; \
	chmod 755 /entrypoint.sh;

# Healthcheck
HEALTHCHECK CMD curl -v --silent http://localhost:$APP_SERVER_PORT$APP_SERVER_CONTEXT_PATH/health 2>&1 | grep UP

ENTRYPOINT ["/entrypoint.sh"]
CMD ["java -jar spring-rest-api-starter.jar"]
