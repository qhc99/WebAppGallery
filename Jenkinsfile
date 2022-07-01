pipeline {
    agent any 
    stages {
        stage('Test Backend') { 
            steps {
                sh 'cd gallery-backend'
                sh 'mvnw clean test' 
            }
        }
    }
}