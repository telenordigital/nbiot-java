package com.telenordigital.nbiot;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpStatus;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;

/**
* This is the client for the Telenor NB-IoT REST API.
*/
public class Client {
	private static final Logger logger = Logger.getLogger(Client.class.getName());

	private static final String TOKEN_HEADER = "X-API-Token";
	
	private final String endpoint;
	private final String token;
	
	static {
		Unirest.setObjectMapper(new ObjectMapper() {
			private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
				= new com.fasterxml.jackson.databind.ObjectMapper()
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
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
	* Create a new Telenor NB-IoT client using the configuration. The configuration
	* can either be set by adding a ${HOME}/.telenor-nbiot file or by setting the
	* environment variables TELENOR_NBIOT_ADDRESS and TELENOR_NBIOT_TOKEN. The environment
	* variables override the configuration file.
	*/
	public Client() throws ClientException {
		final Config cfg = new Config();
		this.token = cfg.token();
		this.endpoint = cfg.endpoint();
		
		ping();
	}
	
	/**
	* Create a new Telenor NB-IoT client with the specified endpoint and token.
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
				.patch(endpoint + path)
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
			Unirest.delete(endpoint + path)
				.header(TOKEN_HEADER, token)
				.asString();
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
		return get("/teams", Team.TeamList.class).teams();
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
		return get("/collections", Collection.CollectionList.class).collections();
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
		return get("/collections/" + collectionID + "/devices", Device.DeviceList.class).devices();
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


	/**
	* Retrieve an output.
	*/
	public Output output(final String collectionID, final String outputID) throws ClientException {
		return get("/collections/" + collectionID + "/outputs/" + outputID, Output.class);
	}
	
	/**
	* Retrieve all outputs in a collection.
	*/
	public Output[] outputs(final String collectionID) throws ClientException {
		return get("/collections/" + collectionID + "/outputs", Output.OutputList.class).outputs();
	}
	
	/**
	* Create an output.
	*/
	public Output createOutput(final String collectionID, final Output template) throws ClientException {
		return create("/collections/" + collectionID + "/outputs", template, Output.class);
	}
	
	/**
	* Update an output.
	*/
	public Output updateOutput(final String collectionID, final Output output) throws ClientException {
		return update("/collections/" + collectionID + "/outputs/" + output.id(), output, Output.class);
	}
	
	/**
	* Delete an output.
	*/
	public void deleteOutput(final String collectionID, final String outputID) throws ClientException {
		delete("/collections/" + collectionID + "/outputs/" + outputID);
	}


	/**
	* Receive data messages sent by all devices in a collection.
	*/
	public OutputStream collectionOutput(final String collectionID, OutputHandler handler) throws ClientException {
		return output("/collections/" + collectionID, handler);
	}

	/**
	* Receive data messages sent by a device.
	*/
	public OutputStream deviceOutput(final String collectionID, final String deviceID, OutputHandler handler) throws ClientException {
		return output("/collections/" + collectionID + "/devices/" + deviceID, handler);
	}

	public static interface OutputHandler {
		public void onData(OutputDataMessage msg);
		public void onEnd();
	}

	private OutputStream output(final String path, OutputHandler handler) throws ClientException {
		URI uri = null;
		String scheme = "wss";
		try {
			URI endpointURI = new URI(endpoint);
			if (endpointURI.getScheme().equals("http")) {
				scheme = "ws";
			}
			uri = new URI(scheme, null, endpointURI.getHost(), endpointURI.getPort(), path + "/from", null, null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return new OutputStream(uri, token, handler);
	}
}
