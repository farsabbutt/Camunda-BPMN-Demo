package org.automationlab.api.database.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

import org.automationlab.api.database.models.EngineMessage;

public interface IMessageRepository {
	
	public void addMessage(EngineMessage message) throws SQLException;
	
	public void updateMessage(EngineMessage message) throws SQLException;
	
	public ArrayList<EngineMessage> getPendingMessages() throws SQLException;
	
	public void destroy() throws SQLException;
}
