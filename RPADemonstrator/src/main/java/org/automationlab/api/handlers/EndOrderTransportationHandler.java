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
@ExternalTaskSubscription("endOrderTransportation")
public class EndOrderTransportationHandler implements ExternalTaskHandler {
	
	private final Logger _logger = LoggerFactory.getLogger(EndOrderTransportationHandler.class);
	
	@Autowired
	public ICamundaMessageService camundaMessageService;

	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {		
		
		try {			
			CamundaMessageDto camundaMessage = new CamundaMessageDto("OrderTransportationEndMessage");
			camundaMessage.setBusinessKey(externalTask.getBusinessKey());
					
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
