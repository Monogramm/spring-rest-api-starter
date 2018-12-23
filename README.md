# README #

[![License: AGPL v3](https://img.shields.io/badge/License-AGPL%20v3-blue.svg)](http://www.gnu.org/licenses/agpl.html)
[![Build
Status](https://secure.travis-ci.org/Monogramm/spring-rest-api-starter.png)](https://travis-ci.org/Monogramm/spring-rest-api-starter)
[![codecov](https://codecov.io/gh/Monogramm/spring-rest-api-starter/branch/master/graph/badge.svg)](https://codecov.io/gh/Monogramm/spring-rest-api-starter)

### What is this repository for? ###

A "simple" starter project custom RESTful API.

The project uses Spring Boot and a MySQL database.

## Prerequisites
0. Globally installed [Git](https://git-scm.com/), [JDK](https://www.java.com/download/) (8 at least), [Maven](https://maven.apache.org/) and [MySQL](https://www.mysql.com/)

### Installation ###

* `git clone https://github.com/monogramm/spring-rest-api-starter.git`
* `cd spring-rest-api-starter`

Finally, edit the properties in `application.properties` to your needs (name, db, mail, etc...) and compile the project.

* `mvn clean package`

The application will automatically setup the database.

## Run Backend API
* `mvn spring-boot:run`

You can now access your API at `http://localhost:8080/spring-rest-api-starter/api/`

## Tests
### Unit Tests
* `mvn test`

### Integration Tests
* `mvn verify`

Be careful as the IT will startup the server and test the actual functions. This means that all application properties must be valid for integration, even mail ones (not mocked).

The IT use there own [application.properties](src/integration-test/resources/application.properties) which defaults to a separate database. Make sure to edit the default parameters to your environment.

### Documentation

It is possible to generate documentation using JavaDoc
* `mvn site`

Also, the application uses Swagger to document the API.
When running the backend API, go to  `http://localhost:8080/spring-rest-api-starter/api/v2/api-docs`

### Contribution guidelines ###

See [CONTRIBUTING](CONTRIBUTING.md) file.

### License ###

This product is distributed under the GNU Affero General Public License v3.0.
See the complete license in the bundle:

[Read the license](https://github.com/Monogramm/spring-rest-api-starter/blob/master/LICENSE)

### Who do I talk to? ###

* [madmath03](https://github.com/madmath03)
* [ebacem](https://github.com/ebacem)
* [vinctix](https://github.com/vinctix)

