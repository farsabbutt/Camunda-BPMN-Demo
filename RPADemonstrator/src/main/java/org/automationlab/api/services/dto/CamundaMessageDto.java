package org.automationlab.api.services.dto;

import java.util.HashMap;

public class CamundaMessageDto {
	
	public String messageName;
	
	public String processInstanceId;
	
	public String businessKey;
	
	public HashMap<String, CamundaMessageVariableDto> processVariables;
	
	public CamundaMessageDto(String messageName) {
		this.messageName = messageName;
		processVariables = new HashMap<String, CamundaMessageVariableDto>();
	}
	
	public CamundaMessageDto(String messageName, String processInstanceId) {
		this.messageName = messageName;
		this.processInstanceId = processInstanceId;
		processVariables = new HashMap<String, CamundaMessageVariableDto>();
	}
		
	public CamundaMessageDto(String messageName, String processInstanceId, HashMap<String, CamundaMessageVariableDto> processVariables) {
		this.messageName = messageName;
		this.processInstanceId = processInstanceId;
		this.processVariables = processVariables;
	}
	
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	public void addVariable(String name, String value) {
		processVariables.put(name, new CamundaMessageVariableDto(value, "String"));
	}
}
