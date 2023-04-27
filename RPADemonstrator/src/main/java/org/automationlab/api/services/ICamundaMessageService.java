package org.automationlab.api.services;

import org.automationlab.api.services.dto.CamundaMessageDto;

public interface ICamundaMessageService {
	
	public ServiceResponse<Object> sendMessage(CamundaMessageDto model);
	
	public ServiceResponse<Object> registerMessage(CamundaMessageDto model);
	
}
