Jenkinsfile (Declarative Pipeline)
pipeline {
    agent { docker { image 'maven:3.8.4-openjdk-11-slim' } }
    stages {
        stage('build') {
            steps {
                sh 'echo "start"'
                sh 'cd spring-backend'
                sh 'mvn --version'
            }
        }
    }
}