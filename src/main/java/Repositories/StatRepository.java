package Repositories;

import DBConnection.DatabaseConnection;
import DTO.Request.StatRequest.Stats;
import DTO.Response.StatResponse.CustomerResponse;
import DTO.Response.StatResponse.StatResponse;
import Entities.Product;
import Exceptions.MyExitException;
import Exceptions.MySQLException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.math.BigDecimal;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class StatRepository {
    private String response;
    private Stats interval;
    private LocalDate date1;
    private LocalDate date2;

    private BigDecimal totalCustomersExpenses;
    private Double avgCustomersExpenses;

    public StatRepository() {
        interval = new Stats();
    }

    public String statByPeriod(String json) throws MySQLException {
        try {
            StatResponse statResponse = new StatResponse();
            statResponse.setType(OperationType.stat.name());
            readJson(json);
            statResponse.setTotalDays(calculateWorkDay());

            DatabaseConnection.getInstance().initializeConnection();

            List<CustomerResponse> customerResponseList = getStatFromDB();
            totalCustomersExpenses = getTotalCustomersExpenses();
            avgCustomersExpenses = getAvgCustomersExpenses();

            DatabaseConnection.getInstance().closeConnection();

            statResponse.setCustomers(customerResponseList);
            statResponse.setTotalExpenses(totalCustomersExpenses);
            statResponse.setAvgExpenses(avgCustomersExpenses);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            try {
                // Преобразуем объект SearchResponse в JSON строку
                response = objectMapper.writeValueAsString(statResponse);
            } catch (Exception e) {
                throw new MySQLException(e.getMessage());
            }
        }
        catch (MySQLException e) {
            throw new MySQLException(e.getMessage());
        }

        return response;
    }

    private List<CustomerResponse> getStatFromDB() throws MySQLException {
        List<CustomerResponse> customerResponseList = new ArrayList<>();
        String sql = "SELECT DISTINCT cus.id_customer, CONCAT(cus.lastName, ' ', cus.firstName) AS name, cus_total.total_customer_expenses\n" +
                    "FROM purchases pur\n" +
                    "INNER JOIN customers cus ON cus.id_customer = pur.id_customer\n" +
                    "INNER JOIN products pr ON pr.id_product = pur.id_product\n" +
                    "INNER JOIN (\n" +
                    "    SELECT pur.id_customer, SUM(pr.expenses) AS total_customer_expenses\n" +
                    "    FROM purchases pur\n" +
                    "    INNER JOIN products pr ON pr.id_product = pur.id_product\n" +
                    "    WHERE pur.datepurchase BETWEEN ? AND ?\n" +
                    "    GROUP BY pur.id_customer) cus_total ON cus_total.id_customer = cus.id_customer\n" +
                    "WHERE pur.datepurchase BETWEEN ? AND ?\n" +
                    "GROUP BY cus.id_customer, cus.firstName, cus.lastName, pr.name, cus_total.total_customer_expenses\n" +
                    "ORDER BY cus.id_customer, name DESC";

        Connection connection = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(interval.getStartDate()));
            preparedStatement.setDate(2, java.sql.Date.valueOf(interval.getEndDate()));
            preparedStatement.setDate(3, java.sql.Date.valueOf(interval.getStartDate()));
            preparedStatement.setDate(4, java.sql.Date.valueOf(interval.getEndDate()));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    CustomerResponse customerResponse = new CustomerResponse();
                    customerResponse.setId_customer(
                            resultSet.getLong("id_customer")
                    );
                    customerResponse.setName(
                            resultSet.getString("name")
                    );
                    customerResponse.setTotalExpenses(
                            resultSet.getBigDecimal("total_customer_expenses")
                    );

                    customerResponseList.add(customerResponse);
                }
            }
        } catch (SQLException e) {
            throw new MySQLException(e.getMessage());
        }

        sql = "SELECT cus.id_customer, pr.name as product_name, SUM(pr.expenses) AS total_product_expenses\n" +
                "FROM purchases pur\n" +
                "INNER JOIN customers cus ON cus.id_customer = pur.id_customer\n" +
                "INNER JOIN products pr ON pr.id_product = pur.id_product\n" +
                "WHERE pur.datepurchase BETWEEN ? AND ?\n" +
                "GROUP BY cus.id_customer, cus.firstName, cus.lastName, pr.name\n" +
                "ORDER BY cus.id_customer, cus.firstName, cus.lastName, total_product_expenses DESC";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(interval.getStartDate()));
            preparedStatement.setDate(2, java.sql.Date.valueOf(interval.getEndDate()));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Long tempID = -1L;
                List<Product> productList = new ArrayList<>();
                while (resultSet.next()) {
                    if(!tempID.equals(Long.valueOf(resultSet.getLong("id_customer")))) {
                        productList = new ArrayList<>();
                        tempID = resultSet.getLong("id_customer");

                        try {
                            List<Product> finalProductList = productList;
                            customerResponseList.stream().forEach(customerResponse -> {
                                try {
                                    if(customerResponse.getId_customer()
                                            .equals(Long.valueOf(resultSet.getLong("id_customer")))
                                    ) {
                                        customerResponse.setPurchases(finalProductList);
                                        throw new MyExitException();
                                    }
                                } catch (SQLException e) {
                                    throw new MySQLException(e.getMessage());
                                } catch (MyExitException e) {
                                    throw new MyExitException();
                                }
                            });
                        }
                        catch (MyExitException e) {}
                    }


                    Product product = new Product(
                            -1L,
                            resultSet.getString("product_name"),
                            resultSet.getBigDecimal("total_product_expenses")
                    );

                    productList.add(product);
                }
            }
        } catch (SQLException e) {
            throw new MySQLException(e.getMessage());
        }

        return customerResponseList;
    }

    private Long calculateWorkDay() {
        Long daysBetween = 0L;

        while (!date1.isAfter(date2)) {
            if (date1.getDayOfWeek() != DayOfWeek.SATURDAY && date1.getDayOfWeek() != DayOfWeek.SUNDAY) {
                daysBetween++;
            }
            date1 = date1.plus(1, ChronoUnit.DAYS);
        }

        return daysBetween;
    }

    private BigDecimal getTotalCustomersExpenses() throws MySQLException {
        BigDecimal totalExpenses = BigDecimal.ZERO;

        String sql = "SELECT SUM(total_customer_expenses) AS total_customers_expenses\n" +
                        "FROM (\n" +
                        "    SELECT cus.id_customer, SUM(pr.expenses) AS total_customer_expenses\n" +
                        "    FROM purchases pur\n" +
                        "    INNER JOIN customers cus ON cus.id_customer = pur.id_customer\n" +
                        "    INNER JOIN products pr ON pr.id_product = pur.id_product\n" +
                        "    WHERE pur.datepurchase BETWEEN ? AND ?\n" +
                        "    GROUP BY cus.id_customer\n" +
                        ") AS customer_expenses";

        Connection connection = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(interval.getStartDate()));
            preparedStatement.setDate(2, java.sql.Date.valueOf(interval.getEndDate()));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    totalExpenses = resultSet.getBigDecimal("total_customers_expenses");
                    break;
                }
            }
        }
        catch (SQLException e) {
            throw new MySQLException(e.getMessage());
        }

        return totalExpenses;
    }

    private Double getAvgCustomersExpenses() throws MySQLException {
        Double avgExpenses = 0D;

        String sql = "SELECT AVG(total_customer_expenses) AS avg_customers_expenses\n" +
                        "FROM (\n" +
                        "    SELECT cus.id_customer, SUM(pr.expenses) AS total_customer_expenses\n" +
                        "    FROM purchases pur\n" +
                        "    INNER JOIN customers cus ON cus.id_customer = pur.id_customer\n" +
                        "    INNER JOIN products pr ON pr.id_product = pur.id_product\n" +
                        "    WHERE pur.datepurchase BETWEEN ? AND ?\n" +
                        "    GROUP BY cus.id_customer\n" +
                        ") AS customer_expenses";

        Connection connection = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(interval.getStartDate()));
            preparedStatement.setDate(2, java.sql.Date.valueOf(interval.getEndDate()));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    avgExpenses = resultSet.getDouble("avg_customers_expenses");
                    break;
                }
            }
        }
        catch (SQLException e) {
            throw new MySQLException(e.getMessage());
        }

        return avgExpenses;
    }

    private void readJson(String jsonFileName) throws MySQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            //this.interval = objectMapper.readValue(json, this.interval.getClass());
            this.interval = objectMapper.readValue(new File(System.getProperty("user.dir") + "\\" + jsonFileName), this.interval.getClass());

            date1 = interval.getStartDate();
            date2 = interval.getEndDate();

        } catch (Exception e) {
            throw new MySQLException("Неправильный формат даты (yyyy-MM-dd)");
        }
    }
}
