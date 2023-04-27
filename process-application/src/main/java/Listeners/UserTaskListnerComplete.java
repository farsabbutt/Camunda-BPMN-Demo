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


public class UserTaskListnerComplete implements TaskListener {
	
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		Date date = new Date(System.currentTimeMillis());
		
		MongoClient client = MongoClients.create("mongodb://mongodb:27017");
		
		MongoDatabase db = client.getDatabase("camunda_project");
		
		MongoCollection col= db.getCollection("eventLogs");
		
		col.updateOne(Filters.and(Filters.eq("ProductionOrderId", delegateTask.getVariable("productionOrderId")),
				Filters.eq("Activityname", delegateTask.getExecution().getCurrentActivityName())),
				Updates.set("Endtime",formatter.format(date)));
		     
		
		LOGGER.info("Endtime:="+ formatter.format(date));
		
		LOGGER.info("activity:="+delegateTask.getExecution().getCurrentActivityName());
		
		
	}

}
