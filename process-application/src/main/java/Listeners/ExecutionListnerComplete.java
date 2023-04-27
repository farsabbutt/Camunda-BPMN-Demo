package Listeners;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import camundajar.impl.scala.collection.MapView.Filter;

public class ExecutionListnerComplete implements ExecutionListener {

	//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
			Date date = new Date(System.currentTimeMillis());
				
			MongoClient client = MongoClients.create("mongodb://mongodb:27017");
			
			MongoDatabase db = client.getDatabase("camunda_project");
			
			MongoCollection col= db.getCollection("eventLogs");
		
			col.updateOne(Filters.and(Filters.eq("Businesskey", execution.getBusinessKey()),
					Filters.eq("Activityname", execution.getCurrentActivityName())),
					Updates.set("Endtime",formatter.format(date)));

			

	}

}
