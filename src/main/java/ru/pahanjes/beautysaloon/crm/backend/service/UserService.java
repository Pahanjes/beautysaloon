package ru.pahanjes.beautysaloon.crm.backend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.pahanjes.beautysaloon.crm.backend.entity.Customer;
import ru.pahanjes.beautysaloon.crm.backend.entity.Employee;
import ru.pahanjes.beautysaloon.crm.backend.entity.Role;
import ru.pahanjes.beautysaloon.crm.backend.entity.User;
import ru.pahanjes.beautysaloon.crm.backend.repository.CustomerRepository;
import ru.pahanjes.beautysaloon.crm.backend.repository.EmployeeRepository;
import ru.pahanjes.beautysaloon.crm.backend.repository.ServiceRepository;
import ru.pahanjes.beautysaloon.crm.backend.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private ServiceRepository serviceRepository;
    private CustomerRepository customerRepository;
    private static final Logger LOGGER = Logger.getLogger(CustomerService.class.getName());

    public UserService(UserRepository userRepository, EmployeeRepository employeeRepository, ServiceRepository serviceRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.serviceRepository = serviceRepository;
        this.customerRepository = customerRepository;
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
            Employee employeeUser = new Employee();
            Employee employeeAdmin = new Employee();

            User defaultAdmin = new User();
            User defaultUser = new User();

            employeeUser.setEmail("user@mail.com");
            employeeUser.setFirstName("user");
            employeeUser.setLastName("account");
            employeeUser.setPhoneNumber("phone number");
            employeeUser.setPosition("simple user");
            employeeUser.setSalary(new BigDecimal(0));
            employeeUser.setStatus(Employee.Status.OnVacation);

            employeeRepository.save(employeeUser);

            defaultUser.setActive(true);
            defaultUser.setUsername("user");
            defaultUser.setPassword("pass");
            defaultUser.setRole(Collections.singleton(Role.USER));
            defaultUser.setEmployee(employeeUser);

            userRepository.save(defaultUser);
            employeeUser.setUser(defaultUser);
            employeeRepository.save(employeeUser);

            employeeAdmin.setEmail("admin@mail.com");
            employeeAdmin.setFirstName("admin");
            employeeAdmin.setLastName("account");
            employeeAdmin.setPhoneNumber("phone number");
            employeeAdmin.setPosition("simple admin");
            employeeAdmin.setSalary(new BigDecimal(0));
            employeeAdmin.setStatus(Employee.Status.OnVacation);

            employeeRepository.save(employeeAdmin);

            defaultAdmin.setActive(true);
            defaultAdmin.setUsername("admin");
            defaultAdmin.setPassword("pass");
            defaultAdmin.setRole(Collections.singleton(Role.ADMIN));
            defaultAdmin.setEmployee(employeeAdmin);

            userRepository.save(defaultAdmin);
            employeeAdmin.setUser(defaultAdmin);
            employeeRepository.save(employeeAdmin);

            ArrayList<ru.pahanjes.beautysaloon.crm.backend.entity.Service> services = new ArrayList<>();
            services.add(new ru.pahanjes.beautysaloon.crm.backend.entity.Service("Стрижка мужская \"Люкс\"", new BigDecimal("500.0")));
            services.add(new ru.pahanjes.beautysaloon.crm.backend.entity.Service("Шугаринг", new BigDecimal("800.0")));

            ArrayList<Customer> customers = new ArrayList<>();
            customers.add(new Customer("Алла", "Борисова", Customer.Status.SignedUp, "allo4ka@mail.ru", "+7-956-674-75-32"));
            customers.add(new Customer("Сергей", "Мавроди", Customer.Status.HasBeenServed, "serezha@mmm.ussr", "+7-915-624-61-45"));

            for(ru.pahanjes.beautysaloon.crm.backend.entity.Service service : services) {
                serviceRepository.save(service);
            }

            for(Customer customer : customers) {
                customerRepository.save(customer);
            }
        }
    }

    public void delete(User user) {
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
