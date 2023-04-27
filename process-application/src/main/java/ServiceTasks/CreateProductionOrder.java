package ServiceTasks;

import model.ProductionOrder;
import org.apache.commons.io.FileUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class CreateProductionOrder implements JavaDelegate {


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        ProductionOrder productionOrder = new ProductionOrder();
               productionOrder.setProductName(delegateExecution.getVariable("product").toString());
        productionOrder.setProductToBeManufactured(delegateExecution.getVariable("product").toString());
        productionOrder.setOrderedQuantity((Integer) delegateExecution.getVariable("ordered quantity"));
        productionOrder.setEstimatedProductionTime("5Mins");
        productionOrder.setCustomerEmail(delegateExecution.getVariable("customer email").toString());
        productionOrder.setCustomerName(delegateExecution.getVariable("customer name").toString());
        productionOrder.setProductionOrderId((Integer) delegateExecution.getVariable("productionOrderId"));

        delegateExecution.setVariable("productionOrder", productionOrder);

        //Initializing productionOrder.html with data
        File htmlTemplateFile = new File("src/main/resources/forms/productionOrderApproval.html");
        String htmlString = FileUtils.readFileToString(htmlTemplateFile, StandardCharsets.UTF_8);
        String title = "Production Order Approval";
        String customerName = delegateExecution.getVariable("customer email").toString();
        String customerEmail = delegateExecution.getVariable("customer email").toString();
        String productName = delegateExecution.getVariable("product").toString();
        int orderedQuantity = (Integer) delegateExecution.getVariable("ordered quantity");
        int productionQuantity = 1;
        String estimatedProductionTime = "5 Mins";

        htmlString = htmlString.replace("$customerName", customerName);
        htmlString = htmlString.replace("$customerEmail", customerEmail);
        htmlString = htmlString.replace("$productName", productName);
        htmlString = htmlString.replace("$orderedQuantity", String.valueOf(orderedQuantity));
        htmlString = htmlString.replace("$productionQuantity", String.valueOf(productionQuantity));
        htmlString = htmlString.replace("$estimatedProductionTime", estimatedProductionTime);
        File newHtmlFile = new File("path/new.html");
        FileUtils.writeStringToFile(htmlTemplateFile, htmlString, StandardCharsets.UTF_8);



    }
}
