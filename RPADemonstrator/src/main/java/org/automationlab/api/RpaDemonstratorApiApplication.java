package org.automationlab.api;

import org.automationlab.api.services.*;
import org.json.JSONObject;

import java.sql.SQLException;

import org.automationlab.api.database.DatabaseContext;
import org.automationlab.api.database.interfaces.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.RequestScope;

@SpringBootApplication
public class RpaDemonstratorApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(RpaDemonstratorApiApplication.class, args);  
		
	}
	
	@Bean
	@ApplicationScope
	public IOrderCheckService getOrderCheckService(IUIPathManager uiPathManager, Environment env){
		return new OrderCheckService(uiPathManager, env);
	}
	
	@Bean
	@ApplicationScope
	public ICamundaMessageService getCamundaMessageService(Environment env){
		return new CamundaMessageService(env);
	}
	
	
	@Bean
	@ApplicationScope
	public IUIPathManager getUIPathManager(Environment environment){
		
		JSONObject defaultArguments = new JSONObject();
		defaultArguments.put("APIAddress", environment.getProperty("apiAddress"));
		defaultArguments.put("APIKey", environment.getProperty("apiKey"));
		defaultArguments.put("WebSiteAddress", environment.getProperty("siteAddress"));
		defaultArguments.put("Login", environment.getProperty("siteLogin"));
		defaultArguments.put("Password", environment.getProperty("sitePassword"));
		
		return new UIPathManager(
				environment.getProperty("uiPathBaseUrl"),
				environment.getProperty("uiPathOrganization"),
				environment.getProperty("uiPathTenant"),
				environment.getProperty("uiPathClient"),
				environment.getProperty("uiPathSecret"),
				environment.getProperty("uiPathScope"),				
				environment.getProperty("uiPathClientId"),
				defaultArguments);
	}
	
	@Bean
	@RequestScope
	public DatabaseContext getDatabaseContext(Environment env) throws ClassNotFoundException, SQLException{
		return new DatabaseContext(env);
	}
		
	@Bean
	@RequestScope
	public IOrderService getOrderService(IOrderRepository orderRepository, ICamundaMessageService camundaMessageService){
		return new OrderService(orderRepository, camundaMessageService);
	}
}
