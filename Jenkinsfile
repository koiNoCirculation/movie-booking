node {
         def ENV = "prod"
         def DOMAIN = "dev"
         if(env.GIT_BRANCH != 'main') {
              ENV = 'devk8s'
              DOMAIN = 'local'
         }
        agent {
            dockerfile {
                additionalBuildArgs "--build-arg profile=${ENV}"
            }
        }
        def maven = docker.image('maven:3.9.9-eclipse-temurin-21')
        def kubectl = docker.image('bitnami/kubectl')
        maven.pull()
        kubectl.pull()
        stage('SCM') {
             checkout scm
        }
        stage('Test & Sonar') {
                    maven.inside {
                            withSonarQubeEnv() {
                                sh "mvn clean install sonar:sonar"
                                junit 'target/surefire-reports/*.xml'
                            }

                    }
        }

        stage("Quality Gate"){
             timeout(time: 5, unit: 'MINUTES') {
                  def qg = waitForQualityGate()
                  if (qg.status != 'OK') {
                      error "Pipeline aborted due to quality gate failure: ${qg.status}"
                  }
             }
        }

        stage('Build and publish') {
            docker.withRegistry('http://harbor.youtiao.local', 'harbor-userpass') {
                docker.build('harbor.youtiao.local/movie-booking/movie-booking',"--build-arg profile=${ENV} .").push("${ENV}-${env.BUILD_TAG}")
            }
        }

        stage('deploy to k8s') {
            withCredentials([string(credentialsId: 'k8s', variable: 'KUBECONFIG'), string(credentialsId:'harbor', variable: 'DOCKERPULLSECRET')]) {
            sh "sed -i s/{TAG}/${ENV}-${env.BUILD_TAG}/ k8s/template/movie-booking-deployment.yaml"
            sh 'sed -i "s#{DOCKERCONFIG}#${DOCKERPULLSECRET}#" k8s/template/movie-booking-secrets.yaml'
            sh "sed -i s/{NAME}/movie-booking-${ENV}/ k8s/template/namespace.yaml"
            sh "sed -i s/{ENV}/${ENV}/ k8s/template/movie-booking-ingress.yaml"
            sh "sed -i s/{DOMAIN}/${DOMAIN}/ k8s/template/movie-booking-ingress.yaml"
            kubectl.inside("--entrypoint=''") {
                sh 'echo $KUBECONFIG | base64 -d > /tmp/kubeconfig'
                sh 'cat k8s/template/movie-booking-secrets.yaml'
                sh 'kubectl --kubeconfig /tmp/kubeconfig apply -f k8s/template/namespace.yaml'
                sh "kubectl --kubeconfig /tmp/kubeconfig apply -f k8s/template/movie-booking-deployment.yaml -n movie-booking-${ENV}"
                sh "kubectl --kubeconfig /tmp/kubeconfig apply -f k8s/template/movie-booking-secrets.yaml -n movie-booking-${ENV}"
                sh "kubectl --kubeconfig /tmp/kubeconfig apply -f k8s/template/movie-booking-ingress.yaml -n movie-booking-${ENV}"
            }
            }
        }
}