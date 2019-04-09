package com.telenordigital.nbiot;

import java.util.HashMap;
import java.util.Map;

import org.immutables.value.Value;

/*
 * A UDP output.
 */
@Value.Immutable
@Value.Style(builder = "new")
public abstract class UDPOutput implements Output {
	/**
	 * The output's host.
	 */
	public abstract String host();

	/**
	 * The output's port.
	 */
	public abstract Integer port();

	public OutputInternal toInternal() {
		Map<String, Object> config = new HashMap<>();
		config.put("host", host());
		config.put("port", port());

		return new ImmutableOutputInternal.Builder()
			.id(id())
			.collectionID(collectionID())
			.type("udp")
			.config(config)
			.enabled(enabled())
			.tags(tags())
			.build();
	}
}
