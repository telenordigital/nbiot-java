package com.telenordigital.nbiot;

import java.util.Map;
import javax.annotation.Nullable;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * A device.
 */
@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableDevice.Builder.class)
public interface Device {

    /**
     * The device's EUI.
     */
    @JsonProperty("deviceId")
    @Nullable
    String id();

    /**
     * The collection the device belongs to.
     */
    @JsonProperty("collectionId")
    @Nullable
    String collectionID();

    /**
     * The device's IMEI.
     */
    @JsonProperty("imei")
    @Nullable
    String imei();

    /**
     * The device's IMSI.
     */
    @JsonProperty("imsi")
    @Nullable
    String imsi();

    /**
     *
     */
    @JsonProperty("tags")
    @Nullable
    Map<String, String> tags();
}