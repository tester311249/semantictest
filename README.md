# semanticnode

# Specify GitHub Token
The token must allow to publish to the registry.
Create an npm token and to set it in the NPM_TOKEN environment variable:
## Installation
To set up Semantic Release in your project, install the required dependencies:

```bash
npm install --save-dev semantic-release @semantic-release/changelog @semantic-release/git @semantic-release/npm
```
# Semantic Release
It ensures that your package version reflects the changes made, generates a changelog automatically, and publishes releases to GitHub and npm.
## Overview
Semantic Release automates the versioning and package publishing process based on the commit messages in your repository. 
It ensures that your package version reflects the changes made and generates a changelog automatically.

## Key Features
- Automatic versioning based on semantic versioning rules.
- Automated changelog generation.
- Publishing packages to npm or other registries.
- GitHub release creation with release notes.

## Configuration
- Set up .releaserc.json: Add a configuration file to define the release rules and plugins.
- Add release script: Update the package.json file to include the release script:
```
"scripts": {
"release": "npx semantic-release --branches main"
}
```
## Environment Variables:
- Set up a GH_TOKEN for GitHub authentication.
- Set up an NPM_TOKEN for npm authentication (if publishing to npm)

## Commit Message Guidelines
Semantic Release uses commit messages to determine the type of release. Follow the Conventional Commits format:


- fix: Indicates a bug fix (patch release).
- feat: Indicates a new feature (minor release).
- BREAKING CHANGE: Indicates a breaking change (major release).
## Plugins
The project uses the following plugins:

``
@semantic-release/commit-analyzer: Analyzes commit messages to determine the release type.
@semantic-release/release-notes-generator: Generates release notes.
@semantic-release/changelog: Updates the CHANGELOG.md file.
@semantic-release/npm: Manages npm package publishing.
@semantic-release/git: Commits updated files like CHANGELOG.md and package.json.
@semantic-release/github: Creates GitHub releases.
``
test:
test 3:
test 4:
test 5:
test 6:
test 7: Add changes
test8: do test
test again test again test again
feat: test
BREAKING CHANGE: test didnt work
BREAKING CHANGE: test worked and now pre-commit added in .git/hooks
test 9: test
Retest agai n

``
Reset Local Branch to Remote: Fetch the latest changes and reset your local branch to match the remote branch.


git fetch origin
git reset --hard origin/main
Apply Your Changes: Reapply your changes to the updated branch. If your changes are stashed, you can pop them:


git stash pop
Recommit and Push: Recommit your changes and push them to the remote.


git add .
git commit -m "Your commit message"
https://www.conventionalcommits.org/en/v1.0.0/#specification


- name: Set values for bootstrap or value stream
  set_fact:
    values_file_prefix: "{{ (UNINSTALL_BEFORE_INSTALL is not defined) | ternary((VALUES_FILE_PREFIX | default(cluster_name ~ '.')), '') }}"
    values_file_name: "{{ (UNINSTALL_BEFORE_INSTALL is not defined) | ternary(values_file_prefix ~ 'values.yaml', 'values' ~ (ENV_TYPE | default('')) ~ '.yaml') }}"
    release_name: "{{ (UNINSTALL_BEFORE_INSTALL is not defined) | ternary(cluster_name ~ '-bootstrap', (RELEASE_NAME | default(NAMESPACE))) }}"
    values_to_apply: >-
      {{ (UNINSTALL_BEFORE_INSTALL is not defined)
        | ternary('-f ' ~ values_file_name,
                  '-f values.yaml' ~ (lookup('file', values_file_name, errors='ignore') is not none | ternary(' -f ' ~ values_file_name, '')))
      }}
  vars:
    cluster_name: "{{ azureCliUtils_clusterName }}"
    NAMESPACE: "{{ env_Namespace }}"
    VALUES_FILE_PREFIX: "{{ env_VALUES_FILE_PREFIX }}"
    ENV_TYPE: "{{ env_ENV_TYPE }}"
    RELEASE_NAME: "{{ env_RELEASE_NAME }}"
    UNINSTALL_BEFORE_INSTALL: "{{ params_UNINSTALL_BEFORE_INSTALL }}"
