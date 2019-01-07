package com.telenordigital.nbiot;

import org.immutables.value.Value;

/**
 * A message sent from a device.
 */
@Value.Immutable
@Value.Style(builder = "new")
public interface OutputDataMessage {

    /**
     * The sending device.
     */
    Device device();

    /**
     * The payload sent.
     */
    byte[] payload();

    /**
     * The time received.
     */
    java.time.Instant received();
}
