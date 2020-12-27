package ru.pahanjes.beautysaloon.crm.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Employee extends AbstractEntity{

    public enum Status {
        CurrentlyWorking ("На смене"),
        LunchBreak ("На обеде"),
        WaitingForTheCustomer ("Ожидает клиента"),
        OnVacation ("В отпуске"),
        fired ("Уволен");

        private final String value;

        static public int count() {
            return 5;
        }

        Status(String value){
            this.value = value;
        }

        public String getValue(){
            return value;
        }
    }
    /**/
    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)//LAZY
    private Set<Customer> customers;

    @OneToOne(cascade = {/*CascadeType.MERGE, *//*CascadeType.REFRESH,*//* */CascadeType.REMOVE}, fetch = FetchType.EAGER)//LAZY
    @JoinColumn(name = "user_id")
    private User user;
    /*@OneToOne(mappedBy = "employee")
    private User user;*/

    @ManyToMany(fetch = FetchType.EAGER, cascade = {/*CascadeType.REFRESH,*/ CascadeType.MERGE})
    @JoinTable(
            name = "employee_service",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<Service> services;

    public Employee() {

    }

    public Employee(@NotNull @NotEmpty String firstName, @NotNull @NotEmpty String lastName, @NotNull Employee.Status status,
                    @Email @NotNull @NotEmpty String email, @NotNull @NotEmpty String phoneNumber,
                    @NotNull @NotEmpty String position, @NotNull BigDecimal salary
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.salary = salary;
    }

    public Employee(Set<Customer> customers, User user, Set<Service> services, @NotNull @NotEmpty String firstName,
                    @NotNull @NotEmpty String lastName, @NotNull Employee.Status status, @Email @NotNull @NotEmpty String email,
                    @NotNull @NotEmpty String phoneNumber, @NotNull @NotEmpty String position, @NotNull BigDecimal salary
    ) {
        this.customers = customers;
        this.user = user;
        this.services = services;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.salary = salary;
    }

    @NotNull
    @NotEmpty
    private String firstName = "";

    @NotNull
    @NotEmpty
    private String lastName = "";

    @Enumerated(EnumType.STRING)
    @NotNull
    private Employee.Status status;

    @Email
    @NotNull
    @NotEmpty
    private String email = "";

    @NotNull
    @NotEmpty
    private String phoneNumber = "";

    @NotNull
    @NotEmpty
    private String position = "";

    @NotNull
    private BigDecimal salary = new BigDecimal("0.0");

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public void addCustomer(Customer customer) {
        this.customers.add(customer);
    }

    public void removeCustomer(Customer customer) {
        this.customers.remove(customer);
    }

    public void clearCustomers() {
        this.customers.clear();
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFullNameWithPosition() {
        return firstName + ' ' + lastName + ": " + position;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal sal) {
        this.salary = sal;
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    public void addService(Service service) {
        this.services.add(service);
    }

    public void removeService(Service service) {
        this.services.remove(service);
    }

}