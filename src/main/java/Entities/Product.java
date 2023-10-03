package Entities;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private Long id_product;
    private String name;
    private BigDecimal expenses;

    public Product() {
        id_product = -1L;
        name = "";
        expenses = BigDecimal.ZERO;
    }

    public Product(Long id_product, String name, BigDecimal expenses) {
        this.id_product = id_product;
        this.name = name;
        this.expenses = expenses;
    }

    public Long getId_product() {
        return id_product;
    }

    public void setId_product(Long id_product) {
        this.id_product = id_product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public void setExpenses(BigDecimal expenses) {
        this.expenses = expenses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id_product, product.id_product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_product);
    }
}
