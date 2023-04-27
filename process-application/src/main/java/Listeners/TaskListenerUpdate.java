package Listeners;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.praxisproject.Camunda_IoT.LoggerDelegate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class TaskListenerUpdate implements TaskListener {
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		Date date = new Date(System.currentTimeMillis());
		int productionOrderId = (int) delegateTask.getExecution().getVariable("productionOrderId");	
		
		MongoClient client = MongoClients.create("mongodb://mongodb:27017");
		
		MongoDatabase db = client.getDatabase("camunda_project");
		
		MongoCollection col= db.getCollection("eventLogs");

		col.updateOne(Filters.eq("Businesskey", delegateTask.getExecution().getBusinessKey()), Updates.set("ProductionOrderId",productionOrderId ));
		
		col.updateOne(Filters.eq("Businesskey", delegateTask.getExecution().getBusinessKey()), Updates.set("Endtime", formatter.format(date)));
		
		LOGGER.info("Endtime:"+ formatter.format(date));
		LOGGER.info("ProductionOrderId:"+ productionOrderId);

	}

}
