#!/usr/bin/env groovy

pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    parameters {
        string(name: 'IMAGE_NAME', defaultValue: 'monogramm/spring-rest-api-starter', description: 'Docker Image name.')

        string(name: 'DOCKER_TAG', defaultValue: 'latest', description: 'Docker Image tag.')

        choice(name: 'VARIANT', choices: ['8-jre-alpine'], description: 'Docker Image variant.')

        string(name: 'DOCKER_REGISTRY', defaultValue: 'registry-1.docker.io', description: 'Docker Registry to publish the result image to.')

        credentials(name: 'DOCKER_CREDENTIALS', credentialType: 'Username with password', required: true, description: 'Docker credentials to push on the Docker registry.')
    }
    triggers {
        // Daily build, at 5 AM (server time), every business day
        cron('H 6 * * 1-5')
    }
    stages {
        stage('pending') {
            steps {
                updateGitlabCommitStatus name: 'jenkins', state: 'pending'
            }
        }

        stage('check docker') {
            steps {
                sh "docker --version"
                sh "docker-compose --version"
            }
        }

        stage('test') {
            steps {
                updateGitlabCommitStatus name: 'jenkins', state: 'running'

                docker run -it --rm -v "$(pwd)":/usr/src/app -w /usr/src/app maven:3-jdk-8-slim mvn clean test verify -P all-tests -B -V 
            }
        }

        stage('build') {
            steps {
                script {
                    docker.withRegistry("https://${DOCKER_REGISTRY}", "${DOCKER_CREDENTIALS}") {
                        def customImage = docker.build(
                            "${DOCKER_REGISTRY}/${IMAGE_NAME}:${DOCKER_TAG}",
                            "--build-arg TAG=${DOCKER_TAG} --build-arg VCS_REF=`git rev-parse --short HEAD` --build-arg BUILD_DATE=`date -u +'%Y-%m-%dT%H:%M:%SZ'` -f Dockerfile ."
                        )

                        customImage.push()
                        customImage.push("${VARIANT}")
                    }
                }
            }
        }
    }
    post {
        always {
            // Always cleanup after the build.
            sh 'docker image prune -f --filter until=$(date -d "yesterday" +%Y-%m-%d)'
        }
        success {
            updateGitlabCommitStatus name: 'jenkins', state: 'success'
        }
        failure {
            updateGitlabCommitStatus name: 'jenkins', state: 'failed'
        }
    }
}
