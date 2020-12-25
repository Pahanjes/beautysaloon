package ru.pahanjes.beautysaloon.crm.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Service extends AbstractEntity {

    @Column(name = "service")
    private String service = "";

    /*@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)//LAZY
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(
            name = "employee_services",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_service_id")
    )
    private List<Employee> employees;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(
            name = "customer_services",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_service_id")
    )
    private List<Customer> customers;*/

    @NotNull
    private BigDecimal price = new BigDecimal("0.0");

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    /*public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public void addCustomer(Customer customer) {
        this.customers.add(customer);
    }*/

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
