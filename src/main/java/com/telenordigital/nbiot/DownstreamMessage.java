package com.telenordigital.nbiot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

/**
 * A message sent from a device.
 */
@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableDownstreamMessage.Builder.class)
public interface DownstreamMessage {
    /**
     * The port to send to.
     */
    @JsonProperty("port")
    int port();

    /**
     * The payload to send.
     */
    @JsonProperty("payload")
    byte[] payload();
}
