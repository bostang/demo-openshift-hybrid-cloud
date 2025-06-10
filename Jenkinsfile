pipeline{
    agent any

    // deklarasi variabel environment
    environment{
        PROJECT_NAME = "bostang-dev"
        BUILD_NAME = "demo-openshift-hybrid-cloud"
    }

    // deklarasi tahapan pipeline
    stages{
        // Stage 1 : Build pada OpenShift
        stage('Trigger Build in OpenShift'){
            steps{
                sh "oc start-build ${BUILD_NAME} --from-dir=. --follow -n ${PROJECT_NAME}"
            }
        }

        // Stage 2 : Deploy ke OpenShift 
        stage('Deploy to OpenShift'){
            steps{
                sh "oc rollout restart deployment/${BUILD_NAME} -n ${PROJECT_NAME}"
            }
        }
    }

    // Pesan sukses/gagal proses pipeline 
    post{
        sucess {
            echo '✅ Build & deploy behasil via OpenShift BuildConfig'
        }
        failure{
            echo '❌ Gagal menjalankan pipeline'
        }
    }
}