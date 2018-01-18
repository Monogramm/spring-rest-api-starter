# README #

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
* `mysql --user=root --password < src/main/resources/_schema-mysql.sql`
* `mysql --user=root --password < src/main/resources/_data-mysql.sql`

## Run Backend API
* `mvn spring-boot:run`

### Contribution guidelines ###

See [CONTRIBUTING](CONTRIBUTING.md) file.

### License ###

This product is distributed under the GNU Affero General Public License v3.0.
See the complete license in the bundle:

[Read the license](https://github.com/madmath03/password/blob/master/LICENSE)

### Who do I talk to? ###

* [madmath03](https://github.com/madmath03)
* [ebacem](https://github.com/ebacem)
