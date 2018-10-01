package com.telenordigital.nbiot;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClientTest {
	/**
	 *  Just create a client and make sure it won't throw an exception
	 */
	@Test
	public void TestClientCreation() throws Exception {
		new Client();
		assertTrue(true);
	}
}