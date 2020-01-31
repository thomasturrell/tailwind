# Tailwind

Tailwind is a sample application designed to demonstrate a wide range of technologies and best practices.

Hello


It uses the following technologies:

* Flyway for database versioning
* Asciidoc for documentation
* Asciidoctor for PDF generation
* Spring Data Rest for restful APIs
* Maven release plugin for versioning
* Hibernate for object-relational mapping
* H2 database in memory database for testing
* JSR-303 validation

Best practices:

* Unit testing
* Integration testing
* Continuous integration
* Continuous delivery
* Continuous deployment
* Bitbucket Jira Integration
* Bitbucket binary artefact storage
* Jira release management
* Kanban boards
* Configuration via start up parameters
* Code Coverage
* Automatic static code analysis on pull requests

## Build from Source

### Before You Start

To build you will need Git and JDK 8 or later. Be sure that your JAVA_HOME environment variable points to the JDK.

### Get the Source Code

```sh
git clone https://bitbucket.org/berrycloud/tailwind.git
cd tailwind
```

### Build from the Command Line

To compile, test, build all wars, jars, distribution zips, and docs use:

```sh
./mvnw clean install
```

The first time you run the build it may take a while to download Maven and all build dependencies, as well as to run all tests.

## Running the API

```sh
cd tailwind
../mvnw spring-boot:run
```

## Specifying a database connection

By default the Tailwind API will run with a H2 in memory database. In production environments you should specify the database connection by passing the url, username and password.

For example:

```sh
cd tailwind
../mvnw spring-boot:run -Dspring.datasource.url=jdbc:h2:tcp://localhost/~/test -Dspring.datasource.username=sa -Dspring.datasource.password=
```

## Prepare a release

Create a release branch which is named release-* where * is the version number.

For example:

```sh
git fetch && git checkout master
git checkout -b release-1.1.0
```

To prepare the next release, run:

```sh
./mvnw clean release:prepare -DpushChanges=false
git push
```

The prepare goal of the release plugin will perform the following steps:

* Change the version in the POMs from x-SNAPSHOT to a new version
* Run the project tests against the modified POMs
* Commit the modified POMs
* Tag the code in the SCM with a version name
* Bump the version in the POMs to a new value y-SNAPSHOT (these values will also be prompted for)
* Commit the modified POMs

Finally the release branch should be merged back into master.

Copyright Berry Cloud Ltd 2018 - 2019