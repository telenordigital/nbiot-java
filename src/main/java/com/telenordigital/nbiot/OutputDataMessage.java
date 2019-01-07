package com.telenordigital.nbiot;

import javax.annotation.Nullable;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * A device.
 */
@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableOutputDataMessage.Builder.class)
public interface OutputDataMessage {

    /**
     * Ignore this.  For internal use only.
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
}