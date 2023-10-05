package DTO.Response.StatResponse;

import Entities.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.*;

public class CustomerResponse {
    @JsonIgnore
    private Long id_customer;
    private String name;
    private List<Product> purchases;
    private BigDecimal totalExpenses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Product> purchases) {
        this.purchases = purchases;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public Long getId_customer() {
        return id_customer;
    }

    public void setId_customer(Long id_customer) {
        this.id_customer = id_customer;
    }
}
