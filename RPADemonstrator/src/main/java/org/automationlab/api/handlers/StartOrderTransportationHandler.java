package org.automationlab.api.handlers;

import org.automationlab.api.services.ICamundaMessageService;
import org.automationlab.api.services.ServiceResponse;
import org.automationlab.api.services.dto.CamundaMessageDto;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription("startOrderTransportation")
public class StartOrderTransportationHandler implements ExternalTaskHandler {
	
	private final Logger _logger = LoggerFactory.getLogger(StartOrderTransportationHandler.class);
	
	@Autowired
	public ICamundaMessageService camundaMessageService;

	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {		
		
		try {			
			CamundaMessageDto camundaMessage = new CamundaMessageDto("StartOrderTransportationMessage");
			camundaMessage.setBusinessKey(externalTask.getBusinessKey());
			camundaMessage.addVariable("totalMass", "100");
			camundaMessage.addVariable("totalVolume", "2.5");
			camundaMessage.addVariable("numberOfItems", "2");
			camundaMessage.addVariable("latestDeliveryDate", "2023-12-31");
			camundaMessage.addVariable("shipperAddress", "Steinmüllerallee 1, 51643 Gummersbach");
			camundaMessage.addVariable("consignueAddress", "Steinmüllerallee 1, 51643 Gummersbach");
					
			ServiceResponse<Object> serviceResponse = camundaMessageService.registerMessage(camundaMessage);
						
			if(serviceResponse.success == false) {
				externalTaskService.handleFailure(externalTask, serviceResponse.message, null, 0, 0);
			}else {			
				externalTaskService.complete(externalTask); 
			}
		}catch(Exception e) {
			_logger.error(e.getMessage());
			externalTaskService.handleFailure(externalTask, e.getMessage(), null, 0, 0);
		}
	}
}
