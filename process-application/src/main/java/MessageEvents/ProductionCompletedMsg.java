package MessageEvents;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.praxisproject.Camunda_IoT.LoggerDelegate;

public class ProductionCompletedMsg implements JavaDelegate {
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		String store= "store";
		Map<String, Object> variables = new HashMap<String, Object>();
	    variables.put("businessKey", execution.getVariable("businessKey"));
		/*
		 * variables.put("item", execution.getVariable("orderedProduct"));
		 * variables.put("shelf_id", execution.getVariable("shelf_id"));
		 * variables.put("place_id", execution.getVariable("place_id"));
		 */
	   
	    
	    Long processOrderId = (Long) execution.getVariable("productionOrderId");
		/* execution.setVariable("tasktype", "store"); */
		/*
		 * processVariables.put("tasktype",execution.getVariable("tasktype"));
		 * processVariables.put("shelf_id",execution.getVariable("shelf_id"));
		 * processVariables.put("place_id",execution.getVariable("place_id"));
		 * processVariables.put("item",execution.getVariable("materialRequired"));
		 * processVariables.put("orderedProduct",execution.getVariable("orderedProduct")
		 * );
		 */

	    execution.getProcessEngineServices().getRuntimeService()
	      .createMessageCorrelation("orderProcessingStopped")
	      .processInstanceVariableEquals("productionOrderId",processOrderId)
	      .setVariables(variables)
	      .correlate();
	
		LOGGER.info("processing completed message"+ variables);

	}

}
