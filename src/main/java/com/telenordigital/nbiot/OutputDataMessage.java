package com.telenordigital.nbiot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;

/**
 * A message sent from a device.
 */
@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableOutputDataMessage.Builder.class)
public interface OutputDataMessage {

    /**
     * The sending device.
     */
    @JsonProperty("device")
    Device device();

    /**
     * The payload sent.
     */
    @JsonProperty("payload")
    byte[] payload();

    /**
     * The time received.
     */
    @JsonProperty("received")
    java.time.Instant received();

    @Value.Immutable
    @Value.Style(builder = "new")
    @JsonDeserialize(builder = ImmutableOutputDataMessageList.Builder.class)
    static interface OutputDataMessageList {
        @JsonProperty("messages")
        @Nullable
        OutputDataMessage[] messages();
    }
}
