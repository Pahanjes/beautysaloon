package ru.pahanjes.beautysaloon.crm.backend.service;

import org.springframework.stereotype.Service;
import ru.pahanjes.beautysaloon.crm.backend.entity.Employee;
import ru.pahanjes.beautysaloon.crm.backend.repository.EmployeeRepository;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EmployeeService {

    private static final Logger LOGGER = Logger.getLogger(EmployeeService.class.getName());
    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public List<Employee> findAllWithoutAccount() {
        List<Employee> employeesWithoutAccount = employeeRepository.findAll();
        employeesWithoutAccount.removeIf(employee -> employee.getUser() != null);
        return employeesWithoutAccount;
    }

    public List<Employee> findAll(String filter) {
        if(filter == null || filter.isEmpty()){
            return employeeRepository.findAll();
        } else {
            return employeeRepository.search(filter);
        }
    }

    public Employee findById(Long id) {
        return employeeRepository.search(id);
    }

    public void delete(Employee employee) {
        employeeRepository.delete(employee);
    }

    public void save(Employee employee) {
        if(employee == null) {
            LOGGER.log(Level.SEVERE,
                    "Клиент пуст. Вы уверены, что хотите подключить форму к приложению?");
            return;
        }
        employeeRepository.save(employee);
    }
}
