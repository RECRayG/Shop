package DTO.Request.SearchRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.*;

public class Criterias {
    @JsonProperty("criterias")
    private List<Object> criterias;

    private List<Criteria> criteriaList;

    public Criterias() {
        criteriaList = new ArrayList<>();
    }

    public Criterias(String jsonRequest) {
        criteriaList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Criterias searchCriteria = objectMapper.readValue(jsonRequest, this.getClass());

            List<Object> criterias = searchCriteria.getCriterias();
            for (Object crit : criterias) {
                LinkedHashMap<String, Object> criteria = (LinkedHashMap<String, Object>) crit;

                if (criteria.containsKey(SearchCriteria.lastName.name())) {
                    this.addLastName(criteria.get(SearchCriteria.lastName.name()).toString());

                    continue;
                }

                if (criteria.containsKey(SearchCriteria.productName.name()) &&
                        criteria.containsKey(SearchCriteria.minTimes.name()))
                {
                    this.addProduct(
                            criteria.get(SearchCriteria.productName.name()).toString(),
                            Integer.parseInt(criteria.get(SearchCriteria.minTimes.name()).toString()));

                    continue;
                }

                if(criteria.containsKey(SearchCriteria.maxExpenses.name()) &&
                        criteria.containsKey(SearchCriteria.minExpenses.name()))
                {
                    this.addExpenses(
                            BigDecimal.valueOf(Long.parseLong(criteria.get(SearchCriteria.minExpenses.name()).toString())),
                            BigDecimal.valueOf(Long.parseLong(criteria.get(SearchCriteria.maxExpenses.name()).toString())));

                    continue;
                }

                if(criteria.containsKey(SearchCriteria.badCustomers.name())) {
                    this.addBadCustomers(Integer.parseInt(criteria.get(SearchCriteria.badCustomers.name()).toString()));
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка чтения JSON файла с критерием search");
        }
    }

    public List<Object> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<Object> criterias) {
        this.criterias = criterias;
    }

    public void addLastName(String lastName) {
        LastNameCriteria lastNameCriteria = new LastNameCriteria();
        lastNameCriteria.setLastName(lastName);

        criteriaList.add(lastNameCriteria);
    }

    public void addProduct(String productName, Integer minTimes) {
        ProductCriteria productCriteria = new ProductCriteria();
        productCriteria.setProductName(productName);
        productCriteria.setMinTimes(minTimes);

        criteriaList.add(productCriteria);
    }

    public void addExpenses(BigDecimal minExpenses, BigDecimal maxExpenses) {
        ExpensesCriteria expensesCriteria = new ExpensesCriteria();
        expensesCriteria.setMinExpenses(minExpenses);
        expensesCriteria.setMaxExpenses(maxExpenses);

        criteriaList.add(expensesCriteria);
    }

    public void addBadCustomers(Integer badCustomers) {
        BadCustomersCriteria badCustomersCriteria = new BadCustomersCriteria();
        badCustomersCriteria.setBadCustomers(badCustomers);

        criteriaList.add(badCustomersCriteria);
    }

    public List<Criteria> getCriteriaList() {
        return criteriaList;
    }
}