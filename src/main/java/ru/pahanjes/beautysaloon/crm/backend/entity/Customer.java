package ru.pahanjes.beautysaloon.crm.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Customer extends AbstractEntity implements Cloneable {

    public enum Status {
        SignedUp ("Записан"),
        CanseldTheEntry ("Запись была отменена"),
        Served ("Обслуживается"),
        HasBeenServed ("Обслуживание завершено");

        private final String value;

        Status(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @NotNull
    @NotEmpty
    private String firstName = "";

    @NotNull
    @NotEmpty
    private String lastName = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Customer.Status status;

    @Email
    @NotNull
    @NotEmpty
    private String email = "";

    @NotNull
    @NotEmpty
    private String phoneNumber ="";

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

}