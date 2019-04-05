package com.telenordigital.nbiot;

import org.junit.Test;

public class ClientTest {
	@Test
	public void TestClientCreation() throws Exception {
		new Client();
	}

	@Test
	public void TestSystemConfig() throws Exception {
		new Client().systemConfig();
	}
}