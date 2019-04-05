# nbiot-java
[![Travis-CI](https://api.travis-ci.com/telenordigital/nbiot-java.svg)](https://travis-ci.com/telenordigital/nbiot-java)

NBIoT-Java provides a Java client for the [REST API](https://api.nbiot.telenor.io) for
[Telenor NB-IoT](https://nbiot.engineering).

Java 8 is required.

## Configuration

The configuration file is located at `${HOME}/.telenor-nbiot`. The file is a simple
list of key/value pairs. Additional values are ignored. Comments must start
with a `#`:

    #
    # This is the URL of the Telenor NB-IoT REST API. The default value is
    # https://api.nbiot.telenor.io and can usually be omitted.
    address=https://api.nbiot.telenor.io

    #
    # This is the API token. Create new token by logging in to the Telenor NB-IoT
    # front-end at https://nbiot.engineering and create a new token there.
    token=<your api token goes here>


The configuration file settings can be overridden by setting the environment
variables `TELENOR_NBIOT_ADDRESS` and `TELENOR_NBIOT_TOKEN`. If you only use environment variables
the configuration file can be ignored.  Finally, there is a Client constructor that
accepts the address and token directly.

## How do I use it?

Just include the following in your `pom.xml`:

```
    <dependency>
        <groupId>engineering.exploratory</groupId>
        <artifactId>nbiot-java</artifactId>
        <version>0.2.0</version>
    </dependency>
```
## Sample code
Below you will find some examples on how to use the client.

### Fetching data

#### Collection with default params
```java
Client client = new Client();

OutputDataMessage[] data = client.data("<YOUR_COLLECTION_ID>");
```

#### Collection with custom params

```java
Client client = new Client();

OutputDataMessage[] data = client.data("<YOUR_COLLECTION_ID>", new ImmutableDataSearchParameters.Builder()
    .limit(500)
    .until(Instant.now())
    .build()
);
```

#### Device with default params

```java
Client client = new Client();

OutputDataMessage[] data = client.data("<YOUR_COLLECTION_ID>", "<YOUR_DEVICE_ID>");
```

#### Device with custom params

```java
Client client = new Client();

OutputDataMessage[] data = client.data("<YOUR_COLLECTION_ID>", "<YOUR_DEVICE_ID>", new ImmutableDataSearchParameters.Builder()
    .limit(500)
    .until(Instant.now())
    .build()
);
```

### Setting up a data stream through the client
```java
Client client = new Client();

client.outputStream("<YOUR_COLLECTION_ID>", handler -> {
    handler.onConnect((session -> logger.info("Handler connect")));
    handler.onError(((session, error) -> logger.warning("Handler error")));
    handler.onClose((code, reason) -> logger.info("Handler close"));
    handler.onMessage((message) -> {
        logger.info("Handler message");
        logger.info(message.toString());
    });
});
```

## Development

### Testing

Run `mvn test` to run the tests.

### Dependencies

Run `mvn versions:display-plugin-updates` to check if any plugins have newer versions available.

Run `mvn versions:display-dependency-updates` to check if any dependencies have newer versions available.

### Deployment

In order to release our components to the Central Repository, we deploy them to [OSSRH](https://oss.sonatype.org/).

Our OSSRH credentials and GPG key can be found in AWS Systems Manager Parameter Store.  Import the GPG key into your keyring.

Make sure your `$HOME/.m2/settings.xml` file contains the following, with OSSRH `password` and `gpg.passphrase` inserted:

```xml
<settings>
    <servers>
        <server>
            <id>ossrh</id>
            <username>Exploratory Engineering</username>
            <password></password>
        </server>
    </servers>

    <profiles>
        <profile>
            <id>ossrh</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <gpg.executable>gpg</gpg.executable>
                <gpg.passphrase></gpg.passphrase>
            </properties>
        </profile>
    </profiles>
</settings>
```

Finally, run this command to deploy to OSSRH.  It will automatically be released to the Central Repository.

```bash
GPG_TTY=$(tty) mvn clean deploy -P release
```

For more information see:
 - https://maven.apache.org/repository/guide-central-repository-upload.html
 - https://central.sonatype.org/pages/ossrh-guide.html
 - https://central.sonatype.org/pages/apache-maven.html
