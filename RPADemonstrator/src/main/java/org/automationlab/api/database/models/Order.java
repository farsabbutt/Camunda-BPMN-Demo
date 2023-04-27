package org.automationlab.api.database.models;

import java.time.LocalDate;

public class Order {
	
	public String processId;
	
	public int orderId;
	
	public String businessKey;
	
	public String status;
	
	public LocalDate orderDate;
	
	public LocalDate latestDeliveryDate;
	
	public LocalDate expectedTruckArrivalDate;
	
	public LocalDate expectedDeliveryDate;
	
	public LocalDate truckArrivalDate;
	
	public LocalDate deliveryDate;
	
	public final static String StatusPending = "Pending";
	
	public final static String StatusApproved = "Approved";
	
	public final static String StatusDeclined = "Declined";
	
	public final static String StatusPickedUp = "PickedUP";
	
	public final static String StatusDelivered = "Delivered";
	
}
