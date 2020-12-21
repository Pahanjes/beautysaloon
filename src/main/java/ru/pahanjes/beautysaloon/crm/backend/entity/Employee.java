package ru.pahanjes.beautysaloon.crm.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

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

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<Customer> clients;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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

    public List<Customer> getClients() {
        return clients;
    }

    public void setClients(List<Customer> clients) {
        this.clients = clients;
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

}