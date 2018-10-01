package com.telenordigital.nbiot;

import java.util.Map;
import javax.annotation.Nullable;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * A team represents a collection of users.
 */
@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableTeam.Builder.class)
public interface Team {
    /**
     * The team's ID
     */
    @JsonProperty("teamId")
    @Nullable
    String id();

    /**
     * The team's members
     */
    @JsonProperty("members")
    @Nullable
    Member[] members();

    /**
     * Team tags.
     */
    @JsonProperty("tags")
    @Nullable
    Map<String, String> tags();
}
