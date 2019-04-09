package com.telenordigital.nbiot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.immutables.value.Value;

/*
 * An IFTTT output.
 */
@Value.Immutable
@Value.Style(builder = "new")
public abstract class IFTTTOutput implements Output {
	/**
	 * The output's key.
	 */
	public abstract String key();

	/**
	 * The output's eventName.
	 */
	public abstract String eventName();

	/**
	 * The output's asIsPayload.
	 */
	@Nullable
	public abstract Boolean asIsPayload();

	public OutputInternal toInternal() {
		Map<String, Object> config = new HashMap<>();
		config.put("key", key());
		config.put("eventName", eventName());
		if (asIsPayload() != null) {
			config.put("asIsPayload", asIsPayload());
		}

		return new ImmutableOutputInternal.Builder()
			.id(id())
			.collectionID(collectionID())
			.type("ifttt")
			.config(config)
			.enabled(enabled())
			.tags(tags())
			.build();
	}
}
