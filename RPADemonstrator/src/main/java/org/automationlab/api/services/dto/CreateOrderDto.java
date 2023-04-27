package org.automationlab.api.services.dto;

import java.time.LocalDate;

public class CreateOrderDto {
	
	public int orderId;
	
	public String processId;
	
	public String businessKey;
	
	public LocalDate orderDate;
	
	public LocalDate latestDeliveryDate;
}
