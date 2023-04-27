package org.automationlab.api.controllers;

import org.automationlab.api.services.IUIPathManager;
import org.automationlab.api.services.ServiceResponse;
import org.automationlab.api.services.dto.StartUIPathProcessDto;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RobotsController {
		
	private final IUIPathManager _uiPathManager;
	
	@Autowired
	public RobotsController(IUIPathManager uiPathManager) {
		_uiPathManager = uiPathManager;
	}
	
	@PostMapping(
			value = "/robots/start",
			consumes = MediaType.APPLICATION_JSON_VALUE,
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ServiceResponse<Object>> startProcess(@RequestBody StartUIPathProcessDto model) {
		ServiceResponse<Object> response;
		if(model.releaseName == null) {
			response = new ServiceResponse<Object>("Release name is required");
			return new ResponseEntity<ServiceResponse<Object>>(response, HttpStatus.BAD_REQUEST);
		}
		if(model.manchineNames == null) {
			model.manchineNames = new String[0];
		}

		JSONObject arguments;
		if(model.arguments == null) {
			arguments = new JSONObject();
		}else{
			arguments = new JSONObject(model.arguments);
		}
		
		ServiceResponse<JSONObject> uiPathResponse = _uiPathManager.startProcess(model.releaseName, model.manchineNames, arguments);
		response = new ServiceResponse<Object>(uiPathResponse.success, null, uiPathResponse.message);
		if(response.success) {
			return new ResponseEntity<ServiceResponse<Object>>(response, HttpStatus.OK);
		}
		return new ResponseEntity<ServiceResponse<Object>>(response, HttpStatus.BAD_REQUEST);
	}
}
