pipeline {
    agent any 
    stages {
        stage('Test Backend') { 
            steps {
                
                sh '''
                cd gallery-backend
                mvn clean test
                '''  
            }
        }
    }
}