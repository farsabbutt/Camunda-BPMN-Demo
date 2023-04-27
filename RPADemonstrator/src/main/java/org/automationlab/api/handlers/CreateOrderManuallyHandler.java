package org.automationlab.api.handlers;

import java.sql.SQLException;
import java.time.LocalDate;

import org.automationlab.api.database.DatabaseContext;
import org.automationlab.api.database.interfaces.IOrderRepository;
import org.automationlab.api.database.models.Order;
import org.automationlab.api.services.ICamundaMessageService;
import org.automationlab.api.services.IOrderService;
import org.automationlab.api.services.OrderService;
import org.automationlab.api.services.ServiceResponse;
import org.automationlab.api.services.dto.CreateOrderManuallyDto;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription("createOrderManually")
public class CreateOrderManuallyHandler implements ExternalTaskHandler {
	
	private final Logger _logger = LoggerFactory.getLogger(CreateOrderManuallyHandler.class);
	
	@Autowired
	public Environment env;
	
	@Autowired
	public ICamundaMessageService camundaMessageService;

	@Override
	public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {		
		IOrderRepository orderRepository = null;
		try {			
			orderRepository = new DatabaseContext(env);		
			IOrderService orderService = new OrderService(orderRepository, camundaMessageService);
					
			CreateOrderManuallyDto model = new CreateOrderManuallyDto();
			model.processId = externalTask.getProcessInstanceId();
			model.businessKey = externalTask.getBusinessKey();
			model.orderId = Integer.parseInt((String)externalTask.getVariableTyped("newOrderId").getValue());
			model.latestDeliveryDate = LocalDate.parse((String)externalTask.getVariableTyped("latestDeliveryDate").getValue());
			
			ServiceResponse<Order> serviceResponse = orderService.createOrderManually(model);
						
			if(serviceResponse.success == false) {
				externalTaskService.handleFailure(externalTask, serviceResponse.message, null, 0, 0);
			}else {			
				externalTaskService.complete(externalTask); 
			}
		}catch(Exception e) {
			_logger.error(e.getMessage());
			externalTaskService.handleFailure(externalTask, e.getMessage(), null, 0, 0);
		}finally {
			try {
				orderRepository.destroy();
			} catch (SQLException e) {
				_logger.error(e.getMessage());
			}
		}
	}
}
