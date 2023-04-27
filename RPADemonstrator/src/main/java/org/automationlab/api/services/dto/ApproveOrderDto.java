package org.automationlab.api.services.dto;

import java.time.LocalDate;

public class ApproveOrderDto {
	
	public int orderId;
	
	public LocalDate expectedTruckArrivalDate;
	
	public LocalDate expectedDeliveryDate;
}
