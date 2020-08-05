#!/usr/bin/env groovy
pipeline {
    agent any
    stages {
        stage('Clone sources') {
            steps {
                echo 'Cloning...'
                git branch: '*/module4', url: 'https://github.com/Altmerian/gift-rest-service'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
                bat 'gradlew.bat clean test'
            }
        }
        stage('Build') {
            steps {
                echo 'Building...'
                bat 'gradlew.bat build'
            }
        }
        stage('Sonarqube scanning') {
            steps {
                echo 'Sonarqube scanning...'
                bat 'gradlew.bat jacocoTestReport sonarqube'
            }
        }
        stage('Deploying') {
            steps {
                echo 'Deploying...'
                deploy adapters: [tomcat9(credentialsId: 'ab6daf10-4b8d-4c41-95ed-2e6a04e41b3c', path: '', url: 'http://localhost:8088')],
                        contextPath: '/gift-rest-service', onFailure: false, war: '**/*.war'
            }
        }

    }
}