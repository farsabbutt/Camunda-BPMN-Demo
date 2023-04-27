package ServiceTasks;


import model.CustomerOrder;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spinjar.com.fasterxml.jackson.core.JsonProcessingException;
import spinjar.com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StoreCustomerOrder implements JavaDelegate {
    private final String driverClassName = "com.mysql.cj.jdbc.Driver";
    private final String dbUserName = "dev";
    private final String dbPassword = "root";
    private final String dbUrl = "jdbc:mysql://localhost:3306/customer_DB?useSSL=false";

    private Logger log = LoggerFactory.getLogger(StoreCustomerOrder.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws JsonProcessingException, SQLException {
        /**  log.info(String.format("PROCESSING STORE CUSTOMER ORDER... " +
         "|| ActivityInstanceId: %s", delegateExecution.getActivityInstanceId()));  */
        Object jsonCustomerOrder = delegateExecution.getVariable("customerOrder");
        ObjectMapper objectMapper = new ObjectMapper();
        //CustomerOrder customerOrder = objectMapper.readValue(jsonCustomerOrder.toString(), CustomerOrder.class);
        CustomerOrder customerOrder = CustomerOrder.builder()
                .customerName(delegateExecution.getVariable("customerName").toString())
                .orderStatus("OPEN")
                .address(delegateExecution.getVariable("address").toString())
                .Email(delegateExecution.getVariable("Email").toString())
                .phonenumber(delegateExecution.getVariable("Phone").toString())
                .quantity(delegateExecution.getVariable("quantity").toString())
                .build();
        log.info("BICYCLE FROM THE FRONTEND APP _____________________________________________________________________________________________________________   " + customerOrder.toString());
        String query = "INSERT INTO customerOrder (customerName, email, phonenumber, product, address, quantity)"
                + "VALUES(?,?,?,?,?,?)";
        try {
            Class.forName(driverClassName);
            Connection connection = DriverManager
                    .getConnection(dbUrl, dbUserName, dbPassword);
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, customerOrder.getCustomerName());
            ps.setString(2, customerOrder.getEmail());
            ps.setString(3, customerOrder.getPhonenumber());
            ps.setString(4, customerOrder.getProduct());
            ps.setString(5, customerOrder.getAddress());
            ps.setString(6, customerOrder.getQuantity());
            ps.execute();
            connection.close();

        } catch (Exception e) {
            //log.debug(e.getMessage());
        }
    }
}

