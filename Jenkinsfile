def IMG = "192.168.100.12/commerce-yr/commerce-yr-auth-img"
def KC = "/usr/local/bin/kubectl --kubeconfig=/home/jenkins/acloud-client.conf"

pipeline {

  environment {
    registry = "lee-code712/Commerce-Auth"
    dockerImage = ""
  }

  agent any

  stages {

    stage('Checkout Source') {
      steps {
        echo "Checkout Source START"
        git 'https://github.com/lee-code712/Commerce-Auth.git'
        echo "Checkout Source END"
      }
    }

    stage('Build image') {
      steps{
        script {
          echo "Build image START $BUILD_NUMBER"
          sh "docker build --no-cache -t ${IMG}:auth-${BUILD_NUMBER} ."
          echo "Build image END"
        }
      }
    }

    stage('Push Image') {
      environment {
               registryCredential = 'harbor'
           }
      steps{
        script {
          echo "Push Image START"
          sh "docker login 192.168.100.12 -u admin -p Unipoint11"
          sh "docker push ${IMG}:auth-${BUILD_NUMBER}"
          }
        echo "Push Image END"
      }
    }
    

    stage('Deploy App') {
      steps {
        script {
          echo "Deploy App START"
          sh "${KC} apply -f auth_deployment_v2.yaml"
          sh "${KC} set image deployment/commerce-yr-auth-v2 commerce-yr-auth=${IMG}:auth-${BUILD_NUMBER} -n commerce-yr"
          echo "Deploy App END"
        }
      }
    }

  }

}
