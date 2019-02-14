package com.telenordigital.nbiot;

import javax.annotation.Nullable;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * A message sent from a device.
 */
@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableOutputDataMessageInternal.Builder.class)
interface OutputDataMessageInternal {

    /**
     * The message type.
     */
    @JsonProperty("type")
    String type();

    /**
     * The sending device.
     */
    @JsonProperty("device")
    @Nullable
    Device device();

    /**
     * The payload sent.
     */
    @JsonProperty("payload")
    @Nullable
    byte[] payload();

    /**
     * The time received.
     */
    @JsonProperty("received")
    @Nullable
    Long received();

    @Value.Immutable
    @Value.Style(builder = "new")
    @JsonDeserialize(builder = ImmutableOutputDataMessageListInternal.Builder.class)
    interface OutputDataMessageListInternal {
        @JsonProperty("messages")
        OutputDataMessageInternal[] messages();
    }
}