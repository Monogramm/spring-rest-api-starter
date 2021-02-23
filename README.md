# README

[![License: AGPL v3](https://img.shields.io/badge/License-AGPL%20v3-blue.svg)](http://www.gnu.org/licenses/agpl.html)
[![Build
Status](https://secure.travis-ci.org/Monogramm/spring-rest-api-starter.png)](https://travis-ci.org/Monogramm/spring-rest-api-starter)
[![codecov](https://codecov.io/gh/Monogramm/spring-rest-api-starter/branch/master/graph/badge.svg)](https://codecov.io/gh/Monogramm/spring-rest-api-starter)
[![Docker Automated buid](https://img.shields.io/docker/build/monogramm/spring-rest-api-starter.svg)](https://hub.docker.com/r/monogramm/spring-rest-api-starter/)
[![Docker Pulls](https://img.shields.io/docker/pulls/monogramm/spring-rest-api-starter.svg)](https://hub.docker.com/r/monogramm/spring-rest-api-starter/)

### What is this repository for

A 'simple' starter project custom RESTful API.

The project uses Spring Boot, a connection to a database and a connection to a mail server.

## Prerequisites

0.  Globally installed [Git](https://git-scm.com/), [JDK](https://www.java.com/download/) (8 at least) and [Maven](https://maven.apache.org/).

The project should work with the following databases:

-   [MySQL](https://www.mysql.com/) / [MariaDB](https://mariadb.org/)
-   [PostgreSQL](https://www.postgresql.org/)
-   [Java H2](http://www.h2database.com)

Edit the `application.properties` depending on the database system you need (see _installation_).

### Installation

-   `git clone https://github.com/monogramm/spring-rest-api-starter.git`
-   `cd spring-rest-api-starter`

Finally, edit the properties in `application.properties` to your needs (name, db, mail, etc...) and compile the project.

-   `mvn clean install`

The application will automatically setup the database. See `InitialDataLoader.java` for details.

## Run Backend API

-   `mvn spring-boot:run`

You can now access your API at `http://localhost:8080/spring-rest-api-starter/api/`

## Tests

### Unit Tests

-   `mvn test`

### Integration Tests

-   `mvn verify`

Be careful as the IT will startup the server and test the actual functions. This means that all application properties must be valid for integration, even mail ones (not mocked).

The IT use there own [application.properties](src/integration-test/resources/application.properties) which defaults to a in memory H2 database and in memory GreenMail mail server.

## Release

-   `mvn release:prepare`
-   `mvn release:perform`

### Documentation

It is possible to generate documentation using JavaDoc

-   `mvn site`

Also, the application uses Swagger to document the API.
When running the backend API, go to  `http://localhost:8080/spring-rest-api-starter/api/v2/api-docs`

### Docker

A Dockerfile and docker-compose template are available at the root of this project.
After building the project, the docker image can be created:

    mvn clean install
    docker build --build-arg=target/*.jar -t "monogramm/docker-spring-rest-api-starter" "."

### Tools

A helper script is available at the root of this project: `tools.sh`
It can be used for common operations

### Contribution guidelines

See [CONTRIBUTING](CONTRIBUTING.md) file.

### License

This product is distributed under the GNU Affero General Public License v3.0.
See the complete license in the bundle:

[Read the license](https://github.com/Monogramm/spring-rest-api-starter/blob/master/LICENSE)

### Who do I talk to?

-   [madmath03](https://github.com/madmath03)

### Awesome contributors

-   [ebacem](https://github.com/ebacem)
-   [vinctix](https://github.com/vinctix)
