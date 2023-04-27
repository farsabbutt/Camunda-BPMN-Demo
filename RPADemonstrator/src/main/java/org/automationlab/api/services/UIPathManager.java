package org.automationlab.api.services;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.net.URI;
import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UIPathManager implements IUIPathManager {
	
	private final Logger _logger = LoggerFactory.getLogger(UIPathManager.class);
	
	private static final String AuthenticationError = "Authentication request failed";
	private static final String ReleaseNotFoundError = "Release is not found";
	private static final String MachineNotFoundError = "Machine is not found";
	private static final String NotAllMachinesFoundError = "Not all machines were found";
			
	private final String baseUrl;
	private final String organization;
	private final String tenant;
	private final String clientId;
	private final String clientSecret;
	private final String scope;
	private final String organizationUnitId;
	private final JSONObject defaultArguments;
	
	private String accessToken;
	private int refreshTokenAttempts;
	
	private HttpClient httpClient;
	
	public UIPathManager(String baseUrl, String organization, String tenant, String clientId, String clientSecret, String scope, String organizationUnitId, JSONObject defaultArguments) {
		this.baseUrl = baseUrl;
		this.organization = organization;
		this.tenant = tenant;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.scope = scope;
		this.organizationUnitId = organizationUnitId;
		this.defaultArguments = defaultArguments;
		httpClient = HttpClient.newHttpClient();
		accessToken = null;
		refreshTokenAttempts = 0;
	}
	
	public ServiceResponse<JSONObject> getReleaseByName(String releaseName){	
		// constructs request
		Builder requestBuilder = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl + "/" + organization + "/" + tenant + "/orchestrator_/odata/Releases"))
				.header("x-uipath-organizationunitid", organizationUnitId)
		        .GET();
		
		// send request
		ServiceResponse<String> response = sendRequest(requestBuilder);
		if(response.success == false) {
			return new ServiceResponse<JSONObject>(response.message);
		}
		
		try {		
			// extract the list of releases
			JSONObject responseBody = new JSONObject(response.data);
			JSONArray releases = responseBody.getJSONArray("value");
			
			// find and return the release with name equal to @releaseName
			for (int i = 0; i < releases.length(); i++) {
				JSONObject release = releases.getJSONObject(i);
				
				if(release.getString("Name").equals(releaseName)) {
					return new ServiceResponse<JSONObject>(true, release);
				}
			}
		}catch(JSONException e) {
			// return caught JSON exception message
			_logger.error(e.getMessage());
			return new ServiceResponse<JSONObject>(e.getMessage());
		}
		
		// return error if release with name equal to @releaseName was not found
		return new ServiceResponse<JSONObject>(ReleaseNotFoundError);
	}
	
	public ServiceResponse<JSONArray> getMachinesByNames(String... machineNames){
		// constructs request
		Builder requestBuilder = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl + "/" + organization + "/" + tenant + "/orchestrator_/odata/Machines"))
		        .GET();
		
		// send request
		ServiceResponse<String> response = sendRequest(requestBuilder);
		if(response.success == false) {
			return new ServiceResponse<JSONArray>(response.message);
		}
		
		JSONArray requestedMachines = new JSONArray();
		try {		
			// extract the list of machines
			JSONObject responseBody = new JSONObject(response.data);
			JSONArray machines = responseBody.getJSONArray("value");
			
			// find machines with names from @machineNames
			for (int i = 0; i < machines.length(); i++) {
				JSONObject machine = machines.getJSONObject(i);
				if(Arrays.asList(machineNames).indexOf(machine.getString("Name")) != -1) {
					requestedMachines.put(machine);
				}
			}
		}catch(JSONException e) {
			// return caught JSON exception message
			_logger.error(e.getMessage());
			return new ServiceResponse<JSONArray>(e.getMessage());
		}
		
		// return found machines
		return new ServiceResponse<JSONArray>(true, requestedMachines);
	}

	public ServiceResponse<JSONObject> getMachineByName(String machineName){
		// request the machine with name equal to @machineName
		ServiceResponse<JSONArray> response = getMachinesByNames(machineName);
		if(response.success == false) {
			return new ServiceResponse<JSONObject>(response.message);
		}
		
		// return error if machine with name equal to @machineName was not found
		if(response.data.length() == 0) {
			return new ServiceResponse<JSONObject>(MachineNotFoundError);
		}
		
		// return machine with name equal to @machineName
		return new ServiceResponse<JSONObject>(true, response.data.getJSONObject(0));
	}
		
	public ServiceResponse<JSONObject> startProcess(String releaseName, String[] machineNames, JSONObject arguments){
		// request the release with name equal to @releaseName
		ServiceResponse<JSONObject> releaseResponse = getReleaseByName(releaseName);
		if(releaseResponse.success == false) {
			return new ServiceResponse<JSONObject>(releaseResponse.message);
		}
		
		// request the machines with names from @machineNames
		ServiceResponse<JSONArray> machinesResponse = getMachinesByNames(machineNames);
		if(machinesResponse.success == false) {
			return new ServiceResponse<JSONObject>(machinesResponse.message);
		}
		if(machineNames.length != machinesResponse.data.length()) {
			return new ServiceResponse<JSONObject>(NotAllMachinesFoundError);
		}
		
		String releaseKey;
		int[] machineIds;
		
		// extract @releaseKey and @machineIds
		try {
			releaseKey = releaseResponse.data.getString("Key");
			machineIds = new int[machinesResponse.data.length()];
			for(int i = 0; i < machineIds.length; i++) {
				machineIds[i] = machinesResponse.data.getJSONObject(i).getInt("Id");
			}
		}catch(JSONException e) {
			// return caught JSON exception message
			_logger.error(e.getMessage());
			return new ServiceResponse<JSONObject>(e.getMessage());
		}
	
		return startProcess(releaseKey, machineIds, arguments);
	}
		
	public ServiceResponse<JSONObject> startProcess(String releaseKey, int[] machineIds, JSONObject arguments){
			
		// construct body			
		JSONObject startInfo = new JSONObject();
		startInfo.put("JobPriority", "Normal");
		startInfo.put("JobsCount", 1);
		startInfo.put("ReleaseKey", releaseKey);
		startInfo.put("ResumeOnSameContext", false);
		startInfo.put("RunAsMe", false);
		startInfo.put("RuntimeType", "Unattended");
		startInfo.put("Strategy", "ModernJobsCount");
		
		JSONObject processArguments = new JSONObject();
		defaultArguments.keySet().forEach(key ->
	    {
	    	processArguments.put(key, defaultArguments.get(key));
	    });
		arguments.keySet().forEach(key ->
	    {
	    	processArguments.put(key, arguments.get(key));
	    });		
		startInfo.put("InputArguments", processArguments.toString());
				
		if(machineIds.length > 0) {
			JSONArray machines = new JSONArray();
			for(int i = 0; i < machineIds.length; i++) {
				JSONObject machine = new JSONObject();
				machine.put("MachineId", machineIds[i]);
				machines.put(machine);
			}
			startInfo.put("MachineRobots", machines);
		}
		
		JSONObject body = new JSONObject();
		body.put("startInfo", startInfo);
		System.out.println(body.toString());
		
		// constructs request
		Builder requestBuilder = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl + "/" + organization + "/" + tenant + "/orchestrator_/odata/Jobs/UiPath.Server.Configuration.OData.StartJobs"))
				.header("Content-Type", "application/json")
				.header("x-uipath-organizationunitid", organizationUnitId)
		        .POST(BodyPublishers.ofString(body.toString()));
		
		ServiceResponse<String> response = sendRequest(requestBuilder);
		if(response.success == false) {
			return new ServiceResponse<JSONObject>(response.message);
		}
		try {
			return new ServiceResponse<JSONObject>(true, new JSONObject(response.data));
		}catch(JSONException e) {
			// return caught JSON exception message
			_logger.error(e.getMessage());
			return new ServiceResponse<JSONObject>(e.getMessage());
		}		
	}
	
	private boolean refreshToken() {
		// construct token request body
		StringBuilder sb = new StringBuilder();
		sb.append("grant_type=client_credentials");
		sb.append("&");
		sb.append("client_id=");
		sb.append(clientId);
		sb.append("&");
		sb.append("client_secret=");
		sb.append(clientSecret);
		sb.append("&");
		sb.append("scope=");
		sb.append(scope);		
		String body = sb.toString();
		
		// constructs request
		HttpRequest request = HttpRequest.newBuilder()
		        .uri(URI.create(baseUrl + "/identity_/connect/token"))
		        .header("Content-Type", "application/x-www-form-urlencoded")
		        .POST(BodyPublishers.ofString(body))
		        .build();		
		try {
			// request access token
			HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
			
			// setup token if request was successful
			if(response.statusCode() / 100 == 2) {
				JSONObject jsonResponse = new JSONObject(response.body());
				accessToken = jsonResponse.getString("access_token");
				refreshTokenAttempts = 0;
				return true;
			}
		} catch (Exception e) {	
			_logger.error(e.getMessage());
		}
		
		// retry request access token
		refreshTokenAttempts++;
		if(refreshTokenAttempts < 2) {
			return refreshToken();
		}
		refreshTokenAttempts = 0;
		return false;
	}	
	
	private ServiceResponse<String> sendRequest(Builder requestBuilder) {
		// check of access token received
		if(accessToken == null) {
			boolean refreshTokenResult = refreshToken();
			if(refreshTokenResult == false) {
				return new ServiceResponse<String>(AuthenticationError);
			}
		}
		
		// add access token to request
		requestBuilder.header("Authorization", "Bearer " + accessToken);
		
		try {
			// first request attempt
			HttpResponse<String> response = httpClient.send(requestBuilder.build(), BodyHandlers.ofString());
			
			// return response string if request was successful 
			if(response.statusCode() / 100 == 2) {
				return new ServiceResponse<String>(true, response.body());
			}
			
			// check of request was unauthenticated and refresh the token
			if(response.statusCode() == 401) {
				boolean refreshTokenResult = refreshToken();
				if(refreshTokenResult == false) {
					return new ServiceResponse<String>(AuthenticationError);
				}
				
				// add new access token to request
				requestBuilder.header("Authorization", "Bearer " + accessToken);
				
				// second request attempt
				response = httpClient.send(requestBuilder.build(), BodyHandlers.ofString());
				
				// return response string if request was successful 
				if(response.statusCode() / 100 == 2) {
					return new ServiceResponse<String>(true, response.body());
				}
			}
			
			// return unexpected response
			return new ServiceResponse<String>("Unexpected response " + response.body());
		} catch (Exception e) {	
			// return error message
			_logger.error(e.getMessage());
			return new ServiceResponse<String>(e.getMessage());
		}
	}
}
