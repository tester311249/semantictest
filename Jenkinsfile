pipeline {
    agent any
    stages {
        stage('Add and Use Credentials') {
            steps {
                script {
                    // Define the custom credential class inside the script block
                    class CustomBaseStandardCredentials extends com.cloudbees.plugins.credentials.impl.BaseStandardCredentials {
                        private final hudson.util.Secret secret

                        CustomBaseStandardCredentials(com.cloudbees.plugins.credentials.CredentialsScope scope, String id, String description, String secretValue) {
                            super(scope, id, description)
                            this.secret = hudson.util.Secret.fromString(secretValue)
                        }

                        hudson.util.Secret getSecret() {
                            return secret
                        }
                    }

                    // Add the custom credentials
                    def addCustomCredentials = {
                        def customCredentials = new CustomBaseStandardCredentials(
                            com.cloudbees.plugins.credentials.CredentialsScope.GLOBAL,
                            "my-credential-id", // Unique ID for the credential
                            "Description of the credential",
                            "my-secret-value"  // Secret value
                        )

                        def domain = com.cloudbees.plugins.credentials.domains.Domain.global()
                        def store = jenkins.model.Jenkins.instance.getExtensionList(
                            'com.cloudbees.plugins.credentials.SystemCredentialsProvider'
                        )[0].getStore()
                        store.addCredentials(domain, customCredentials)

                        echo "Custom BaseStandardCredentials added successfully!"
                    }

                    // Call the function to add credentials
                    addCustomCredentials()

                    // Retrieve and use the credentials
                    def credentialsId = 'my-credential-id'
                    def credentials = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
                        com.cloudbees.plugins.credentials.common.StandardCredentials.class,
                        jenkins.model.Jenkins.instance,
                        null,
                        null
                    ).find { it.id == credentialsId }

                    if (credentials instanceof CustomBaseStandardCredentials) {
                        def secretValue = credentials.getSecret().getPlainText()
                        echo "Retrieved Secret: ${secretValue}"
                    } else {
                        error "Credentials with ID '${credentialsId}' not found or unsupported type"
                    }
                }
            }
        }
    }
}