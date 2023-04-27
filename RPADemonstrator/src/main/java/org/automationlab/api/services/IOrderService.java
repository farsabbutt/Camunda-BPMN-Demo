package org.automationlab.api.services;

import org.automationlab.api.database.models.Order;
import org.automationlab.api.services.dto.*;

public interface IOrderService{
	
	public ServiceResponse<Order> createOrder(CreateOrderDto model);
	
	public ServiceResponse<Order> createOrderError(CreateOrderErrorDto model);
	
	public ServiceResponse<Order> createOrderManually(CreateOrderManuallyDto model);
	
	public ServiceResponse<Order> approveOrder(ApproveOrderDto model);
	
	public ServiceResponse<Order> declineOrder(DeclineOrderDto model);
	
	public ServiceResponse<Order> pickUpOrder(PickUpOrderDto model);
	
	public ServiceResponse<Order> deliverOrder(DeliverOrderDto model);
}