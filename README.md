# PostHog HogQL JDBC Driver

Still WIP

## Usage

```bash
mvn clean && mvn compile && mvn package && mvn exec:java
```

Then:
1. Copy `target/hogql-jdbc-driver-1.0.0-SNAPSHOT-jar-with-dependencies.jar` to your app.
2. Select `com.posthog.hogql.HogQLDriver` as the driver.

## Note

This is still very much a work in progress. For example the API URL is encoded in the query.