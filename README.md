# Showcase application

Repository originally created at the beginning of 2022, added to GitHub on February 2023.

It showcases the use of a wide range of tools, libraries, plugins and <ins>**_hexagonal architecture pattern_**<ins>.

## Technological stack

On the backend side, there is a Spring Boot application being used, on the frontend - Angular one. Persistence layer is done using Hibernate and Liquibase as a DB migration tool.

### Tools and libraries

Among many frameworks, libraries and tools, the most important being used are as follows:

- Java 17
- Angular 15
- Spring Boot 3
- Gradle 7.6
- Hibernate
- Liquibase
- Postgres
- H2
- RabbitMq
- Docker
- Lombok
- TestContainers
- ArchUnit
- Apache FOP
- Jakarta Mail
- Ehcache
- Freemarker

### Plugins

The following plugins are used during building of the application (all configuration files can be found in *"/etc"*
dir):

1. [Spotless](https://github.com/diffplug/spotless/tree/main/plugin-gradle) – plugin that is used for
   formatting. Executing the following command on the root of the project `./gradlew spotlessApply` will start it. During *gradle
   build* step formatting will be checked.
2. [JaCoCo](https://www.eclemma.org/jacoco/) – code coverage library for Java. The default limit is set to 100%.
3. [SpotBugs](https://spotbugs.github.io/) – program which uses static analysis to
   look for bugs in Java code.
4. [DependencyCheck](https://jeremylong.github.io/DependencyCheck/dependency-check-gradle/index.html) – a
   software composition analysis plugin that identifies known vulnerable dependencies used by the project.
5. [GitProperties](https://github.com/n0mer/gradle-git-properties) – plugin that produces git.properties for
   spring-boot-actuator.
6. [Checkstyle](https://docs.gradle.org/current/userguide/checkstyle_plugin.html) – performs quality checks
   on Java source files using [Checkstyle](https://checkstyle.org/index.html) tool and generates reports from these
   checks.
7. [Gradle node](https://github.com/node-gradle/gradle-node-plugin) – plugin that is used for building the
   client app.
8. [PMD](https://pmd.github.io/) – PMD is a source code analyzer.
   It finds common programming flaws like unused variables, empty catch blocks, unnecessary object creation, etc. It
   supports Java, JavaScript, Salesforce.com, PLSQL, Apache Velocity, XML, XSL, etc.
9. [Gradle Versions Plugin](https://github.com/ben-manes/gradle-versions-plugin) – this plugin provides a
    task to determine which dependencies have updates. Additionally, the plugin checks for updates to Gradle itself.

## Starting the application

Execution of command `./gradlew clean build` will build the application and make it ready to start.

The application can be started using one of 4 pre-defined run configurations:

- h2 - in-memory database without RabbitMq connection
- h2-amqp - in-memory database with RabbitMq connection
- postgres - Postgres database without RabbitMq connection
- postgres-amqp - Postgres database without RabbitMq connection

Docker images for Postgres server and RabbitMq instance can be found in */etc/docker* directory. `docker-compose up -d` command will pull images and start containers.
