#!/bin/bash

function generateJavaKeyStore() {
	APP_VERIFIER_KEY_ALIAS=$1
	APP_VERIFIER_KEY_PASS=$2

	mkdir -p /srv/app/keys
	keytool -genkeypair -alias "$APP_SIGNING_KEYPAIR_ALIAS" -dname "CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, S=Unknown, C=Unknown" -keystore data/keys/.private.jks -keyalg RSA -keypass "$APP_SIGNING_KEYPAIR_PASS" -storepass "$APP_SIGNING_KEYPAIR_PASS"
	keytool -importkeystore -srckeystore data/keys/.private.jks -srcstorepass "$APP_SIGNING_KEYPAIR_PASS" -destkeystore data/keys/.private.jks -deststoretype pkcs12
	keytool -list -rfc --keystore data/keys/.private.jks -storepass "$APP_SIGNING_KEYPAIR_PASS" | openssl x509 -inform pem -pubkey -noout > data/keys/public.txt
}

function generateRsaKey() {
	mkdir -p /srv/app/keys

	ssh-keygen -b 2048 -m PEM -t rsa -f data/keys/.id_rsa -q -N ""
}

function build {
	mvn clean install
}

function run {
	mvn spring-boot:run
}

function release {
	mvn release:prepare
	mvn release:perform -B
}

function usage {
    echo "Simple tools to help development and release"
    echo "USAGE: $0 [ build | keys [alias password] | run | release ]"
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
    *)
        usage
        ;;
esac

