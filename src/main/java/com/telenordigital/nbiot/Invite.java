package com.telenordigital.nbiot;

import javax.annotation.Nullable;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * An invite is an invitation to join a team.
 */
@Value.Immutable
@Value.Style(builder = "new")
@JsonDeserialize(builder = ImmutableInvite.Builder.class)
public interface Invite {
    /**
     * The invite code.
     */
    @JsonProperty("code")
    @Nullable
    String code();

    /**
     * The creation time, in milliseconds since Unix epoch.
     */
    @JsonProperty("createdAt")
    long createdAt();

    @Value.Immutable
    @Value.Style(builder = "new")
    @JsonDeserialize(builder = ImmutableInviteList.Builder.class)
    static interface InviteList {
        @JsonProperty("invites")
        @Nullable
        Invite[] invites();
    }
}
