package MessageEvents;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.praxisproject.Camunda_IoT.LoggerDelegate;

public class SendProductionOrder implements JavaDelegate {
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
		
		
		Map<String, Object> processVariables = new HashMap<String, Object>();
		
		
		processVariables.put("businessKey", execution.getBusinessKey());
		processVariables.put("productionOrderId",execution.getVariable("orderId"));
		/*
		 * processVariables.put("tasktype",execution.getVariable("tasktype"));
		 * processVariables.put("shelf_id",execution.getVariable("shelf_id"));
		 * processVariables.put("place_id",execution.getVariable("place_id"));
		 * processVariables.put("item",execution.getVariable("materialRequired"));
		 * processVariables.put("orderedProduct",execution.getVariable("orderedProduct")
		 * );
		 */
		runtimeService.startProcessInstanceByMessage("startTheOrderProcessing", processVariables);
 
		
		    
	    LOGGER.info("message send Production order "+ execution.getBusinessKey() );
		
		    
	 

	}

}
