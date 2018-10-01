package com.telenordigital.nbiot;

import java.util.Map;
import javax.annotation.Nullable;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * A member represents a member of a team.
 */
@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableMember.Builder.class)
public interface Member {
    /**
     * The user's ID
     */
    @JsonProperty("userId")
    @Nullable
    String userID();

    /**
     * The user's role
     */
    @JsonProperty("role")
    @Nullable
    String role();
}
