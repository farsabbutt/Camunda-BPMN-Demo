package Listeners;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.bson.Document;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.praxisproject.Camunda_IoT.LoggerDelegate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class TaskListnerEventsLog implements TaskListener {
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	
	
	@Override
	public void notify(DelegateTask delegateTask) {
		LOGGER.info("TaskListnerEventsLog::Executed: "+ delegateTask.getExecution().getCurrentActivityName() );
		Date date = new Date(System.currentTimeMillis());
		
		MongoClient client = MongoClients.create("mongodb://mongodb:27017");
		
		MongoDatabase db = client.getDatabase("camunda_project");
		
		MongoCollection col= db.getCollection("eventLogs");
		
		Document doc = new Document();
		
		doc.put("Activityname", delegateTask.getExecution().getCurrentActivityName());
		doc.put("Businesskey", delegateTask.getExecution().getBusinessKey());
		doc.put("ProductionOrderId",delegateTask.getVariable("productionOrderId"));
		doc.put("Starttime", formatter.format(date));
		
		col.insertOne(doc);
			
	}

}
