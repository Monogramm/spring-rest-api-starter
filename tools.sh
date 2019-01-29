#!/bin/bash

function generateVerifierKey() {
	keytool -genkeypair -alias $1 -keyalg RSA -keypass $2 -keystore private.jks -storepass $2
	keytool -importkeystore -srckeystore private.jks -destkeystore private.jks -deststoretype pkcs12
	keytool -list -rfc --keystore private.jks | openssl x509 -inform pem -pubkey -noout > public.txt
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
    echo "USAGE: $0 [ build | run | release | jks [alias password] ]"
}


case $1 in
    build)
        build
        ;;
    run)
        run
        ;;
    release)
        release
        ;;
    jks)
        generateVerifierKey "${@:2}"
        ;;
    *)
        usage
        ;;
esac

