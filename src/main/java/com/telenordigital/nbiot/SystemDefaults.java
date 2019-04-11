package com.telenordigital.nbiot;

import javax.annotation.Nullable;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * The system configuration
 */
@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableSystemDefaults.Builder.class)
public interface SystemDefaults {
    /**
     * The default field mask
     */
    @JsonProperty("defaultFieldMask")
    @Nullable
    FieldMask fieldMask();

    /**
     * The forced field mask
     */
    @JsonProperty("forcedFieldMask")
    @Nullable
    FieldMask forcedFieldMask();
}
