package Entities;

import java.time.Instant;
import java.util.Objects;

public class Purchase {
    private Long id_purchase;
    private Customer customer;
    private Product product;
    private Instant datePurchase;

    public Purchase() {
        id_purchase = -1L;
        customer = new Customer();
        product = new Product();
        datePurchase = Instant.MIN;
    }

    public Purchase(Long id_purchase, Customer customer, Product product, Instant datePurchase) {
        this.id_purchase = id_purchase;
        this.customer = customer;
        this.product = product;
        this.datePurchase = datePurchase;
    }

    public Long getId_purchase() {
        return id_purchase;
    }

    public void setId_purchase(Long id_purchase) {
        this.id_purchase = id_purchase;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Instant getDatePurchase() {
        return datePurchase;
    }

    public void setDatePurchase(Instant datePurchase) {
        this.datePurchase = datePurchase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase purchase = (Purchase) o;
        return Objects.equals(id_purchase, purchase.id_purchase) &&
                Objects.equals(customer, purchase.customer) &&
                Objects.equals(product, purchase.product) &&
                Objects.equals(datePurchase, purchase.datePurchase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_purchase, customer, product, datePurchase);
    }
}
