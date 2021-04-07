#!/usr/bin/env groovy

pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    /*
     * Expected Parameters to create the Pipeline in Jenkins
     *  - gitlabSourceRepoURL:
     *      - https://scm.example.com/owner/project.git
     *      - The GitLab Source repository URL.
     *        If you use login / password credentials, this is expected to be a HTTPS URL, as in https://scm.example.com/path/to/project.git
     *        If you use SSH key credentials, this is expected to be a SSH URL, as in ssh://scm.example.com/path/to/project.git
     *        This is expected to be a HTTPS URL, as in https://scm.example.com/path/to/project.git, if you use login / password credentials.
     *  - sourceBranch:
     *      - heads/develop
     *      - The GitLab Source branch.
     *        Will be overriden by $gitlabSourceBranch in case of GitLab hook, pulling refs/heads/${gitlabSourceBranch}.
     *        
     *        The repository will be pulled from refs/${sourceBranch}. You can use the following formats:
     *        .  heads/<branchName>
     *            * Tracks/checks out the specified branch. E.g. heads/master, heads/feature1/something
     *        
     *        .  tags/<tagName>
     *            * Tracks/checks out the specified tag. E.g. tags/git-2.3.0
     */
    parameters {
        string(name: 'DOCKER_REPO', defaultValue: 'monogramm/spring-rest-api-starter', description: 'Docker Image name.')

        string(name: 'DOCKER_TAG', defaultValue: 'latest', description: 'Docker Image tag.')

        choice(name: 'VARIANT', choices: ['8-jre-alpine'], description: 'Docker Image variant.')

        choice(name: 'MAVEN_PROFILE', choices: ['dev', 'integration-test', 'all-tests'], description: 'Maven build and test profile.')

        //credentials(name: 'PACKAGE_REPO_CREDENTIALS', credentialType: 'Username with password', required: true, description: 'Private Package Repository credentials to pull packages.')

        string(name: 'DOCKER_REGISTRY', defaultValue: '', description: 'Docker Registry to publish the result image to. Leave empty for DockerHub.')

        credentials(name: 'DOCKER_CREDENTIALS', credentialType: 'Username with password', required: true, description: 'Docker credentials to push on the Docker Registry.')
    }
    triggers {
        // Daily build, at 6 AM (server time), every business day
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
                sh "docker info"
            }
        }

        /*
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
                            "${DOCKER_REGISTRY}/${DOCKER_REPO}:${DOCKER_TAG}",
                            "--build-arg TAG=${DOCKER_TAG} --build-arg VCS_REF=`git rev-parse --short HEAD` --build-arg BUILD_DATE=`date -u +'%Y-%m-%dT%H:%M:%SZ'` -f Dockerfile ."
                        )

                        customImage.push()
                        customImage.push("${VARIANT}")
                    }
                }
            }
        }
        */

        stage('build-hooks') {
            //environment {
            //    PACKAGE_REPO_CREDS = credentials("${PACKAGE_REPO_CREDENTIALS}")
            //}
            steps {
                updateGitlabCommitStatus name: 'jenkins', state: 'running'

                sh 'export PACKAGE_REPO_LOGIN=${PACKAGE_REPO_CREDS_USR}; export PACKAGE_REPO_PASSWORD=${PACKAGE_REPO_CREDS_PSW}; ./hooks/run build "${VARIANT}"'
            }
        }

        stage('test-hooks') {
            steps {
                updateGitlabCommitStatus name: 'jenkins', state: 'running'

                sh './hooks/run test "${VARIANT}"'
            }
        }

        stage('push-hooks') {
            environment {
                DOCKER_CREDS = credentials("${DOCKER_CREDENTIALS}")
            }
            steps {
                updateGitlabCommitStatus name: 'jenkins', state: 'running'

                // Write Docker image tags to push
                sh '([ "${DOCKER_TAG}" = "latest" ] && echo "${VARIANT} " || echo "${DOCKER_TAG}-${VARIANT} ") > .dockertags'
                // Export variables to login and push to Docker Registry
                sh 'export DOCKER_LOGIN=${DOCKER_CREDS_USR}; export DOCKER_PASSWORD=${DOCKER_CREDS_PSW}; ./hooks/run push "${VARIANT}"'
                sh 'rm -f .dockertags'
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
