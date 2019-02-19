package com.telenordigital.nbiot;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.time.Instant;

@Value.Immutable
@Value.Style(builder = "new")
public interface DataSearchParameters {
	/**
	 * Limit number of results when querying data endpoint
	 * @return Limit parameter as an Integer
	 */
	@Nullable
	Integer limit();

	/**
	 * Fetch data since given Instant
	 * @return Since parameter as Instant
	 */
	@Nullable
	Instant since();

	/**
	 * Fetch data until given Instant
	 * @return Until parameter as Instant
	 */
	@Nullable
	Instant until();
}