package MessageEvents;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class OrderProcessingCancelled implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> variables = new HashMap<String, Object>();
	    variables.put("businessKey", execution.getVariable("businessKey"));

	    Long processOrderId = (Long) execution.getVariable("productionOrderId");

	    execution.getProcessEngineServices().getRuntimeService()
	      .createMessageCorrelation("orderProcessingStopped")
	      .processInstanceVariableEquals("productionOrderId",processOrderId)
	      .setVariables(variables)
	      .correlate();

	}

}
