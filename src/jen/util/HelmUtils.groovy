package jen.util

class HelmUtils {
    static void version() {
        sh 'helm version --short'
    }

    static void packageChart(String chartDir = '.', String destination = '.') {
        sh "helm package ${chartDir} --destination ${destination}"
    }

    static void uploadPackageToNexus(String packageFile, String nexusUrl, String repo, String username, String password) {
        sh """
            curl -u ${username}:${password} --upload-file ${packageFile} ${nexusUrl}/repository/${repo}/
        """
    }

    static void updateChartVersion(String chartYamlPath, String newVersion) {
        sh "yq e '.version = \"${newVersion}\"' -i ${chartYamlPath}"
    }

    static void dependencyUpdate(String chartDir = '.') {
        sh "helm dependency update ${chartDir}"
    }

    static void uninstall(String releaseName, String namespace) {
        sh "helm uninstall ${releaseName} --namespace ${namespace}"
    }

    static void upgrade(String releaseName, String chart, String namespace, String values = '') {
        def valuesArg = values ? "-f ${values}" : ''
        sh "helm upgrade --install ${releaseName} ${chart} --namespace ${namespace} ${valuesArg}"
    }
}