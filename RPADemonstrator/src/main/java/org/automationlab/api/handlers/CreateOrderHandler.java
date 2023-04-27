package org.automationlab.api.handlers;

import org.automationlab.api.services.IUIPathManager;
import org.automationlab.api.services.ServiceResponse;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription("createOrder")
public class CreateOrderHandler implements ExternalTaskHandler {

	@Autowired
	public IUIPathManager uiPathManager;
	  
	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
		try {
			JSONObject arguments = new JSONObject();
			arguments.put("ProcessId", externalTask.getProcessInstanceId());
			arguments.put("BusinessKey", externalTask.getBusinessKey());
			arguments.put("TotalMass", (String)externalTask.getVariableTyped("totalMass").getValue());
			arguments.put("TotalVolume", (String)externalTask.getVariableTyped("totalVolume").getValue());
			arguments.put("NumberOfItems", (String)externalTask.getVariableTyped("numberOfItems").getValue());
			arguments.put("LatestDeliveryDate", (String)externalTask.getVariableTyped("latestDeliveryDate").getValue());
			arguments.put("ShipperAddress", (String)externalTask.getVariableTyped("shipperAddress").getValue());
			arguments.put("ConsignueAddress", (String)externalTask.getVariableTyped("consignueAddress").getValue());
			
			ServiceResponse<JSONObject> serviceResponse = uiPathManager.startProcess("CreateOrderProcess", new String[0], arguments);
			
			if(serviceResponse.success == false) {
				externalTaskService.handleFailure(externalTask, serviceResponse.message, null, 0, 0);
			}else {			
				externalTaskService.complete(externalTask); 
			}   
		}catch(Exception e) {
			externalTaskService.handleFailure(externalTask, e.getMessage(), null, 0, 0);
		}
	}
}
