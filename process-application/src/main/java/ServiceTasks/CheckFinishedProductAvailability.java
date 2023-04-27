package ServiceTasks;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;

import java.sql.*;
import java.util.Random;

public class CheckFinishedProductAvailability implements JavaDelegate {
    private Connection connection = null;
    private final Statement statement = null;
    private final PreparedStatement preparedStatement = null;
    private final ResultSet resultSet = null;
    private Boolean isAvailable = false;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        try {
            // below two lines are used for connectivity.
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://mysql:3306/customer_DB",
                    "root", "root");

            Statement statement;
            statement = connection.createStatement();
            ResultSet resultSet;
            resultSet = statement.executeQuery(
                    "select count(*) from customer_DB.finished_products");
            Random rand = new Random();
            int int_random = rand.nextInt(2);
            isAvailable = int_random == 1;
            if (int_random == 1){
                isAvailable = true;
            }else {
                isAvailable = false;
            }
            delegateExecution.setVariable("isAvailable", isAvailable);

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }
}
