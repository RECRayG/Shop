package DTO.Request.SearchRequest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BadCustomersCriteria {
    @JsonProperty("badCustomers")
    private Integer badCustomers;

    public Integer getBadCustomers() {
        return badCustomers;
    }

    public void setBadCustomers(Integer badCustomers) {
        this.badCustomers = badCustomers;
    }
}
