package DTO.Response.StatResponse;

import java.math.BigDecimal;
import java.util.*;

public class StatResponse {
    private String type;
    private Long totalDays;
    private List<CustomerResponse> customers;
    private BigDecimal totalExpenses;
    private Double avgExpenses;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Long totalDays) {
        this.totalDays = totalDays;
    }

    public List<CustomerResponse> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerResponse> customers) {
        this.customers = customers;
    }

    public Double getAvgExpenses() {
        return avgExpenses;
    }

    public void setAvgExpenses(Double avgExpenses) {
        this.avgExpenses = avgExpenses;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }
}
