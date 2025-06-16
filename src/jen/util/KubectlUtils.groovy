package jen.util

class KubectlUtils {

    final Script script
    KubectlUtils(script) {
        this.script = script
    }
    def deploymentValidation(String releaseName, String flags = '') {
        script.echo("\033[1m============== Image Tags ==============\033[0m")
        def podImages = script.sh(
                script: """
echo "Retrieving image tags for release: ${releaseName}"" &&  kubectl get pods -o jsonpath='{.items[*].spec.containers[*].image}' | tr ' ' '\\n' ${flags}
  """
                , returnStdout: true).trim()
        script.echo("Pod Image Tags:\n${podImages}")

        script.echo("\033[1m============== Deployment Status ==============\033[0m")
        def podStatusOutput = script.sh(
                script: "kubectl get pods ${flags} --no-headers",
                returnStdout: true).trim()
        script.echo("Pod Status:\n${podStatusOutput}")

        def podErrors = []
        podStatusOutput.split('\n').each { line ->
            def podName = line.split()[0]
            def podStatus = line.split()[2]
            def restartCount = line.split()[3]
            if (!(podStatus == 'Running' || podStatus == 'Completed') || restartCount.toInteger() > 5) {
                podErrors << "Unhealthy Pod ${podName} (Status: '${podStatus}' with ${restartCount} restarts.)"

                def podDetails = script.sh(
                        script: "kubectl describe pod ${podName} ${flags}",
                        returnStdout: true
                ).trim()
                script.echo("Logs for ${podName}:\n${podDetails}")
            }
        }
        if (podErrors) {
            script.error("Deployment validation failed:\n${podErrors.join('\n')}")
        } else {
            script.echo("All pods are healthy.")
        }
    }
}