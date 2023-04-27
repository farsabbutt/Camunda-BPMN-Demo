package ServiceTasks;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.Document;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import Listeners.ExecutionTasklistnerStart;

public class SaveOrder implements JavaDelegate {

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Date date = new Date(System.currentTimeMillis());
				//create connection to Mongodb localhost
		MongoClient client = MongoClients.create("mongodb://localhost:27017");
		MongoDatabase db = client.getDatabase("camunda_project");
				// execution listener executed
		MongoCollection col= db.getCollection("eventLogs");
				
		Document doc = new Document();
		doc.put("Activityname", execution.getCurrentActivityName());
		doc.put("Businesskey", execution.getBusinessKey());
		doc.put("ProductionOrderId",execution.getVariable("productionOrderId"));
		doc.put("Starttime", formatter.format(date));
		doc.put("Endtime", formatter.format(date));
		col.insertOne(doc);
		//  service Task Save production Order in DB
		MongoCollection orderCol = db.getCollection("productionorders");
		
		Document order = new Document();
		order.put("approved", execution.getVariable("approve"));
		order.put("ProductionOrderId", execution.getVariable("productionOrderId"));
		order.put("orderQuantity",execution.getVariable("orderQuantity"));
		order.put("materialRequired",  execution.getVariable("materialRequired"));
		order.put("materialQuantity", execution.getVariable("materialQuantity"));
		order.put("productionDuration", execution.getVariable("productionDuration"));
		orderCol.insertOne(order);

		// End Task Execution listener
		col.updateOne(Filters.and(Filters.eq("Businesskey", execution.getBusinessKey()),
				Filters.eq("Activityname", execution.getCurrentActivityName())),
				Updates.set("Endtime",formatter.format(date)));
		     

	}

}
