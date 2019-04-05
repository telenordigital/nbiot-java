package com.telenordigital.nbiot;

import javax.annotation.Nullable;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * A field mask masks fields in API responses.
 */
@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableFieldMask.Builder.class)
public interface FieldMask {
    /**
     * Whether to mask the IMSI.
     */
    @JsonProperty("imsi")
    @Nullable
    Boolean imsi();

    /**
     * Whether to mask the IMEI.
     */
    @JsonProperty("imei")
    @Nullable
    Boolean imei();

    /**
     * Whether to mask the location.
     */
    @JsonProperty("location")
    @Nullable
    Boolean location();

    /**
     * Whether to mask the MSISDN.
     */
    @JsonProperty("msisdn")
    @Nullable
    Boolean msisdn();
}
