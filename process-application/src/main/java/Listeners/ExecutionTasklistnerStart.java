package Listeners;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.Document;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ExecutionTasklistnerStart implements ExecutionListener {

	//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		Date date = new Date(System.currentTimeMillis());
		
		MongoClient client = MongoClients.create("mongodb://mongodb:27017");
		
		MongoDatabase db = client.getDatabase("camunda_project");
		
		MongoCollection col= db.getCollection("eventLogs");
		
		Document doc = new Document();
		
		doc.put("Activityname", execution.getCurrentActivityName());
		doc.put("Businesskey", execution.getVariable("businessKey"));
		doc.put("ProductionOrderId",execution.getVariable("productionOrderId"));
		doc.put("Starttime", formatter.format(date));
		doc.put("Endtime", formatter.format(date));
		col.insertOne(doc);

	}

}
