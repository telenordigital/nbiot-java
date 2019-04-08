package com.telenordigital.nbiot;

import java.util.Map;
import javax.annotation.Nullable;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * An output.
 */
@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableOutput.Builder.class)
public interface Output {

    /**
     * The output's ID.
     */
    @JsonProperty("outputId")
    @Nullable
    String id();

    /**
     * The collection the output belongs to.
     */
    @JsonProperty("collectionId")
    @Nullable
    String collectionID();

    /**
     * The output's type.
     */
    @JsonProperty("type")
    @Nullable
    String type();

    /**
     * The type-specific configuration.
     */
    @JsonProperty("config")
    @Nullable
    Map<String, Object> config();

    /**
     * Whether the output is enabled.
     */
    @JsonProperty("enabled")
    @Nullable
    Boolean enabled();

    /**
     * Output tags.
     */
    @JsonProperty("tags")
    @Nullable
    Map<String, String> tags();

    @Value.Immutable
    @Value.Style(builder = "new")
    @JsonDeserialize(builder = ImmutableOutputList.Builder.class)
    static interface OutputList {
        @JsonProperty("outputs")
        @Nullable
        Output[] outputs();
    }
}