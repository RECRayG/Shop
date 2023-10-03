import DTO.Request.SearchRequest.Criterias;
import DTO.Response.SearchResponse.SearchResponse;
import DTO.Response.SearchResponse.SearchResults;
import Entities.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String json = "{\"criterias\":[" +
                "{\"lastName\":\"Иванов\"}," +
                "{\"lastName\":\"Петров\"}," +
                "{\"productName\":\"Минеральная вода\",\"minTimes\":5}," +
                "{\"minExpenses\":112,\"maxExpenses\":4000}," +
                "{\"badCustomers\":3}" +
                "]}";



        Criterias criterias = new Criterias(json);

        SearchResults searchResults = new SearchResults();
        searchResults.setCriteria(criterias.getCriteriaList().get(0));
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1L, "-", "-"));
        customers.add(new Customer(2L, "-", "-"));
        searchResults.setResults(customers);

        SearchResults searchResults2 = new SearchResults();
        searchResults2.setCriteria(criterias.getCriteriaList().get(2));
        List<Customer> customers2 = new ArrayList<>();
        customers2.add(new Customer(3L, "", ""));
        customers2.add(new Customer(4L, "", ""));
        searchResults2.setResults(customers2);

        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setType("search");
        List<SearchResults> searchResultsList = new ArrayList<>();

        searchResultsList.add(searchResults);
        searchResultsList.add(searchResults2);
        searchResponse.setResults(searchResultsList);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            // Преобразуем объект SearchResponse в JSON строку
            String json2 = objectMapper.writeValueAsString(searchResponse);

            // Выводим JSON строку
            System.out.println(json2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
