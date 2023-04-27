package org.automationlab.api.database.interfaces;

import java.sql.SQLException;

import org.automationlab.api.database.models.Order;

public interface IOrderRepository {
	
	public Order getOrderById(int orderId) throws SQLException;
	
	public Order getOrderByProcessId(String processId) throws SQLException;
	
	public Order getOrder(String processId, int orderId) throws SQLException;
	
	public Order[] getOrdersForStatusCheck() throws SQLException;
	
	public void addOrder(Order order)  throws SQLException;
	
	public void updateOrder(Order order)  throws SQLException;
	
	public void updateOrderByProcessId(Order order)  throws SQLException;
		
	public void destroy() throws SQLException;
}
