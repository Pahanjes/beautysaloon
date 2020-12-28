package ru.pahanjes.beautysaloon.crm.backend.service;

import org.springframework.stereotype.Service;
import ru.pahanjes.beautysaloon.crm.backend.entity.Customer;
import ru.pahanjes.beautysaloon.crm.backend.entity.Employee;
import ru.pahanjes.beautysaloon.crm.backend.repository.CustomerRepository;
import ru.pahanjes.beautysaloon.crm.backend.repository.EmployeeRepository;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CustomerService {
    private static final Logger LOGGER = Logger.getLogger(CustomerService.class.getName());
    private CustomerRepository customerRepository;
    private EmployeeRepository employeeRepository;

    public CustomerService(CustomerRepository customerRepository,
                           EmployeeRepository employeeRepository) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public List<Customer> findAll(String filter) {
        if(filter == null || filter.isEmpty()){
            return customerRepository.findAll();
        } else {
            return customerRepository.search(filter);
        }
    }

    public Customer findById(Long id) {
        return customerRepository.search(id);
    }

    public Long count() {
        return customerRepository.count();
    }

    public void delete(Customer customer) {
        if (customer.getEmployee() != null) {
            Employee employee = customer.getEmployee();
            employee.removeCustomer(customer);
            employeeRepository.save(employee);
            customer.setEmployee(null);
        }
        customerRepository.delete(customer);
    }

    public void save(Customer customer) {
        if(customer == null) {
            LOGGER.log(Level.SEVERE,
                    "customer_error");
            return;
        }
        customerRepository.save(customer);
    }
}
