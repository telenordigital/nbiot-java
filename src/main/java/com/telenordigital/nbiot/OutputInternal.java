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
@JsonDeserialize(builder = ImmutableOutputInternal.Builder.class)
abstract class OutputInternal implements Output {
    /**
     * The output's ID.
     */
    @JsonProperty("outputId")
    @Nullable
    public abstract String id();

    /**
     * The collection the output belongs to.
     */
    @JsonProperty("collectionId")
    @Nullable
    public abstract String collectionID();

    /**
     * The output's type.
     */
    @JsonProperty("type")
    @Nullable
    public abstract String type();

    /**
     * The type-specific configuration.
     */
    @JsonProperty("config")
    @Nullable
    public abstract Map<String, Object> config();

    /**
     * Whether the output is enabled.
     */
    @JsonProperty("enabled")
    @Nullable
    public abstract Boolean enabled();

    /**
     * Output tags.
     */
    @JsonProperty("tags")
    @Nullable
    public abstract Map<String, String> tags();

    @Value.Immutable
    @Value.Style(builder = "new")
    @JsonDeserialize(builder = ImmutableOutputList.Builder.class)
    static interface OutputList {
        @JsonProperty("outputs")
        @Nullable
        OutputInternal[] outputs();
	}

	protected Output toOutput() {
		switch (type()) {
		case "webhook":
			return new ImmutableWebHookOutput.Builder()
				.id(id())
				.collectionID(collectionID())
				.url(str("url"))
                .basicAuthUser(str("basicAuthUser"))
                .basicAuthPass(str("basicAuthPass"))
                .customHeaderName(str("customHeaderName"))
                .customHeaderValue(str("customHeaderValue"))
                .enabled(enabled())
				.tags(tags())
				.build();
        case "mqtt":
            return new ImmutableMQTTOutput.Builder()
                .id(id())
                .collectionID(collectionID())
                .endpoint(str("endpoint"))
                .disableCertCheck(bool("disableCertCheck"))
                .username(str("username"))
                .password(str("password"))
                .clientID(str("clientID"))
                .topicName(str("topicName"))
                .enabled(enabled())
                .tags(tags())
                .build();
        case "ifttt":
            return new ImmutableIFTTTOutput.Builder()
                .id(id())
                .collectionID(collectionID())
                .key(str("key"))
                .eventName(str("eventName"))
                .asIsPayload(bool("asIsPayload"))
                .enabled(enabled())
                .tags(tags())
                .build();
        case "udp":
            return new ImmutableUDPOutput.Builder()
                .id(id())
                .collectionID(collectionID())
                .host(str("host"))
                .port(integer("port"))
                .enabled(enabled())
                .tags(tags())
                .build();
        }

		return this;
	}

	private String str(String key) {
		Object o = config().get(key);
		if (o instanceof String) {
			return (String)o;
		}
		return null;
	}

	private Boolean bool(String key) {
		Object o = config().get(key);
		if (o instanceof Boolean) {
			return (Boolean)o;
		}
		return null;
	}

	private Integer integer(String key) {
		Object o = config().get(key);
		if (o instanceof Integer) {
			return (Integer)o;
		}
		return null;
	}
}
