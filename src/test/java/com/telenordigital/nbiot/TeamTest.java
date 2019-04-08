package com.telenordigital.nbiot;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

public class TeamTest {
	@Test
	public void testTeam() throws Exception {
		Client client = new Client();
		Map<String, String> tags = new HashMap<String, String>();
		tags.put("name", "The test team");
		Team team = new ImmutableTeam.Builder().tags(tags).build();
		team = client.createTeam(team);
		try {
			client.teams();
			
			Invite iv = client.createInvite(team.id());
			try {
				client.invites(team.id());
				ClientException ex = assertThrows(ClientException.class, new ThrowingRunnable(){
					@Override
					public void run() throws Throwable {
						client.acceptInvite(iv.code());
					}
				});
				assertEquals(HttpStatus.SC_CONFLICT, ex.status());
			} finally {
				client.deleteInvite(team.id(), iv.code());
			}

			client.updateTeam(team);
		} finally {
			client.deleteTeam(team.id());
		}
	}
}