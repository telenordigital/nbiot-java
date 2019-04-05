package com.telenordigital.nbiot;

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

    /**
     * The user's name
     */
    @JsonProperty("name")
    @Nullable
    String name();

    /**
     * The user's email
     */
    @JsonProperty("email")
    @Nullable
    String email();

    /**
     * The user's phone
     */
    @JsonProperty("phone")
    @Nullable
    String phone();

    /**
     * The user's verifiedEmail
     */
    @JsonProperty("verifiedEmail")
    boolean verifiedEmail();

    /**
     * The user's verifiedPhone
     */
    @JsonProperty("verifiedPhone")
    boolean verifiedPhone();

    /**
     * The user's connectID
     */
    @JsonProperty("connectID")
    @Nullable
    String connectID();

    /**
     * The user's gitHubLogin
     */
    @JsonProperty("gitHubLogin")
    @Nullable
    String gitHubLogin();

    /**
     * The user's authType
     */
    @JsonProperty("authType")
    @Nullable
    String authType();

    /**
     * The user's avatarURL
     */
    @JsonProperty("avatarURL")
    @Nullable
    String avatarURL();
}
