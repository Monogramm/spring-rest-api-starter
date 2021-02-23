FROM php:7.4-cli-alpine

ARG COMPOSER_VERSION=2.0.4

COPY docker_test.sh /docker_test.sh

RUN set -ex; \
    chmod 755 /docker_test.sh; \
    apk add --update \
        curl \
        git \
    ; \
    \
    curl --silent --show-error "https://getcomposer.org/installer" -o composer-setup.php; \
    php composer-setup.php --install-dir=/usr/local/bin --filename=composer "--version=${COMPOSER_VERSION}"; \
    composer --version; \
    \
    rm -rf /var/cache/apk/*

CMD ["/docker_test.sh"]
