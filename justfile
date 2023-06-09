set dotenv-load

# just displays the receipes
default:
    @just --list

# build the plugin
build:
    ./gradlew buildPlugin

# sign the plugin
## requires enviroment variabels and certificates to be set
sign: build
    ./gradlew signPlugin

# publish the plugin
publish: sign
    ./gradlew publishPlugin