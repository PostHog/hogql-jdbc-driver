# PostHog HogQL JDBC Driver

<img width="1222" alt="image" src="https://user-images.githubusercontent.com/53387/227744220-a5f44d41-a657-4ad6-856f-fdfd4d63dfaf.png">

## Usage

```bash
mvn clean && mvn compile && mvn package && mvn exec:java
```

Then:
1. Copy `target/hogql-jdbc-driver-1.0.0-SNAPSHOT-jar-with-dependencies.jar` to your app.
2. Select `com.posthog.hogql.HogQLDriver` as the driver.

## Note

This is still very much a work in progress. For example the API URL and a (development) private key are encoded in the code.

However it at least works for making SQL queries inside PyCharm.
