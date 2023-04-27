package MessageEvents;


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.praxisproject.Camunda_IoT.LoggerDelegate;


public class processingDone implements JavaDelegate {
	
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
	
		execution.getProcessEngineServices().getRuntimeService().correlateMessage("processCompleted", execution.getVariables());
		
	
		LOGGER.info("processing done message#" );

	}

}
