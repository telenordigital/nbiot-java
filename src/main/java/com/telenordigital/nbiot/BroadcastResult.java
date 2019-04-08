package com.telenordigital.nbiot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

/**
 * The result of a broadcast.
 */
@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableBroadcastResult.Builder.class)
public interface BroadcastResult {
    /**
     * The number of sent messages.
     */
    @JsonProperty("sent")
    int sent();

    /**
     * The number of failed messages.
     */
    @JsonProperty("failed")
    int failed();

    /**
     * Any errors.
     */
    @JsonProperty("errors")
    Error[] errors();

    @Value.Immutable
    @Value.Style(builder = "new")
    @JsonDeserialize(builder = ImmutableError.Builder.class)
    static interface Error {
		/**
		 * The device ID.
		 */
		@JsonProperty("deviceId")
		String deviceID();

		/**
		 * The message.
		 */
		@JsonProperty("message")
		String message();
	}
}
