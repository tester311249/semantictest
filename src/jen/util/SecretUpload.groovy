def uploadSecretToKeyVault(String credentialId, String credentialType, String keyVaultName, String secretName) {
    switch (credentialType) {
        case 'Secret text':
            withCredentials([string(credentialsId: credentialId, variable: 'SECRET_TEXT')]) {
                sh "az keyvault secret set --vault-name ${keyVaultName} --name ${secretName} --value \"${SECRET_TEXT}\""
            }
            break
        case 'Username and password':
            withCredentials([usernamePassword(credentialsId: credentialId, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                sh "az keyvault secret set --vault-name ${keyVaultName} --name ${secretName}-username --value \"${USERNAME}\""
                sh "az keyvault secret set --vault-name ${keyVaultName} --name ${secretName}-password --value \"${PASSWORD}\""
            }
            break
        case 'Secret file':
            withCredentials([file(credentialsId: credentialId, variable: 'SECRET_FILE')]) {
                sh "az keyvault secret set --vault-name ${keyVaultName} --name ${secretName} --file \"${SECRET_FILE}\""
            }
            break
        case 'SSH Username with private key':
            withCredentials([sshUserPrivateKey(credentialsId: credentialId, keyFileVariable: 'SSH_KEY', usernameVariable: 'SSH_USER')]) {
                sh "az keyvault secret set --vault-name ${keyVaultName} --name ${secretName}-user --value \"${SSH_USER}\""
                sh "az keyvault secret set --vault-name ${keyVaultName} --name ${secretName}-key --file \"${SSH_KEY}\""
            }
            break
        case 'Certificate':
            withCredentials([certificate(credentialsId: credentialId, keystoreVariable: 'CERT_FILE', passwordVariable: 'CERT_PASS')]) {
                sh "az keyvault secret set --vault-name ${keyVaultName} --name ${secretName}-cert --file \"${CERT_FILE}\""
                sh "az keyvault secret set --vault-name ${keyVaultName} --name ${secretName}-password --value \"${CERT_PASS}\""
            }
            break
        default:
            error "Unsupported credential type: ${credentialType}"
    }
}

// Example usage in a pipeline
pipeline {
    agent any
    parameters {
        string(name: 'CREDENTIAL_ID', description: 'Jenkins credential ID')
        choice(name: 'CREDENTIAL_TYPE', choices: ['Secret text', 'Username and password', 'Secret file', 'SSH Username with private key', 'Certificate'], description: 'Type of credential')
        string(name: 'KEYVAULT_NAME', description: 'Azure Key Vault name')
        string(name: 'SECRET_NAME', description: 'Secret name in Key Vault')
    }
    stages {
        stage('Upload Secret') {
            steps {
                script {
                    uploadSecretToKeyVault(params.CREDENTIAL_ID, params.CREDENTIAL_TYPE, params.KEYVAULT_NAME, params.SECRET_NAME)
                }
            }
        }
    }
}
