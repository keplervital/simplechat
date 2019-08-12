pipeline {

    agent any

    options { 
        disableConcurrentBuilds() 
    }

    tools {
        maven 'localMaven'
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn -f ./app/simplechat clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn -f ./app/simplechat clean verify'
            }
            post {
                success {
                    echo 'Now Archiving...'
                    archiveArtifacts artifacts: '**/target/*.jar'
                }
            }
        }
        stage ('Deploy') {
            parallel{
                stage ('Deploy to Staging'){
                    steps {
                        sh "Now deploying"
                    }
                }
            }
        }
    }
}