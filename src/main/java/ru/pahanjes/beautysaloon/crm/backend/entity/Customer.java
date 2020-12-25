package ru.pahanjes.beautysaloon.crm.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Set;

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

    @ManyToOne(fetch = FetchType.EAGER)//
    @JoinColumn(name = "employee_id")
    private Employee employee;

    /*@ManyToMany(mappedBy = "customers", fetch = FetchType.EAGER)
    private Set<Service> services;*/

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "customer_service",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<Service> services;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Customer.Status status;

    @Email
    @NotNull
    @NotEmpty
    private String email = "";

    @NotNull
    @NotEmpty
    private String phoneNumber = "";

    @Column(name = "timetable")
    private java.time.LocalDateTime timetable = LocalDateTime.of(2000, Month.JANUARY, 1, 12, 0, 0);

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

    public LocalDateTime getTimetable() {
        return timetable;
    }

    public void setTimetable(LocalDateTime timetable) {
        this.timetable = timetable;
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

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

}