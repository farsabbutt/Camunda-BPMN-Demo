package org.automationlab.api.services.dto;

import java.time.LocalDate;

public class CreateOrderManuallyDto {

	public String processId;
	
	public int orderId;
	
	public String businessKey;
	
	public LocalDate latestDeliveryDate;
}
