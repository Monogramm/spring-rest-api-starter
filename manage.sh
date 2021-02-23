#!/bin/bash

function log() {
    echo "[$0] [$(date -u +%Y-%m-%dT%H:%M:%SZ)] $*"
}

function ask_field() {
    local FIELD=$1
    local MESSAGE=$2
    local DEFAULT_VALUE=$3

    local TEMP=
    echo "$MESSAGE (or leave empty for default value '$DEFAULT_VALUE'):"
    read -r -e TEMP
    echo ' '
    export "$FIELD"="${TEMP:-$DEFAULT_VALUE}"
}

function generateJavaKeyStore() {
    log 'Generate JWT key stores...'

    APP_VERIFIER_KEY_ALIAS=${1}
    if [ -z "${APP_VERIFIER_KEY_ALIAS}" ]; then
        ask_field 'APP_VERIFIER_KEY_ALIAS' ''
    fi

    APP_VERIFIER_KEY_PASS=${2}
    if [ -z "${APP_VERIFIER_KEY_PASS}" ]; then
        ask_field 'APP_VERIFIER_KEY_PASS' ''
    fi

    mkdir -p /srv/app/keys
    keytool -genkeypair -alias "$APP_SIGNING_KEYPAIR_ALIAS" -dname "CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown" -keystore data/keys/.private.jks -keyalg RSA -keypass "$APP_SIGNING_KEYPAIR_PASS" -storepass "$APP_SIGNING_KEYPAIR_PASS"
    keytool -importkeystore -srckeystore data/keys/.private.jks -srcstorepass "$APP_SIGNING_KEYPAIR_PASS" -destkeystore data/keys/.private.jks -deststoretype pkcs12
    keytool -list -rfc --keystore data/keys/.private.jks -storepass "$APP_SIGNING_KEYPAIR_PASS" | openssl x509 -inform pem -pubkey -noout > data/keys/public.txt
}

function generateRsaKey() {
    log 'Generate RSA keys...'

    mkdir -p /srv/app/keys

    ssh-keygen -b 2048 -m PEM -t rsa -f data/keys/.id_rsa -q -N ""
}

function init_keys() {
    if [ ! -f data/keys/public.txt ]; then
        generateJavaKeyStore
    fi
    if [ ! -f data/keys/.id_rsa ]; then
        generateRsaKey
    fi
}

function build {
    mvn clean install
}

function run {
    init_keys
    mvn spring-boot:run
}

function release {
    mvn release:prepare
    mvn release:perform -B
}

function init_compose {
    if [ ! -f '.env' ]; then
        log 'Init docker compose environment variables...'
        cp .env_template .env.tmp

        ask_field 'APP_PACKAGE_REPO_LOGIN' 'Enter your Private Package Repository login' "${1}"
        sed -i -e "s|APP_PACKAGE_REPO_LOGIN=.*|APP_PACKAGE_REPO_LOGIN=${APP_PACKAGE_REPO_LOGIN}|" .env.tmp
        APP_PACKAGE_REPO_LOGIN=

        ask_field 'APP_PACKAGE_REPO_PASSWORD' 'Enter your Private Package Repository password' "${2}"
        sed -i -e "s|APP_PACKAGE_REPO_PASSWORD=.*|APP_PACKAGE_REPO_PASSWORD=${APP_PACKAGE_REPO_PASSWORD}|" .env.tmp
        APP_PACKAGE_REPO_PASSWORD=

        mv .env.tmp .env
    fi
}

function compose {
    init_compose

    docker-compose \
        -f "docker-compose_${DATABASE:-postgres}.yml" \
        "$@"
}

function compose_config {
    init_compose "$@"

    docker-compose \
        -f "docker-compose_${DATABASE:-postgres}.yml" \
        config
}

function compose_build {
    compose build "$@"
}

function compose_test {
    compose up -d osrm_backend nominatim

    docker-compose \
        -f "docker-compose_${DATABASE:-postgres}.yml" \
        --build-arg MAVEN_PROFILE=all-tests
        build

    compose stop osrm_backend nominatim
    compose rm -f osrm_backend nominatim
}

function compose_ps {
    compose ps "$@"
}

function compose_logs {
    compose logs -f "$@"
}

function compose_run {
    compose run "$@"
}

function compose_up {
    compose up -d "$@"
}

function compose_down {
    compose down "$@"
}

function compose_start {
    compose start "$@"
}

function compose_stop {
    compose stop "$@"
}

function compose_restart {
    compose restart "$@"
}

function usage {
    echo "Simple tools to help development and release"
    echo "USAGE: $0 [ build | keys [alias password] | run | release ]"
    echo "USAGE: $0 <command> [arguments]"
    echo "
Commands:
    keys [alias password]        Generate JWT keys for local env
    run                          Run locally
    release                      Make (maven) release locally
    compose-config               Docker-compose: init env var and check docker-compose file config
    compose-ps                   Docker-compose: list services
    compose-logs                 Docker-compose: follow logs
    compose-build                Docker-compose: build containers
    compose-test                 Docker-compose: build and test containers (UT + IT)
    compose-run                  Docker-compose: run command
    compose-up                   Docker-compose: Create and start containers
    compose-down                 Docker-compose: Stop and remove containers
    compose-start                Docker-compose: Start containers
    compose-stop                 Docker-compose: Stop containers
    compose-restart              Docker-compose: Restart containers
"
}


case $1 in
    build)
        build
        ;;
    keys)
        generateJavaKeyStore "${@:2}"
        generateRsaKey
        ;;
    run)
        run
        ;;
    release)
        release
        ;;
    compose-config)
        init_compose "${@:2}"
        ;;
    compose-build)
        compose_build "${@:2}"
        ;;
    compose-test)
        compose_test "${@:2}"
        ;;
    compose-ps)
        compose_ps "${@:2}"
        ;;
    compose-logs)
        compose_logs "${@:2}"
        ;;
    compose-run)
        compose_run "${@:2}"
        ;;
    compose-up)
        compose_up "${@:2}"
        ;;
    compose-down)
        compose_down "${@:2}"
        ;;
    compose-start)
        compose_start "${@:2}"
        ;;
    compose-stop)
        compose_stop "${@:2}"
        ;;
    compose-restart)
        compose_restart "${@:2}"
        ;;
    *)
        usage
        ;;
esac

