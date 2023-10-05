import Repositories.SearchRepository;
import Repositories.StatRepository;

public class Main {
    //        Criterias criterias = new Criterias(json);
//
//        SearchResults searchResults = new SearchResults();
//        searchResults.setCriteria(criterias.getCriteriaList().get(0));
//        List<Customer> customers = new ArrayList<>();
//        customers.add(new Customer(1L, "-", "-"));
//        customers.add(new Customer(2L, "-", "-"));
//        searchResults.setResults(customers);
//
//        SearchResults searchResults2 = new SearchResults();
//        searchResults2.setCriteria(criterias.getCriteriaList().get(2));
//        List<Customer> customers2 = new ArrayList<>();
//        customers2.add(new Customer(3L, "", ""));
//        customers2.add(new Customer(4L, "", ""));
//        searchResults2.setResults(customers2);
//
//        SearchResponse searchResponse = new SearchResponse();
//        searchResponse.setType("search");
//        List<SearchResults> searchResultsList = new ArrayList<>();
//
//        searchResultsList.add(searchResults);
//        searchResultsList.add(searchResults2);
//        searchResponse.setResults(searchResultsList);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//        try {
//            // Преобразуем объект SearchResponse в JSON строку
//            String json2 = objectMapper.writeValueAsString(searchResponse);
//
//            // Выводим JSON строку
//            System.out.println(json2);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    public static void main(String[] args) {
        String json = "{\"criterias\":[" +
                "{\"lastName\":\"Иванов\"}," +
                "{\"lastName\":\"Петров\"}," +
                "{\"productName\":\"Хлеб\",\"minTimes\":1}," +
                "{\"minExpenses\":10,\"maxExpenses\":70}," +
                "{\"badCustomers\":3}" +
                "]}";

        String json2 = "{\"startDate\": \"2000-01-14\"," +
                "\"endDate\": \"2023-12-26\"" +
                "}";




        //SearchRepository searchRepository = new SearchRepository();
        //System.out.println(searchRepository.searchCustomersByCriterias(json));

        StatRepository statRepository = new StatRepository();
        System.out.println(statRepository.statByPeriod(json2));
    }
}
