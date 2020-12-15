package ru.pahanjes.beautysaloon.crm.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Employee extends AbstractEntity{

    public enum Status {
        CurrentlyWorking, LunchBreak, WaitingForTheCustomer, OnVacation, fired
    }

    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
    private List<Customer> clients = new LinkedList<>();

    public Employee() {
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
    private float salary = 0.0F;

    public List<Customer> getClients() {
        return clients;
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

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

}