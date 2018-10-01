# nbiot-java
NBIoT-Java provides a Java client for the [REST API](https://api.nbiot.telenor.io) for
[Telenor NB-IoT](https://nbiot.engineering).

## Configuration

The configuration file is located at `${HOME}/.nbiot`. The file is a simple
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

    <dependency>
        <groupId>com.telenordigital</groupId>
        <artifactId>nbiot-java</artifactId>
        <version>1.0.0</version>
    </dependency>

## Sample code

## Dependencies

* Logging is done through `java.util.logging`
* HTTP requests is done through the [Unirest library](https://github.com/Kong/unirest-java)
* The [Immutables library](https://immutables.github.io/) is used for the REST entities
* [Jackson](https://github.com/FasterXML/jackson) is used to serialize to and from JSON
