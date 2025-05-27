
package jen.util---
- name: CI/CD Pipeline Replacement for Jenkinsfile
  hosts: localhost
  vars:
    project: "{{ lookup('env', 'PROJECT') | default('mb4') }}"
    env_type: "{{ lookup('env', 'ENV_TYPE') | default('dev') }}"
    branch_name: "{{ lookup('env', 'BRANCH_NAME') }}"
    uninstall_before_install: false  # Set as needed
    values_file_prefix: "{{ lookup('env', 'VALUES_FILE_PREFIX') | default('') }}"
    release_name: ""
    values_to_apply: ""

  tasks:
    - name: Show Helm version
      shell: helm version --short
      register: helm_version

    - name: Show kubectl version
      shell: kubectl version --client --short
      register: kubectl_version

    - name: Add Nexus Helm repo
      shell: |
        helm repo add nexus https://nexus.example.com/repository/helm-hosted/ || true
        helm repo update

    - name: Azure login with service principal
      shell: az login --service-principal -u "{{ azure_client_id }}" -p "{{ azure_client_secret }}" --tenant "{{ azure_tenant_id }}"
      vars:
        azure_client_id: "{{ vault_azure_client_id }}"
        azure_client_secret: "{{ vault_azure_client_secret }}"
        azure_tenant_id: "{{ vault_azure_tenant_id }}"
      no_log: true

    - name: AKS login
      shell: az aks get-credentials --resource-group "{{ resource_group }}" --name "{{ cluster_name }}" --overwrite-existing

    - name: Set Kubernetes namespace
      shell: kubectl config set-context --current --namespace="{{ namespace }}"

    - name: Git checkout
      git:
        repo: 'https://your.git.repo/url.git'
        dest: '/path/to/checkout'
        version: "{{ branch_name }}"

    - name: Set values file and release name
      set_fact:
        values_file_name: "{{ values_file_prefix }}values.yaml"
        release_name: "{{ cluster_name }}-bootstrap"
        values_to_apply: "-f {{ values_file_name }}"

    - name: Adjust values if uninstall_before_install is true
      set_fact:
        values_file_name: "values-{{ env_type }}.yaml"
        release_name: "{{ release_name | default(namespace) }}"
        values_to_apply: "-f values.yaml -f {{ values_file_name }}"
      when: uninstall_before_install

    # Add more tasks for helm upgrade, uninstall, etc.

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