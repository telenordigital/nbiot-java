package com.telenordigital.nbiot;

import java.util.Map;
import javax.annotation.Nullable;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * A collection represents a collection of devices.
 */
@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableCollection.Builder.class)
public interface Collection {
    /**
     * The collection's ID
     */
    @JsonProperty("collectionId")
    @Nullable
    String id();

    /**
     * The team's ID
     */
    @JsonProperty("teamId")
    @Nullable
    String teamID();

    /**
     * Collection tags.
     */
    @JsonProperty("tags")
    @Nullable
    Map<String, String> tags();

    @Value.Immutable
    @Value.Style(builder = "new")
    @JsonDeserialize(builder = ImmutableCollectionList.Builder.class)
    static interface CollectionList {
        @JsonProperty("collections")
        @Nullable
        Collection[] collections();
    }
}
