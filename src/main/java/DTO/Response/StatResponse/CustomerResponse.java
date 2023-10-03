package DTO.Response.StatResponse;

import Entities.Product;

import java.math.BigDecimal;
import java.util.*;

public class CustomerResponse {
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
}
