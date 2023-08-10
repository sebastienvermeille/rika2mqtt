# Release manager notes

> This is automated via github

## Manual releasing operations

1) Set the correct version in `pom.xml` properties `<version></version>` -> call `mvn versions:set -DnewVersion=2.50.1-SNAPSHOT`
2) Fill the `<password></password>` with docker push token in `pom.xml` build section.
3) Run the following command:
   `mvn clean package spring-boot:build-image`

Voila it's released
