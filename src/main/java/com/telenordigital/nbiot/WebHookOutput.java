package com.telenordigital.nbiot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.immutables.value.Value;

/*
 * A webhook output.
 */
@Value.Immutable
@Value.Style(builder = "new")
public abstract class WebHookOutput implements Output {
	/**
	 * The output's URL.
	 */
	public abstract String url();

	/**
	 * The output's basicAuthUser.
	 */
	@Nullable
	public abstract String basicAuthUser();

	/**
	 * The output's basicAuthPass.
	 */
	@Nullable
	public abstract String basicAuthPass();

	/**
	 * The output's customHeaderName.
	 */
	@Nullable
	public abstract String customHeaderName();

	/**
	 * The output's customHeaderValue.
	 */
	@Nullable
	public abstract String customHeaderValue();

	public OutputInternal toInternal() {
		Map<String, Object> config = new HashMap<>();
		config.put("url", url());
		if (basicAuthUser() != null) {
			config.put("basicAuthUser", basicAuthUser());
		}
		if (basicAuthPass() != null) {
			config.put("basicAuthPass", basicAuthPass());
		}
		if (customHeaderName() != null) {
			config.put("customHeaderName", customHeaderName());
		}
		if (customHeaderValue() != null) {
			config.put("customHeaderValue", customHeaderValue());
		}

		return new ImmutableOutputInternal.Builder()
			.id(id())
			.collectionID(collectionID())
			.type("webhook")
			.config(config)
			.enabled(enabled())
			.tags(tags())
			.build();
	}
}
