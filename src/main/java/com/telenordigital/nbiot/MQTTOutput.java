package com.telenordigital.nbiot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.immutables.value.Value;

/*
 * An MQTT output.
 */
@Value.Immutable
@Value.Style(builder = "new")
public abstract class MQTTOutput implements Output {
	/**
	 * The output's endpoint.
	 */
	public abstract String endpoint();

	/**
	 * The output's disableCertCheck.
	 */
	@Nullable
	public abstract Boolean disableCertCheck();

	/**
	 * The output's username.
	 */
	@Nullable
	public abstract String username();

	/**
	 * The output's password.
	 */
	@Nullable
	public abstract String password();

	/**
	 * The output's clientID.
	 */
	public abstract String clientID();

	/**
	 * The output's topicName.
	 */
	public abstract String topicName();

	public OutputInternal toInternal() {
		Map<String, Object> config = new HashMap<>();
		config.put("endpoint", endpoint());
		config.put("clientID", clientID());
		config.put("topicName", topicName());
		if (disableCertCheck() != null) {
			config.put("disableCertCheck", disableCertCheck());
		}
		if (username() != null) {
			config.put("username", username());
		}
		if (password() != null) {
			config.put("password", password());
		}

		return new ImmutableOutputInternal.Builder()
			.id(id())
			.collectionID(collectionID())
			.type("mqtt")
			.config(config)
			.enabled(enabled())
			.tags(tags())
			.build();
	}
}
