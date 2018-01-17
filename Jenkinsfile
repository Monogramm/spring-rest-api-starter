pipeline {
    agent any
    tools {
        maven 'Maven 3.3'
        jdk 'JDK 1.8'
    }
stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    java -version
                    echo "M2_HOME = ${M2_HOME}"
                    echo "MAVEN_HOME = ${MAVEN_HOME}"
                    mvn --version
                '''
            }
        }

        stage ('Build') {
            steps {
                sh 'mvn clean compile' 
            }
        }

        stage ('Test') {
            steps {
                sh 'mvn test' 
            }
            post {
                success {
                    junit 'target/surefire-reports/*.xml,target/failsafe-reports/*.xml' 
                }
            }
        }

        stage ('Packaging') {
            steps {
                sh 'mvn package -Dmaven.test.skip=true' 
            }
            post {
                archiveArtifacts '*target/*.jar'
            }
        }

        stage ('Reporting') {
            steps {
               sh 'mvn site -Dmaven.test.skip=true' 
            }
            post {
              success {
                  findbugs canComputeNew: false, defaultEncoding: '', excludePattern: '', healthy: '', includePattern: '', pattern: '**/findbugs.xml', unHealthy: ''
                  checkstyle canComputeNew: false, defaultEncoding: '', healthy: '', pattern: '**/checkstyle-result.xml', unHealthy: ''
                  pmd canComputeNew: false, defaultEncoding: '', healthy: '', pattern: '**/pmd.xml', unHealthy: ''
                  publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'target/site', reportFiles: 'index.html', reportName: 'Maven Site Reports', reportTitles: ''])
              }
            }
        }
    }
}