package MessageEvents;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.praxisproject.Camunda_IoT.LoggerDelegate;

public class StartIoTDevice implements JavaDelegate {
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
		
		// fill the reply message with this instance process variables
		Map<String, Object> processVariables = new HashMap<String, Object>();
		
		processVariables = execution.getVariables();
			
		processVariables.put("businessKey", execution.getVariable("businessKey"));
		processVariables.put("productionOrderId",execution.getVariable("productionOrderId"));
		
		runtimeService.startProcessInstanceByMessage("startIoTDevice", processVariables);
 
		
		    
	    LOGGER.info("startIot message sent:"+ execution.getBusinessKey() );
	}

}
