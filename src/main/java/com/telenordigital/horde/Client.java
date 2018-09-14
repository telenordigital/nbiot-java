package com.telenordigital.horde;

import java.io.IOException;

import org.apache.http.HttpStatus;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
* This is the client for the Horde REST API.
*/
public class Client {
	private static final String TOKEN_HEADER = "X-API-Token";
	
	private final String endpoint;
	private final String token;
	
	static {
		Unirest.setObjectMapper(new ObjectMapper() {
			private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
				= new com.fasterxml.jackson.databind.ObjectMapper();
			
			public <T> T readValue(String value, Class<T> valueType) {
				try {
					return jacksonObjectMapper.readValue(value, valueType);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			
			public String writeValue(Object value) {
				try {
					return jacksonObjectMapper.writeValueAsString(value);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}
	
	/**
	* Create a new Horde client using the configuration. The configuration
	* can either be set by adding a ${HOME}/.horde file or by setting the
	* environment variables HORDE_ADDRESS and HORDE_TOKEN. The environment
	* variables override the configuration file.
	*/
	public Client() throws ClientException {
		final Config cfg = new Config();
		this.token = cfg.token();
		this.endpoint = cfg.endpoint();
		
		ping();
	}
	
	/**
	* Create a new Horde client with the specified endpoint and token.
	*/
	public Client(final String endpoint, final String token) {
		this.endpoint = endpoint;
		this.token = token;
	}
	
	/**
	* Ping the backend to ensure it is reachable
	*/
	private void ping() throws ClientException {
		try {
			final HttpResponse<String> resp = Unirest
				.get(endpoint + "/")
				.header(TOKEN_HEADER, token)
				.asString();
			if (resp.getStatus() == HttpStatus.SC_FORBIDDEN) {
				throw new ClientException(resp.getBody(), resp.getStatus());
			}
			
		} catch (UnirestException ex) {
			throw new ClientException(ex);
		}
	}
	
	/**
	* generic GET on a resource
	*/
	private <T> T get(final String path, final Class<T> cls) 
	throws ClientException {
		try {
			return Unirest
				.get(endpoint + path)
				.header(TOKEN_HEADER, token)
				.asObject(cls).getBody();
		} catch (final UnirestException ue) {
			throw new ClientException(ue);
		}
	}
	
	/**
	* generic POST on a resource
	*/
	private <T> T create(
		final String path, final T template, final Class<T> cls) 
		throws ClientException {
		try {
			return Unirest
				.post(endpoint + path)
				.header(TOKEN_HEADER, token)
				.body(template)
				.asObject(cls)
				.getBody();
		} catch (final UnirestException ue) {
			throw new ClientException(ue);
		}
	}
	
	/**
	* generic PATCH on a resource
	*/
	private <T> T update(final String path, T updated, final Class<T> cls) 
	throws ClientException {
		try {
			return Unirest
				.put(endpoint + path)
				.header(TOKEN_HEADER, token)
				.body(updated)
				.asObject(cls)
				.getBody();
		} catch (final UnirestException ue) {
			throw new ClientException(ue);
		}
	}
	
	/**
	* generic DELETE on a resource
	*/
	private void delete(final String path) throws ClientException {
		try {
			Unirest.delete(endpoint + path).asString();
		} catch (final UnirestException ue) {
			throw new ClientException(ue);
		}
	}
	
	/**
	* Retrieve a team.
	*/
	public Team team(final String teamID) 
	throws ClientException {
		return get("/teams/" + teamID, Team.class);
	}
	
	/**
	* Retrieve all teams the user has access to.
	*/
	public Team[] teams() throws ClientException {
		return get("/teams", Team[].class);
	}
	
	/**
	* Create a new team.
	*/
	public Team createTeam(final Team template) throws ClientException {
		return create("/teams", template, Team.class);
	}
	
	/**
	* Update a team.
	*/
	public Team updateTeam(final Team team) throws ClientException {
		return update("/teams/" + team.id(), team, Team.class);
	}
	
	/**
	* Delete a team.
	*/
	public void deleteTeam(final String teamID) throws ClientException {
		delete("/teams/" + teamID);
	}
	

	/**
	* Retrieve a collection.
	*/
	public Collection collection(final String collectionID) throws ClientException {
		return get("/collections/" + collectionID, Collection.class);
	}
	
	/**
	* Retrieve all collections the user has access to.
	*/
	public Collection[] collections() throws ClientException {
		return get("/collections", Collection[].class);
	}
	
	/**
	* Create a new collection.
	*/
	public Collection createCollection(final Collection template) throws ClientException {
		return create("/collections", template, Collection.class);
	}
	
	/**
	* Update a collection.
	*/
	public Collection updateCollection(final Collection collection) throws ClientException {
		return update("/collections/" + collection.id(), collection, Collection.class);
	}
	
	/**
	* Delete a collection.
	*/
	public void deleteCollection(final String collectionID) throws ClientException {
		delete("/collections/" + collectionID);
	}
	

	/**
	* Retrieve a device.
	*/
	public Device device(final String collectionID, final String deviceID) throws ClientException {
		return get("/collections/" + collectionID + "/devices/" + deviceID, Device.class);
	}
	
	/**
	* Retrieve all devices in a collection.
	*/
	public Device[] devices(final String collectionID) throws ClientException {
		return get("/collections/" + collectionID + "/devices", Device[].class);
	}
	
	/**
	* Create a device.
	*/
	public Device createDevice(final String collectionID, final Device template) throws ClientException {
		return create("/collections/" + collectionID + "/devices", template, Device.class);
	}
	
	/**
	* Update a device.
	*/
	public Device updateDevice(final String collectionID, final Device device) throws ClientException {
		return update("/collections/" + collectionID + "/devices/" + device.id(), device, Device.class);
	}
	
	/**
	* Delete a device.
	*/
	public void deleteDevice(final String collectionID, final String deviceID) throws ClientException {
		delete("/collections/" + collectionID + "/devices/" + deviceID);
	}
}
