package ru.pahanjes.beautysaloon.crm.backend.service;


import ru.pahanjes.beautysaloon.crm.backend.entity.Service;
import ru.pahanjes.beautysaloon.crm.backend.repository.EmployeeRepository;
import ru.pahanjes.beautysaloon.crm.backend.repository.ServiceRepository;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {
    private ServiceRepository serviceRepository;
    private EmployeeRepository employeeRepository;

    public ServiceService(ServiceRepository serviceRepository, EmployeeRepository employeeRepository) {
        this.serviceRepository = serviceRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Service> findAll() {
        return  serviceRepository.findAll();
    }

    public Long count() {
        return  serviceRepository.count();
    }

    public void delete(Service service) {
        serviceRepository.delete(service);
    }

    public void save(Service service) {
        serviceRepository.save(service);
    }
}
