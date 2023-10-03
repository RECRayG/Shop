package Entities;

import java.util.Objects;

public class Customer {
    private Long id_customer;
    private String firstName;
    private String lastName;

    public Customer() {
        id_customer = -1L;
        firstName = "";
        lastName = "";
    }

    public Customer(Long id_customer, String firstName, String lastName) {
        this.id_customer = id_customer;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId_customer() {
        return id_customer;
    }

    public void setId_customer(Long id_customer) {
        this.id_customer = id_customer;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id_customer, customer.id_customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_customer);
    }
}
