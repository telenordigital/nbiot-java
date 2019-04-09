package com.telenordigital.nbiot;

import java.util.Map;

import javax.annotation.Nullable;

/**
 * An output.
 */
public interface Output {
    /**
     * The output's ID.
     */
    @Nullable
    String id();

    /**
     * The collection the output belongs to.
     */
    @Nullable
    String collectionID();

    /**
     * Whether the output is enabled.
     */
    @Nullable
    Boolean enabled();

    /**
     * Output tags.
     */
    @Nullable
    Map<String, String> tags();

    @Nullable
    OutputInternal toInternal();
}
