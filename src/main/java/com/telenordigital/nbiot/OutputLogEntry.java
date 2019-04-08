package com.telenordigital.nbiot;

import javax.annotation.Nullable;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * An output log entry.
 */
@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableOutputLogEntry.Builder.class)
public interface OutputLogEntry {
    /**
     * The log message.
     */
    @JsonProperty("message")
    String message();

    /**
     * The time the entry was received.
     */
    @JsonProperty("received")
    long received();

    /**
     * The number of times the entry was repeated.
     */
    @JsonProperty("repeated")
    int repeated();

    @Value.Immutable
    @Value.Style(builder = "new")
    @JsonDeserialize(builder = ImmutableOutputLog.Builder.class)
    static interface OutputLog {
        @JsonProperty("logs")
        @Nullable
        OutputLogEntry[] logs();
    }
}