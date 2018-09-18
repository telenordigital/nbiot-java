package com.telenordigital.horde;

import javax.annotation.Nullable;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * A device.
 */
@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableOutputMessage.Builder.class)
public interface OutputMessage {

    /**
     * Ignore this.  For internal use only.
     */
    @JsonProperty("keepAlive")
    boolean keepAlive();

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
    int received();
}