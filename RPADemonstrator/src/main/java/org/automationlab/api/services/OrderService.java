package org.automationlab.api.services;

import java.time.LocalDate;
import java.util.HashMap;

import org.automationlab.api.database.interfaces.IOrderRepository;
import org.automationlab.api.database.models.Order;
import org.automationlab.api.services.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderService implements IOrderService {
	
	private final Logger _logger = LoggerFactory.getLogger(OrderService.class);
	
	private final IOrderRepository _orderRepository;
	
	private final ICamundaMessageService _camundaMessageService;
	
	public OrderService(IOrderRepository orderRepository, ICamundaMessageService camundaMessageService) {
		_orderRepository = orderRepository;
		_camundaMessageService = camundaMessageService;
	}
	
	public ServiceResponse<Order> createOrder(CreateOrderDto model){
		ServiceResponse<Order> serviceResponse = new ServiceResponse<Order>();
		try {
			Order order = new Order();
			order.processId = model.processId;
			order.businessKey = model.businessKey;
			order.orderId = model.orderId;
			order.orderDate = model.orderDate;
			order.latestDeliveryDate = model.latestDeliveryDate;
			order.status = Order.StatusPending;
			_orderRepository.addOrder(order);
			serviceResponse.data = order;
			
			HashMap<String, CamundaMessageVariableDto> variables = new HashMap<String, CamundaMessageVariableDto>();
			variables.put("orderId", new CamundaMessageVariableDto(String.valueOf(order.orderId), "String"));
			variables.put("orderStatus", new CamundaMessageVariableDto(order.status, "String"));
			variables.put("orderDate", new CamundaMessageVariableDto(order.orderDate.toString(), "String"));
			_camundaMessageService.registerMessage(new CamundaMessageDto("OrderCreartedMessage", order.processId, variables));
		}
		catch(Exception e){
			serviceResponse.success = false;
			serviceResponse.message = e.getMessage();
			_logger.error(serviceResponse.message);
		}
		return serviceResponse;
	}
	
	public ServiceResponse<Order> createOrderError(CreateOrderErrorDto model){
		ServiceResponse<Order> serviceResponse = new ServiceResponse<Order>();
		try {
			_camundaMessageService.registerMessage(new CamundaMessageDto("OrderCreationErrorMessage", model.processId));
		}
		catch(Exception e){
			serviceResponse.success = false;
			serviceResponse.message = e.getMessage();
			_logger.error(serviceResponse.message);
		}
		return serviceResponse;
	}
	
	public ServiceResponse<Order> createOrderManually(CreateOrderManuallyDto model){
		ServiceResponse<Order> serviceResponse = new ServiceResponse<Order>();
		try {			
			Order order = _orderRepository.getOrderByProcessId(model.processId);
			if(order == null) {
				order = new Order();
				order.processId = model.processId;
				order.businessKey = model.businessKey;
				order.orderId = model.orderId;
				order.orderDate = LocalDate.now();
				order.latestDeliveryDate = model.latestDeliveryDate;
				order.status = Order.StatusPending;
				_orderRepository.addOrder(order);
			}else {
				order.orderId = model.orderId;
				order.orderDate = LocalDate.now();
				order.latestDeliveryDate = model.latestDeliveryDate;
				order.status = Order.StatusPending;
				_orderRepository.updateOrderByProcessId(order);
			}
			serviceResponse.data = order;
		}
		catch(Exception e){
			serviceResponse.success = false;
			serviceResponse.message = e.getMessage();
			_logger.error(serviceResponse.message);
		}
		return serviceResponse;
	}
	
	public ServiceResponse<Order> approveOrder(ApproveOrderDto model){
		ServiceResponse<Order> serviceResponse = new ServiceResponse<Order>();
		try {
			Order order = _orderRepository.getOrderById(model.orderId);
			if(order == null) {
				throw new Exception("Order not found");
			}
			if(order.status.equals(Order.StatusPending) == false) {
				serviceResponse.data = order;
				serviceResponse.success = false;
				serviceResponse.message = "Only pending order can be approved";
				return serviceResponse;
			}
			order.status = Order.StatusApproved;
			order.expectedTruckArrivalDate = model.expectedTruckArrivalDate;
			order.expectedDeliveryDate = model.expectedDeliveryDate;
			_orderRepository.updateOrder(order);
			serviceResponse.data = order;
			
			HashMap<String, CamundaMessageVariableDto> variables = new HashMap<String, CamundaMessageVariableDto>();
			variables.put("orderStatus", new CamundaMessageVariableDto(order.status, "String"));
			variables.put("expectedTruckArrivalDate", new CamundaMessageVariableDto(order.expectedTruckArrivalDate.toString(), "String"));
			variables.put("expectedDeliveryDate", new CamundaMessageVariableDto(order.expectedDeliveryDate.toString(), "String"));
			_camundaMessageService.registerMessage(new CamundaMessageDto("OrderApprovedMessage", order.processId, variables));
		}
		catch(Exception e){
			serviceResponse.success = false;
			serviceResponse.message = e.getMessage();
			_logger.error(serviceResponse.message);
		}
		return serviceResponse;
	}
	
	public ServiceResponse<Order> declineOrder(DeclineOrderDto model){
		ServiceResponse<Order> serviceResponse = new ServiceResponse<Order>();
		try {
			Order order = _orderRepository.getOrderById(model.orderId);
			if(order == null) {
				throw new Exception("Order not found");
			}
			if(order.status.equals(Order.StatusPending) == false) {
				serviceResponse.data = order;
				serviceResponse.success = false;
				serviceResponse.message = "Only pending order can be declined";
				return serviceResponse;
			}
			order.status = Order.StatusDeclined;
			_orderRepository.updateOrder(order);
			serviceResponse.data = order;

			HashMap<String, CamundaMessageVariableDto> variables = new HashMap<String, CamundaMessageVariableDto>();
			variables.put("orderStatus", new CamundaMessageVariableDto(order.status, "String"));
			_camundaMessageService.registerMessage(new CamundaMessageDto("OrderDeclinedMessage", order.processId, variables));

		}
		catch(Exception e){
			serviceResponse.success = false;
			serviceResponse.message = e.getMessage();
			_logger.error(serviceResponse.message);
		}
		return serviceResponse;
	}
	
	public ServiceResponse<Order> pickUpOrder(PickUpOrderDto model){
		ServiceResponse<Order> serviceResponse = new ServiceResponse<Order>();
		try {
			Order order = _orderRepository.getOrderById(model.orderId);
			if(order == null) {
				throw new Exception("Order not found");
			}
			if(order.status.equals(Order.StatusApproved) == false) {
				serviceResponse.data = order;
				serviceResponse.success = false;
				serviceResponse.message = "Only approved order can be picked up";
				return serviceResponse;
			}
			order.status = Order.StatusPickedUp;
			order.truckArrivalDate = model.truckArrivalDate;
			_orderRepository.updateOrder(order);
			serviceResponse.data = order;
			
			HashMap<String, CamundaMessageVariableDto> variables = new HashMap<String, CamundaMessageVariableDto>();
			variables.put("orderStatus", new CamundaMessageVariableDto(order.status, "String"));
			variables.put("truckArrivalDate", new CamundaMessageVariableDto(order.truckArrivalDate.toString(), "String"));
			_camundaMessageService.registerMessage(new CamundaMessageDto("OrderPickedMessage", order.processId, variables));
		}
		catch(Exception e){
			serviceResponse.success = false;
			serviceResponse.message = e.getMessage();
			_logger.error(serviceResponse.message);
		}
		return serviceResponse;
	}
	
	public ServiceResponse<Order> deliverOrder(DeliverOrderDto model){
		ServiceResponse<Order> serviceResponse = new ServiceResponse<Order>();
		try {
			Order order = _orderRepository.getOrderById(model.orderId);
			if(order == null) {
				throw new Exception("Order not found");
			}
			if(order.status.equals(Order.StatusPickedUp) == false) {
				serviceResponse.data = order;
				serviceResponse.success = false;
				serviceResponse.message = "Only picked order can be delivered";
				return serviceResponse;
			}
			order.status = Order.StatusDelivered;
			order.deliveryDate = model.deliveryDate;
			_orderRepository.updateOrder(order);
			serviceResponse.data = order;
			
			HashMap<String, CamundaMessageVariableDto> variables = new HashMap<String, CamundaMessageVariableDto>();
			variables.put("orderStatus", new CamundaMessageVariableDto(order.status, "String"));
			variables.put("deliveryDate", new CamundaMessageVariableDto(order.deliveryDate.toString(), "String"));
			_camundaMessageService.registerMessage(new CamundaMessageDto("OrderDeliveredMessage", order.processId, variables));
		}
		catch(Exception e){
			serviceResponse.success = false;
			serviceResponse.message = e.getMessage();
			_logger.error(serviceResponse.message);
		}
		return serviceResponse;
	}
}
