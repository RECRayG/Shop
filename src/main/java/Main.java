import DTO.Response.ErrorResponse.ErrorResponse;
import Exceptions.MySQLException;
import Repositories.OperationType;
import Repositories.SearchRepository;
import Repositories.StatRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    private static String response = "";
    // Имя выходного файла
    private static String outFileName = "output.json";
    // Имена входного файла
    private static String inFileName = "input.json";

    public static void main(String[] args) {
//        String json = "{\"criterias\":[" +
//                "{\"lastName\":\"Иванов\"}," +
//                "{\"lastName\":\"Петров\"}," +
//                "{\"productName\":\"Хлеб\",\"minTimes\":1}," +
//                "{\"minExpenses\":10,\"maxExpenses\":70}," +
//                "{\"badCustomers\":3}" +
//                "]}";
//
//        String json2 = "{\"startDate\": \"2000-01-14\"," +
//                "\"endDate\": \"2023-12-26\"" +
//                "}";

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            List<String> parameters = Arrays.asList(args);
            List<String> tempFilesName = parameters.stream()
                    .filter(fileName -> fileName.contains(".json"))
                    .collect(Collectors.toList());
            if(tempFilesName.size() == 2) {
                inFileName = tempFilesName.get(0);
                outFileName = tempFilesName.get(1);
            } else {
                throw new MySQLException("Количество переданных файлов меньше или больше 2");
            }

            if(parameters.contains(OperationType.search.name())) {
                SearchRepository searchRepository = new SearchRepository();
                response = searchRepository.searchCustomersByCriterias(inFileName);
            } else if(parameters.contains(OperationType.stat.name())) {
                StatRepository statRepository = new StatRepository();
                response = statRepository.statByPeriod(inFileName);
            } else {
                throw new MySQLException("Не поддерживаемая операция");
            }

            try {
                String jsonString = objectMapper.writeValueAsString(response);
                Files.write(Paths.get(System.getProperty("user.dir") + "\\" + outFileName), jsonString.getBytes());
            } catch (IOException ex) {
                throw new MySQLException(ex.getMessage());
            }
        }
        catch(MySQLException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setType(OperationType.error.name());
            errorResponse.setMessage(e.getMessage());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            try {
                // Преобразуем объект SearchResponse в JSON строку
                response = objectMapper.writeValueAsString(errorResponse);
                try {
                    String jsonString = objectMapper.writeValueAsString(response);
                    Files.write(Paths.get(System.getProperty("user.dir") + "\\" + outFileName), jsonString.getBytes());
                } catch (IOException ex) {
                    throw new MySQLException(ex.getMessage());
                }
            } catch (Exception exception) {
                System.out.println("Ошибка преобразования в JSON в обработчике ошибок");
            }
        }
    }
}
