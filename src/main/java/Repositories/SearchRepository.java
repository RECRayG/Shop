package Repositories;

import DBConnection.DatabaseConnection;
import DTO.Request.SearchRequest.*;
import DTO.Response.SearchResponse.SearchResponse;
import DTO.Response.SearchResponse.SearchResults;
import Entities.Customer;
import Exceptions.MySQLException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.math.BigDecimal;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchRepository {
    private String response;

    public String searchCustomersByCriterias(String jsonFileName) throws MySQLException {
        try {
            SearchResponse searchResponse = new SearchResponse();
            searchResponse.setType(OperationType.search.name());

            List<SearchResults> searchResultsList = new ArrayList<>();

            Criterias criterias = new Criterias(jsonFileName);
            DatabaseConnection.getInstance().initializeConnection();

            criterias.getCriteriaList().stream().forEach(criteria -> {
                if(LastNameCriteria.class.equals(criteria.getClass())) {
                    SearchResults searchResults = new SearchResults();
                    searchResults.setCriteria(criteria);
                    searchResults.setResults(
                            searchFromLastName(((LastNameCriteria) criteria).getLastName())
                    );

                    searchResultsList.add(searchResults);
                }

                if(ProductCriteria.class.equals(criteria.getClass())) {
                    SearchResults searchResults = new SearchResults();
                    searchResults.setCriteria(criteria);
                    searchResults.setResults(
                            searchFromProduct(((ProductCriteria) criteria).getProductName(),
                                    ((ProductCriteria) criteria).getMinTimes())
                    );

                    searchResultsList.add(searchResults);
                }

                if(ExpensesCriteria.class.equals(criteria.getClass())) {
                    SearchResults searchResults = new SearchResults();
                    searchResults.setCriteria(criteria);
                    searchResults.setResults(
                            searchFromIntervalExpenses(((ExpensesCriteria) criteria).getMinExpenses(),
                                    ((ExpensesCriteria) criteria).getMaxExpenses())
                    );

                    searchResultsList.add(searchResults);
                }

                if(BadCustomersCriteria.class.equals(criteria.getClass())) {
                    SearchResults searchResults = new SearchResults();
                    searchResults.setCriteria(criteria);
                    searchResults.setResults(
                            searchFromBadCustomers(((BadCustomersCriteria) criteria).getBadCustomers())
                    );

                    searchResultsList.add(searchResults);
                }
            });

            searchResponse.setResults(searchResultsList);

            DatabaseConnection.getInstance().closeConnection();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            try {
                // Преобразуем объект SearchResponse в JSON строку
                response = objectMapper.writeValueAsString(searchResponse);
            } catch (Exception e) {
                throw new MySQLException(e.getMessage());
            }
        }
        catch(MySQLException e) {
            throw new MySQLException(e.getMessage());
        }

        return response;
    }

    private List<Customer> searchFromLastName(String lastName) throws MySQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE lastName = ? ORDER BY firstName, lastName";

        Connection connection = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, lastName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    customers.add(new Customer(
                            resultSet.getLong("id_customer"),
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"))
                    );
                }
            }
        } catch (SQLException e) {
            throw new MySQLException(e.getMessage());
        }

        return customers;
    }

    private List<Customer> searchFromProduct(String productName, Integer minTimes) throws MySQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT cus.id_customer, cus.firstName, cus.lastName FROM purchases pur\n" +
                        "INNER JOIN customers cus ON cus.id_customer = pur.id_customer\n" +
                        "INNER JOIN products pr ON pr.id_product = pur.id_product\n" +
                        "WHERE pr.name = ?\n" +
                        "GROUP BY cus.id_customer, cus.firstName, cus.lastName\n" +
                        "HAVING COUNT(pur.id_purchase) > ?";

        Connection connection = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, productName);
            preparedStatement.setInt(2, minTimes);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    customers.add(new Customer(
                            resultSet.getLong("id_customer"),
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"))
                    );
                }
            }
        } catch (SQLException e) {
            throw new MySQLException(e.getMessage());
        }

        return customers;
    }

    private List<Customer> searchFromIntervalExpenses(BigDecimal minExpenses, BigDecimal maxExpenses) throws MySQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT cus.id_customer, cus.firstName, cus.lastName FROM purchases pur\n" +
                        "INNER JOIN customers cus ON cus.id_customer = pur.id_customer\n" +
                        "INNER JOIN products pr ON pr.id_product = pur.id_product\n" +
                        "GROUP BY cus.id_customer, cus.firstName, cus.lastName\n" +
                        "HAVING SUM(pr.expenses) BETWEEN ? AND ?";

        Connection connection = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, minExpenses);
            preparedStatement.setBigDecimal(2, maxExpenses);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    customers.add(new Customer(
                            resultSet.getLong("id_customer"),
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"))
                    );
                }
            }
        } catch (SQLException e) {
            throw new MySQLException(e.getMessage());
        }

        return customers;
    }

    private List<Customer> searchFromBadCustomers(Integer badCustomers) throws MySQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT cus.id_customer, cus.firstName, cus.lastName, COUNT(pur.id_purchase) FROM purchases pur\n" +
                        "INNER JOIN customers cus ON cus.id_customer = pur.id_customer\n" +
                        "INNER JOIN products pr ON pr.id_product = pur.id_product\n" +
                        "GROUP BY cus.id_customer, cus.firstName, cus.lastName\n" +
                        "ORDER BY COUNT(pur.id_purchase) ASC\n" +
                        "LIMIT ?";

        Connection connection = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, badCustomers);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    customers.add(new Customer(
                            resultSet.getLong("id_customer"),
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"))
                    );
                }
            }
        } catch (SQLException e) {
            throw new MySQLException(e.getMessage());
        }

        return customers;
    }
}
