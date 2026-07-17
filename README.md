# Hexagonal architecture showcase

Project created to showcase use of wide range of tools, libraries, plugins and <ins>**_hexagonal architecture pattern_**<ins>.

## Technological stack

On the backend side, there is a Spring Boot application being used, on the frontend - Angular one. Persistence layer is done using Hibernate and Liquibase as a DB migration tool.

### Tools and libraries

Among many frameworks, libraries and tools, the most important being used are as follows:

- Java
- Angular
- Spring Boot
- Spring Security (OAuth2 Resource Server / JWT)
- Gradle
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
- OpenAPI (Swagger UI)
- Micrometer / Prometheus / OpenTelemetry
- GitHub Actions

### Plugins

The following plugins are used during building of the application (all configuration files can be found in *"/etc"*
dir):

1. [Spotless](https://github.com/diffplug/spotless/tree/main/plugin-gradle) – plugin that is used for
   formatting. Executing the following command on the root of the project `./gradlew spotlessApply` will start it. During *gradle
   build* step formatting will be checked.
2. [JaCoCo](https://www.eclemma.org/jacoco/) – code coverage library for Java. The default limit is set to 100%.
3. [SpotBugs](https://spotbugs.github.io/) – program which uses static analysis to look for bugs in Java code.
4. [PMD](https://pmd.github.io/) – PMD is a source code analyzer.
   It finds common programming flaws like unused variables, empty catch blocks, unnecessary object creation, etc. It
   supports Java, JavaScript, Salesforce.com, PLSQL, Apache Velocity, XML, XSL, etc.
5. [DependencyCheck](https://jeremylong.github.io/DependencyCheck/dependency-check-gradle/index.html) – a
   software composition analysis plugin that identifies known vulnerable dependencies used by the project.
6. [GitProperties](https://github.com/n0mer/gradle-git-properties) – plugin that produces git.properties for
   spring-boot-actuator.
7. [Checkstyle](https://docs.gradle.org/current/userguide/checkstyle_plugin.html) – performs quality checks
   on Java source files using [Checkstyle](https://checkstyle.org/index.html) tool and generates reports from these
   checks.
8. [Gradle node](https://github.com/node-gradle/gradle-node-plugin) – plugin that is used for building the
   client app.
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

## Continuous Integration

A GitHub Actions pipeline (`.github/workflows/ci.yml`) runs on every push/PR: backend build, test and quality
gates (Checkstyle, PMD, SpotBugs, JaCoCo, Spotless), an OWASP DependencyCheck scan, and a separate frontend
build/lint/test job.

## API documentation

The REST API is documented with [springdoc-openapi](https://springdoc.org/) and exposed via Swagger UI once the
application is running:

- Swagger UI: `http://localhost:9080/home/swagger-ui/index.html`
- Raw OpenAPI spec: `http://localhost:9080/home/v3/api-docs`

Both are publicly accessible (no authentication required) so the API can be explored immediately.

## Authentication & authorization

The order API (`/api/order/**`) is secured with Spring Security's OAuth2 Resource Server support, validating
JWT bearer tokens issued by Keycloak. Two realm roles gate access:

- `ORDER_READ` – required to `GET /api/order/{orderNumber}`
- `ORDER_WRITE` – required to `POST /api/order`

Everything else (the Angular frontend under `/home`, Swagger UI, actuator health/info/metrics/prometheus
endpoints) remains publicly accessible.

A ready-to-use local Keycloak instance (realm `ecommerce`, client `ecommerce-app`, roles and two demo users
`order-admin`/`order-viewer`) is provided under `/etc/docker/keycloak`:

```
docker compose -f etc/docker/keycloak/docker-compose.yml up -d
```

Keycloak will be available at `http://localhost:8081`. Configure the issuer/JWK-set URIs via
`security.oauth2.issuer-uri` / `security.oauth2.jwk-set-uri` if running Keycloak elsewhere (see
`application-security.yml`).

## Observability

The application exposes Prometheus-formatted metrics (including a custom `orders_placed_total` business metric)
and distributed traces via OpenTelemetry/OTLP, in addition to the usual health/info actuator endpoints:

- Metrics (Prometheus format): `http://localhost:9081/actuator/prometheus`
- Health: `http://localhost:9081/actuator/health`
- Traces are exported over OTLP/HTTP to `http://localhost:4318/v1/traces` (configurable via
  `management.otlp.tracing.endpoint`), with 100% sampling enabled for local/demo purposes.
- Structured JSON console logging (Elastic Common Schema) can be enabled by activating the `json-logging`
  Spring profile, useful for log aggregation in containerized/production environments.

A ready-to-use observability stack (Prometheus + Grafana + Tempo, with pre-provisioned datasources and an
"Ecommerce Showcase - Overview" dashboard) is provided under `/etc/docker/observability`:

```
docker compose -f etc/docker/observability/docker-compose.yml up -d
```

Grafana will be available at `http://localhost:3000` (admin/admin, or anonymous access is enabled for
convenience). Prometheus scrapes the running application's actuator endpoint directly on the host, so start the
Spring Boot application separately before/after bringing up the stack.

## Resilience

The two outbound integrations that talk to external systems - RabbitMQ (`SendOrderMessageAdapter`) and SMTP
(`SendEmailAdapter`) - are wrapped with a circuit breaker and retry, implemented with
[resilience4j](https://resilience4j.readme.io/). The registries and the reusable `ResilientExecutor` helper live in
`adapter:common` (`com.cp.ecommerce.adapter.common.resilience`), so both adapters share the same defaults:

- Retry: up to 3 attempts, 500ms wait between attempts.
- Circuit breaker: opens once 50% of the last 10 calls fail, stays open for 10s, then allows 3 trial calls in
  half-open state.

Resilience4j's Spring Boot autoconfiguration starter is intentionally *not* used (to avoid the kind of Boot
4-incompatible autoconfiguration issue already hit once with another library in this project); the registries are
plain Java beans instead. When a `MeterRegistry` is present (i.e. the full application, not isolated module tests),
circuit breaker/retry metrics are automatically bound to Micrometer and show up alongside the other Prometheus
metrics described above.
