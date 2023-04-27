package Listeners;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.praxisproject.Camunda_IoT.LoggerDelegate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class OrderProcessed implements JavaDelegate {
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	
	//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Date date = new Date(System.currentTimeMillis());
		String activity= "view production statistics";
		//create connection to Mongodb localhost
		MongoClient client = MongoClients.create("mongodb://mongodb:27017");
		
		MongoDatabase db = client.getDatabase("camunda_project");
		
		MongoCollection col= db.getCollection("eventLogs");

		col.updateOne(Filters.and(Filters.eq("ProductionOrderId", execution.getVariable("productionOrderId")),
				Filters.eq("Activityname",activity)),
				Updates.set("Endtime",formatter.format(date)));
			
		 LOGGER.info("message send complete businessKey:"+ execution.getBusinessKey() );
	}

}
