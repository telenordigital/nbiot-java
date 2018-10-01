package com.telenordigital.nbiot;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConfigTest {
	@Test
	public void ConfigLoaderTest() {
		final Config cfg = new Config();
		assertFalse(cfg.token().isEmpty());
		assertFalse(cfg.endpoint().isEmpty());
	}
}