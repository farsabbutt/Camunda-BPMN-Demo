package org.automationlab.api.database;

import java.sql.Connection;
import org.springframework.core.env.Environment;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.automationlab.api.database.interfaces.IMessageRepository;
import org.automationlab.api.database.interfaces.IOrderRepository;
import org.automationlab.api.database.models.EngineMessage;
import org.automationlab.api.database.models.Order;
import org.springframework.beans.factory.DisposableBean;

public class DatabaseContext implements DisposableBean, IOrderRepository, IMessageRepository{
	private Connection connection;
	
	public DatabaseContext(Environment env) throws SQLException, ClassNotFoundException {
		this(env, "rpa_demonstrator");
	}
	
	public DatabaseContext(Environment env, String databaseName) throws SQLException, ClassNotFoundException {		
		Class.forName("com.mysql.cj.jdbc.Driver");
		if(databaseName != "") {
			databaseName = "/" + databaseName;
		}
		connection = DriverManager.getConnection(
				"jdbc:mysql://" + env.getProperty("databaseServerAddress") + ":" + env.getProperty("databaseServerPort") + databaseName,
				env.getProperty("databaseServerUser"),
				env.getProperty("databaseServerPassword"));
	}
	
	public void InitDatabase() throws SQLException {		
		Statement statement = connection.createStatement();
		statement.execute("CREATE DATABASE IF NOT EXISTS rpa_demonstrator;");
		statement.execute("USE rpa_demonstrator;");
		statement.execute(
				"CREATE TABLE IF NOT EXISTS Orders ("
				+ " ProcessId VARCHAR(36) NOT NULL,"
				+ "	BusinessKey VARCHAR(128) NOT NULL,"
				+ "	OrderId INTEGER NOT NULL,"
				+ "	Status TEXT NOT NULL,"
				+ "	OrderDate DATE NOT NULL,"
				+ "	LatestDeliveryDate DATE NOT NULL,"
				+ "	ExpectedTruckArrivalDate DATE NULL,"
				+ "	ExpectedDeliveryDate DATE NULL,"
				+ "	TruckArrivalDate DATE DEFAULT NULL,"
				+ "	DeliveryDate DATE DEFAULT NULL,"
				+ "	UNIQUE INDEX (ProcessId),"
				+ "	UNIQUE INDEX (OrderId)"
				+ ");");
		statement.execute(
				"CREATE TABLE IF NOT EXISTS EngineMessages ("
				+ "	MessageGuid VARCHAR(36) NOT NULL,"
				+ "	MessageTime BIGINT NOT NULL,"
				+ "	Pending BOOLEAN NOT NULL,"
				+ "	Content TEXT NOT NULL,"
				+ "	UNIQUE INDEX (MessageGuid)"
				+ ");");
		statement.close();
	}
	
	@Override
    public void destroy() throws SQLException {	
		if (connection != null) {
			connection.close();
        }
	}
	
	public void addOrder(Order order) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO Orders (ProcessId, BusinessKey, OrderId, Status, OrderDate, LatestDeliveryDate, ExpectedTruckArrivalDate, ExpectedDeliveryDate, TruckArrivalDate, DeliveryDate)"
				+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ");
		statement.setString(1, order.processId);
		statement.setString(2, order.businessKey);
		statement.setInt(3, order.orderId);
		statement.setString(4, order.status);
		statement.setDate(5, Date.valueOf(order.orderDate));
		statement.setDate(6, Date.valueOf(order.latestDeliveryDate));
		statement.setDate(7, order.expectedTruckArrivalDate != null ? Date.valueOf(order.expectedTruckArrivalDate) : null);
		statement.setDate(8, order.expectedDeliveryDate != null ? Date.valueOf(order.expectedDeliveryDate) : null);
		statement.setDate(9, order.truckArrivalDate != null ? Date.valueOf(order.truckArrivalDate) : null);
		statement.setDate(10, order.deliveryDate != null ? Date.valueOf(order.deliveryDate) : null);
		statement.executeUpdate();
		statement.close();
	}
	
	public void updateOrder(Order order) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
				"UPDATE Orders"
				+ " SET Status = ?,"
				+ "	OrderDate = ?,"
				+ "	LatestDeliveryDate = ?,"
				+ " ExpectedTruckArrivalDate = ?,"
				+ " ExpectedDeliveryDate = ?,"
				+ " TruckArrivalDate = ?,"
				+ " DeliveryDate = ?"
				+ " WHERE ProcessId = ? AND OrderId = ? ; ");
		statement.setString(1, order.status);
		statement.setDate(2, Date.valueOf(order.orderDate));
		statement.setDate(3, Date.valueOf(order.latestDeliveryDate));
		statement.setDate(4, order.expectedTruckArrivalDate != null ? Date.valueOf(order.expectedTruckArrivalDate) : null);
		statement.setDate(5, order.expectedDeliveryDate != null ? Date.valueOf(order.expectedDeliveryDate) : null);
		statement.setDate(6, order.truckArrivalDate != null ? Date.valueOf(order.truckArrivalDate) : null);
		statement.setDate(7, order.deliveryDate != null ? Date.valueOf(order.deliveryDate) : null);
		statement.setString(8, order.processId);
		statement.setInt(9, order.orderId);
		statement.executeUpdate();
		statement.close();
	}
	
	public void updateOrderByProcessId(Order order) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
				"UPDATE Orders"
				+ " SET Status = ?,"
				+ "	OrderDate = ?,"
				+ "	LatestDeliveryDate = ?,"
				+ " ExpectedTruckArrivalDate = ?,"
				+ " ExpectedDeliveryDate = ?,"
				+ " TruckArrivalDate = ?,"
				+ " DeliveryDate = ?,"
				+ "	OrderId = ?"
				+ " WHERE ProcessId = ? ; ");
		statement.setString(1, order.status);
		statement.setDate(2, Date.valueOf(order.orderDate));
		statement.setDate(3, Date.valueOf(order.latestDeliveryDate));
		statement.setDate(4, order.expectedTruckArrivalDate != null ? Date.valueOf(order.expectedTruckArrivalDate) : null);
		statement.setDate(5, order.expectedDeliveryDate != null ? Date.valueOf(order.expectedDeliveryDate) : null);
		statement.setDate(6, order.truckArrivalDate != null ? Date.valueOf(order.truckArrivalDate) : null);
		statement.setDate(7, order.deliveryDate != null ? Date.valueOf(order.deliveryDate) : null);
		statement.setInt(8, order.orderId);
		statement.setString(9, order.processId);
		statement.executeUpdate();
		statement.close();
	}
	
	public Order getOrder(String processId, int orderId) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
				"SELECT ProcessId, BusinessKey, OrderId, Status, OrderDate, LatestDeliveryDate, ExpectedTruckArrivalDate, ExpectedDeliveryDate, TruckArrivalDate, DeliveryDate FROM Orders"
				+ " WHERE ProcessId = ? AND OrderId = ? ; ");
		statement.setString(1, processId);
		statement.setInt(2, orderId);
		ResultSet resultSet = statement.executeQuery();
		
		Order[] orders = readOrders(resultSet, 1);
		
		Order order = null;	
		if(orders.length == 1) {
			order = orders[0];
		}
		
		statement.close();
		resultSet.close();
		return order;
	}
	
	public Order getOrderById(int orderId) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
				"SELECT ProcessId, BusinessKey, OrderId, Status, OrderDate, LatestDeliveryDate, ExpectedTruckArrivalDate, ExpectedDeliveryDate, TruckArrivalDate, DeliveryDate FROM Orders"
				+ " WHERE OrderId = ? ; ");
		statement.setInt(1, orderId);
		ResultSet resultSet = statement.executeQuery();
		
		Order[] orders = readOrders(resultSet, 1);
		
		Order order = null;	
		if(orders.length == 1) {
			order = orders[0];
		}
		
		statement.close();
		resultSet.close();
		return order;
	}
	
	public Order getOrderByProcessId(String processId) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
				"SELECT ProcessId, BusinessKey, OrderId, Status, OrderDate, LatestDeliveryDate, ExpectedTruckArrivalDate, ExpectedDeliveryDate, TruckArrivalDate, DeliveryDate FROM Orders"
				+ " WHERE ProcessId = ? ; ");
		statement.setString(1, processId);
		ResultSet resultSet = statement.executeQuery();
		
		Order[] orders = readOrders(resultSet, 1);
		
		Order order = null;	
		if(orders.length == 1) {
			order = orders[0];
		}
		
		statement.close();
		resultSet.close();
		return order;
	}
	
	public Order[] getOrdersForStatusCheck() throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
				"SELECT ProcessId, BusinessKey, OrderId, Status, OrderDate, LatestDeliveryDate, ExpectedTruckArrivalDate, ExpectedDeliveryDate, TruckArrivalDate, DeliveryDate FROM Orders"
				+ " WHERE Status <> ? AND Status <> ? ; ");
		statement.setString(1, Order.StatusDelivered);
		statement.setString(2, Order.StatusDeclined);
		ResultSet resultSet = statement.executeQuery();
		
		Order[] orders = readOrders(resultSet, Integer.MAX_VALUE);
		
		statement.close();
		resultSet.close();
		return orders;
	}
	
	private Order[] readOrders(ResultSet resultSet, int maxCount) throws SQLException {
		ArrayList<Order> orders = new ArrayList<Order>();
		int i = 0;
		while(resultSet.next() && i < maxCount) {
			Order order = new Order();
			order.processId = resultSet.getString("ProcessId");
			order.businessKey = resultSet.getString("BusinessKey");
			order.orderId = resultSet.getInt("OrderId");
			order.status = resultSet.getString("Status");
			
			Date date = resultSet.getDate("OrderDate");
			if(date != null) {
				order.orderDate = date.toLocalDate();
			}
			
			date = resultSet.getDate("LatestDeliveryDate");
			if(date != null) {
				order.latestDeliveryDate = date.toLocalDate();
			}
			
			date = resultSet.getDate("ExpectedTruckArrivalDate");
			if(date != null) {
				order.expectedTruckArrivalDate = date.toLocalDate();
			}
			
			date = resultSet.getDate("ExpectedDeliveryDate");
			if(date != null) {
				order.expectedDeliveryDate = date.toLocalDate();
			}
			
			date = resultSet.getDate("TruckArrivalDate");
			if(date != null) {
				order.truckArrivalDate = date.toLocalDate();
			}
			
			date = resultSet.getDate("DeliveryDate");
			if(date != null) {
				order.deliveryDate = date.toLocalDate();
			}
			orders.add(order);
			i++;
		}
		Order[] ordersArray = new Order[orders.size()];
		return orders.toArray(ordersArray);
	}
	
	public void addMessage(EngineMessage message) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO EngineMessages (MessageGuid, MessageTime, Pending, Content)"
				+ " values(?, ?, ?, ?); ");
		statement.setString(1, message.messageGuid);
		statement.setLong(2, message.messageTime);
		statement.setBoolean(3, message.pending);
		statement.setString(4, message.content);
		statement.executeUpdate();
		statement.close();
	}
	
	public void updateMessage(EngineMessage message) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
				"UPDATE EngineMessages"
				+ " SET Pending = ?"
				+ " WHERE MessageGuid = ? ; ");
		statement.setBoolean(1, message.pending);
		statement.setString(2, message.messageGuid);
		statement.executeUpdate();
		statement.close();
	}
	
	public ArrayList<EngineMessage> getPendingMessages() throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
				"SELECT MessageGuid, MessageTime, Pending, Content FROM EngineMessages"
				+ " WHERE Pending = ? ; ");
		statement.setBoolean(1, true);
		
		ResultSet resultSet = statement.executeQuery();
		ArrayList<EngineMessage> messages = new ArrayList<EngineMessage>();
		while(resultSet.next()) {
			EngineMessage message = new EngineMessage();
			message.messageGuid = resultSet.getString("MessageGuid");
			message.messageTime = resultSet.getLong("MessageTime");
			message.pending = resultSet.getBoolean("Pending");
			message.content = resultSet.getString("Content");
			messages.add(message);
		}
		
		statement.close();
		resultSet.close();
		return messages;
	}
}