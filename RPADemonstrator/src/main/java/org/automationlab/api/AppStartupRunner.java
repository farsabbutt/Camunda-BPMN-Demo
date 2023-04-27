package org.automationlab.api;

import org.automationlab.api.database.DatabaseContext;
import org.automationlab.api.services.ICamundaMessageService;
import org.automationlab.api.services.IOrderCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {
	
	private final Logger _logger = LoggerFactory.getLogger(AppStartupRunner.class);
	
	private final Environment env;
	
	@Autowired 
	public AppStartupRunner(ICamundaMessageService camundaMessageService, IOrderCheckService orderCheckService, Environment env) {
		camundaMessageService.toString();
		orderCheckService.toString();
		this.env = env;
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
			DatabaseContext context = new DatabaseContext(env, "");
			context.InitDatabase();
			context.destroy();
		} catch (Exception e) {
			_logger.info(e.getMessage());
		}
		_logger.info("Application started");
	}
}
