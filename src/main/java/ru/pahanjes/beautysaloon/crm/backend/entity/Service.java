package ru.pahanjes.beautysaloon.crm.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Service extends AbstractEntity {

    @Column(name = "service")
    private String service;

    @NotNull
    private BigDecimal price;

    @NotNull
    boolean isActive = true;

    public Service(String service, @NotNull BigDecimal price) {
        this.service = service;
        this.price = price;
    }

    public Service(String service, @NotNull BigDecimal price, @NotNull boolean isActive) {
        this.service = service;
        this.price = price;
        this.isActive = isActive;
    }

    public Service() {

    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
