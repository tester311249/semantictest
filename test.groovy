import groovy.text.GStringTemplateEngine
import org.jenkinsci.plugins.workflow.cps.CpsScript


String template = libraryResource('package.json')
Map config = [:]
config.env = env

def engine = new GStringTemplateEngine()
template = engine.createTemplate(template).make(config).toString()
writeJSON file: 'package.json', text: template

import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import groovy.text.GStringTemplateEngine

def processPackageJson(script) {
    // Check if package.json exists in the workspace
    def workspacePackageJson = new File('package.json')
    if (workspacePackageJson.exists()) {
        // Parse the existing package.json
        def existingPackageJson = new JsonSlurper().parse(workspacePackageJson)

        // Load the shared library's package.json
        String sharedTemplate = script.libraryResource('package.json')
        def sharedPackageJson = new JsonSlurper().parseText(sharedTemplate)

        // Merge the two package.json files (shared takes precedence)
        def mergedPackageJson = existingPackageJson + sharedPackageJson

        // Write the merged package.json back to the workspace
        script.writeFile file: 'package.json', text: JsonOutput.prettyPrint(JsonOutput.toJson(mergedPackageJson))
    } else {
        // Use the shared library's package.json directly
        String sharedTemplate = script.libraryResource('package.json')
        Map config = [:]
        config.env = script.env

        def engine = new GStringTemplateEngine()
        sharedTemplate = engine.createTemplate(sharedTemplate).make(config).toString()
        script.writeFile file: 'package.json', text: sharedTemplate
    }
}