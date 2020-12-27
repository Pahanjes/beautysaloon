package ru.pahanjes.beautysaloon.crm.backend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.pahanjes.beautysaloon.crm.backend.entity.Customer;
import ru.pahanjes.beautysaloon.crm.backend.entity.Employee;
import ru.pahanjes.beautysaloon.crm.backend.entity.Role;
import ru.pahanjes.beautysaloon.crm.backend.entity.User;
import ru.pahanjes.beautysaloon.crm.backend.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmployeeService employeeService;
    private final ServiceService serviceService;
    private final CustomerService customerService;
    private static final Logger LOGGER = Logger.getLogger(CustomerService.class.getName());

    public UserService(UserRepository userRepository, EmployeeService employeeService, ServiceService serviceService, CustomerService customerService) {
        this.userRepository = userRepository;
        this.employeeService = employeeService;
        this.serviceService = serviceService;
        this.customerService = customerService;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAll(String filter) {
        if (filter == null || filter.isEmpty()) {
            return userRepository.findAll();
        } else {
            return userRepository.search(filter);
        }
    }

    public User findById(Long id) {
        return userRepository.search(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @PostConstruct
    public void createDefaultUser() {
        if(userRepository.count() == 0) {
            User user = new User("user", "pass", true, null, Collections.singleton(Role.USER));
            User admin = new User("admin", "pass", true, null, Collections.singleton(Role.ADMIN));
            userRepository.save(user);
            userRepository.save(admin);
        }
        if(customerService.findAll().size() == 0) {
            Customer customerWomen = new Customer(
                    "Алла",
                    "Борисова",
                    null,
                    null,
                    Customer.Status.Served,
                    "allo4ka@gmail.com",
                    "+7-948-465-35-14",
                    null
            );
            Customer customerMen = new Customer(
                    "Сергей",
                    "Мавроди",
                    null,
                    null,
                    Customer.Status.Served,
                    "serezha@mmm.ussr",
                    "+7-965-863-18-65",
                    null
            );
            customerService.save(customerWomen);
            customerService.save(customerMen);
        }
        if(serviceService.findAll().size() == 0) {
            ru.pahanjes.beautysaloon.crm.backend.entity.Service haircut = new ru.pahanjes.beautysaloon.crm.backend.entity.Service(
                    "Стрижка мужская\"люкс\"",
                    new BigDecimal("600.0"),
                    true
            );
            ru.pahanjes.beautysaloon.crm.backend.entity.Service shugaring = new ru.pahanjes.beautysaloon.crm.backend.entity.Service(
                    "Шугаринг",
                    new BigDecimal("2700.0"),
                    true
            );
            ru.pahanjes.beautysaloon.crm.backend.entity.Service hairPaint = new ru.pahanjes.beautysaloon.crm.backend.entity.Service(
                    "Покраска волос женская",
                    new BigDecimal("2000.0"),
                    true
            );
            serviceService.save(haircut);
            serviceService.save(shugaring);
            serviceService.save(hairPaint);
        }
        if(employeeService.findAll().size() == 0) {
            Employee administrator = new Employee(
                    "Павел",
                    "Василец",
                    Employee.Status.CurrentlyWorking,
                    "pahanjes.spb@gmail.com",
                    "+7-920-537-50-02",
                    "администратор",
                    new BigDecimal("500.0")
            );
            Employee secretary = new Employee(
                    "Анастасия",
                    "Чиркова",
                    Employee.Status.CurrentlyWorking,
                    "Anschirkova@gmail.com",
                    "+7-925-683-45-36",
                    "Секретарь",
                    new BigDecimal("1500.0")
            );
            employeeService.save(administrator);
            employeeService.save(secretary);
        }
    }

    public void delete(User user) {
        if(user.getEmployee() != null) {
            if(user.getEmployee().getCustomers().size() != 0) {
                user.getEmployee().getCustomers().forEach(customer -> {
                    customer.setEmployee(null);
                    customerService.save(customer);
                });
            }
            employeeService.delete(user.getEmployee());
        }
        userRepository.delete(user);
    }

    public void save(User user) {
        if (user == null){
            LOGGER.log(Level.SEVERE,
                    "error");
            return;
        }
        userRepository.save(user);
    }
}
