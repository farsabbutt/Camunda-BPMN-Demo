package org.automationlab.api.services;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import org.automationlab.api.database.DatabaseContext;
import org.automationlab.api.database.interfaces.IOrderRepository;
import org.automationlab.api.database.models.Order;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

public class OrderCheckService extends TimerTask implements IOrderCheckService {

    private final Logger _logger = LoggerFactory.getLogger(OrderCheckService.class);
	
	private final IUIPathManager _uiPathManager;
    
    private final Timer timer;
	
	private final long timeout;
	
	private long lastRun;
	
	private final Environment env;
	
	public OrderCheckService(IUIPathManager uiPathManager, Environment env) {
		_uiPathManager = uiPathManager;
		this.timeout = Integer.parseInt(env.getProperty("orderCheckServiceTimeout") != null ? env.getProperty("orderCheckServiceTimeout") : "60000");
		this.env = env;
		lastRun = System.currentTimeMillis();
		
		timer = new Timer();
		timer.schedule(this, 10000, 1000);
		_logger.info("Order check service started");
	}
	
	@Override
	public void run() {	
		if(System.currentTimeMillis() - lastRun > timeout) {
			checkOrdersStatus();
		}
	}
	
	public ServiceResponse<Object> checkOrdersStatus(){
		ServiceResponse<Object> serviceResponse = new ServiceResponse<Object>();
		IOrderRepository orderRepository  = null;
		lastRun = System.currentTimeMillis();
		try {			
			orderRepository = new DatabaseContext(env);
			Order[] orders = orderRepository.getOrdersForStatusCheck();
			if(orders.length == 0) {
				return serviceResponse;
			}
			String ordersString = "";
			for(int i = 0; i < orders.length; i++) {
				ordersString += orders[i].orderId + "=" + orders[i].status;
				if(i < orders.length - 1) {
					ordersString += ";";
				}
			}
			JSONObject arguments = new JSONObject();
			if(ordersString.length() > 0) {
				arguments.put("Orders", ordersString);
			}
			ServiceResponse<JSONObject> response = _uiPathManager.startProcess("CheckOrdersProcess", new String[0], arguments);
			if(response.success == false) {
				throw new Exception(response.message);
			}
		}
		catch(Exception e) {
			serviceResponse.success = false;
			serviceResponse.message = e.getMessage();
			_logger.error(serviceResponse.message);
		}
		finally{
			try {
				orderRepository.destroy();
			} catch (SQLException e) {
				_logger.error(e.getMessage());
			}
		}
		return serviceResponse;
	}
}
