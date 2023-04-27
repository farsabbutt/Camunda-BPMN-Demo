package org.automationlab.api.services;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IUIPathManager {

	public ServiceResponse<JSONObject> getReleaseByName(String releaseName);
	
	public ServiceResponse<JSONArray> getMachinesByNames(String... machineNames);
	
	public ServiceResponse<JSONObject> getMachineByName(String machineName);
	
	public ServiceResponse<JSONObject> startProcess(String releaseName, String[] machineNames, JSONObject arguments);
	
	public ServiceResponse<JSONObject> startProcess(String releaseKey, int[] machineIds, JSONObject arguments);
}
