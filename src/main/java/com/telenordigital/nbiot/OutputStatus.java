package com.telenordigital.nbiot;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * The status of an output.
 */
@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableOutputStatus.Builder.class)
public interface OutputStatus {
    /**
     * The number of errors.
     */
    @JsonProperty("errorCount")
    int errorCount();

    /**
     * The number of messages forwarded.
     */
    @JsonProperty("forwarded")
    int forwarded();

    /**
     * The number of messages received.
     */
    @JsonProperty("received")
    int received();

    /**
     * The number of messages retransmitted.
     */
    @JsonProperty("retries")
    int retries();
}
