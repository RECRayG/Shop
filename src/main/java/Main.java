import DTO.Request.Criterias;
import DTO.Request.SearchRequest.LastNameCriteria;
import DTO.Request.SearchRequest.ProductCriteria;
import DTO.Request.SearchRequest.SearchCriteria;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.*;

public class Main {
//    public static void main(String[] args) {
//        ArrayList<Customer> customers = new ArrayList<>();
//
//        // SQL-запрос для поиска покупателей по критериям
//        String sql = "SELECT * FROM customers WHERE id_customer >= 5";
//        Connection connection = DatabaseConnection.getInstance().getConnection();
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                while (resultSet.next()) {
//                    Long id_customer = resultSet.getLong("id_customer");
//                    String firstName = resultSet.getString("firstName");
//                    String lastName = resultSet.getString("lastName");
//                    customers.add(new Customer(id_customer, firstName, lastName));
//                }
//            }
//        } catch (SQLException e) {
//            System.out.println("Ошибка...");
//        }
//
//        customers.forEach(customer -> {
//            System.out.println(customer.getId_customer() + ": " + customer.getFirstName() + " " + customer.getLastName());
//        });
//    }
    public static void main(String[] args) {
        String json = "{\"criterias\":[" +
                "{\"lastName\":\"Иванов\"}," +
                "{\"lastName\":\"Петров\"}," +
                "{\"productName\":\"Минеральная вода\",\"minTimes\":5}," +
                "{\"minExpenses\":112,\"maxExpenses\":4000}," +
                "{\"badCustomers\":3}" +
                "]}";

        Criterias criterias = new Criterias(json);
    }
}
