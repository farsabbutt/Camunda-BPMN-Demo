package MessageEvents;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.praxisproject.Camunda_IoT.LoggerDelegate;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ReleaseCustomerOrder implements JavaDelegate {
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        Map<String, Object> processVariables = new HashMap<String, Object>();
        processVariables.put("businessKey", execution.getBusinessKey());
        processVariables.put("productionOrderId",execution.getVariable("orderId"));
        processVariables.put("customer name",execution.getVariable("name"));
        processVariables.put("customer email",execution.getVariable("email"));
        processVariables.put("phonenumber",execution.getVariable("phoneNumber"));
        processVariables.put("customer address",execution.getVariable("address"));
        processVariables.put("product",execution.getVariable("product"));
        processVariables.put("ordered quantity",execution.getVariable("quantity"));

        runtimeService.startProcessInstanceByMessage("startTheOrderProcessing", processVariables);

        LOGGER.info("RELEASING CUSTOMER ORDER.........................................");
        LOGGER.info(processVariables.get("customer name").toString());
        LOGGER.info(processVariables.get("customer email").toString());
        LOGGER.info(processVariables.get("phonenumber").toString());
        LOGGER.info(processVariables.get("customer address").toString());
        LOGGER.info(processVariables.get("product").toString());
        LOGGER.info(processVariables.get("ordered quantity").toString());


        LOGGER.info("message release customer order "+ execution.getBusinessKey() );
    }
}
