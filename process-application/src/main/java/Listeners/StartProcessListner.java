package Listeners;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.praxisproject.Camunda_IoT.LoggerDelegate;

import java.util.Date;
import java.util.logging.Logger;

public class StartProcessListner implements ExecutionListener {
	String PROCESS_TYPE = "PROCESS_STARTED";
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		Date date = new Date(System.currentTimeMillis());

		//create connection to Mongodb localhost
		MongoClient client = MongoClients.create("mongodb://mongodb:27017");

		MongoDatabase db = client.getDatabase("camunda_project");

		MongoCollection col= db.getCollection("eventLogs");

		Document doc = new Document();

		String activityName = execution.getCurrentActivityName();
		doc.put("Activityname", activityName);
		doc.put("dateTime", date.toString());
		doc.put("processType", PROCESS_TYPE);
		col.insertOne(doc);
		LOGGER.info("Process Execution Listener executed successfully for activity: " + activityName );
	}

}
