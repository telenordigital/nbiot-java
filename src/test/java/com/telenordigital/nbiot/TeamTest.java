package com.telenordigital.nbiot;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

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
			client.invites(team.id());
			try {
				client.acceptInvite(iv.code());
			} catch (ClientException ex) {
				if (ex.status() != HttpStatus.SC_CONFLICT) {
					throw ex;
				}
			}
			client.deleteInvite(team.id(), iv.code());

			client.updateTeam(team);
		} finally {
			client.deleteTeam(team.id());
		}
	}
}