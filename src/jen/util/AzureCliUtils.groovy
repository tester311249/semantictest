
package jen.util

class AzureCliUtils {
    static void loginAzureWithServicePrincipal(String credentialsId, String tenantId) {
        withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'AZURE_CLIENT_ID', passwordVariable: 'AZURE_CLIENT_SECRET')]) {
            sh """
                az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET --tenant ${tenantId}
            """
        }
    }

    static void loginToAks(String resourceGroup, String clusterName) {
        sh "az aks get-credentials --resource-group ${resourceGroup} --name ${clusterName} --overwrite-existing"
    }

    static String getKubernetesClusterHostEntry() {
        return sh(script: "kubectl config view --minify -o jsonpath='{.clusters[0].cluster.server}'", returnStdout: true).trim()
    }
}