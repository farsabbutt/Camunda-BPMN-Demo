package org.automationlab.api.controllers;

import org.automationlab.api.database.models.Order;
import org.automationlab.api.services.IOrderCheckService;
import org.automationlab.api.services.IOrderService;
import org.automationlab.api.services.ServiceResponse;
import org.automationlab.api.services.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrdersController {
	
	private final IOrderService _orderService;
	
	private final IOrderCheckService _orderCheckService;
	
	@Autowired
	public OrdersController(IOrderService orderService, IOrderCheckService orderCheckService) {
		_orderService = orderService;
		_orderCheckService = orderCheckService;
	}
	
	@PostMapping(
			value = "/orders/create",
			consumes = MediaType.APPLICATION_JSON_VALUE,
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ServiceResponse<Order>> createOrder(@RequestBody CreateOrderDto model) {
		ServiceResponse<Order> response = _orderService.createOrder(model);
		if(response.success) {
			return new ResponseEntity<ServiceResponse<Order>>(response, HttpStatus.OK);
		}
		return new ResponseEntity<ServiceResponse<Order>>(response, HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping(
			value = "/orders/create/error",
			consumes = MediaType.APPLICATION_JSON_VALUE,
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ServiceResponse<Order>> createOrderError(@RequestBody CreateOrderErrorDto model) {
		ServiceResponse<Order> response = _orderService.createOrderError(model);
		if(response.success) {
			return new ResponseEntity<ServiceResponse<Order>>(response, HttpStatus.OK);
		}
		return new ResponseEntity<ServiceResponse<Order>>(response, HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping(
			value = "/orders/approve",
			consumes = MediaType.APPLICATION_JSON_VALUE,
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ServiceResponse<Order>> approveOrder(@RequestBody ApproveOrderDto model) {
		ServiceResponse<Order> response = _orderService.approveOrder(model);
		if(response.success) {
			return new ResponseEntity<ServiceResponse<Order>>(response, HttpStatus.OK);
		}
		return new ResponseEntity<ServiceResponse<Order>>(response, HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping(
			value = "/orders/decline",
			consumes = MediaType.APPLICATION_JSON_VALUE,
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ServiceResponse<Order>> approveOrder(@RequestBody DeclineOrderDto model) {
		ServiceResponse<Order> response = _orderService.declineOrder(model);
		if(response.success) {
			return new ResponseEntity<ServiceResponse<Order>>(response, HttpStatus.OK);
		}
		return new ResponseEntity<ServiceResponse<Order>>(response, HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping(
			value = "/orders/pickup",
			consumes = MediaType.APPLICATION_JSON_VALUE,
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ServiceResponse<Order>> pickUpOrder(@RequestBody PickUpOrderDto model) {
		ServiceResponse<Order> response = _orderService.pickUpOrder(model);
		if(response.success) {
			return new ResponseEntity<ServiceResponse<Order>>(response, HttpStatus.OK);
		}
		return new ResponseEntity<ServiceResponse<Order>>(response, HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping(
			value = "/orders/deliver",
			consumes = MediaType.APPLICATION_JSON_VALUE,
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ServiceResponse<Order>> deliverOrder(@RequestBody DeliverOrderDto model) {
		ServiceResponse<Order> response = _orderService.deliverOrder(model);
		if(response.success) {
			return new ResponseEntity<ServiceResponse<Order>>(response, HttpStatus.OK);
		}
		return new ResponseEntity<ServiceResponse<Order>>(response, HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping(
			value = "/orders/check",
			consumes = MediaType.APPLICATION_JSON_VALUE,
	        produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ServiceResponse<Object>> checkOrders() {
		ServiceResponse<Object> response = _orderCheckService.checkOrdersStatus();
		if(response.success) {
			return new ResponseEntity<ServiceResponse<Object>>(response, HttpStatus.OK);
		}
		return new ResponseEntity<ServiceResponse<Object>>(response, HttpStatus.BAD_REQUEST);
	}
}
