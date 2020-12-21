package ru.pahanjes.beautysaloon.crm.backend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.pahanjes.beautysaloon.crm.backend.entity.Employee;
import ru.pahanjes.beautysaloon.crm.backend.entity.Role;
import ru.pahanjes.beautysaloon.crm.backend.entity.User;
import ru.pahanjes.beautysaloon.crm.backend.repository.EmployeeRepository;
import ru.pahanjes.beautysaloon.crm.backend.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private EmployeeRepository employeeRepository;
    private static final Logger LOGGER = Logger.getLogger(CustomerService.class.getName());

    public UserService(UserRepository userRepository, EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @PostConstruct
    public void createDefaultUser() {
        if(userRepository.count() == 0) {
            User defaultUser = new User();
            defaultUser.setActive(true);
            defaultUser.setUsername("user");
            defaultUser.setPassword("pass");
            defaultUser.setRole(Collections.singleton(Role.USER));

            Employee employeeUser = new Employee();
            employeeUser.setUser(defaultUser);
            employeeUser.setEmail("user@mail.com");
            employeeUser.setFirstName("user");
            employeeUser.setLastName("account");
            employeeUser.setPhoneNumber("phone number");
            employeeUser.setPosition("simple user");
            employeeUser.setSalary(new BigDecimal(0));
            employeeUser.setStatus(Employee.Status.OnVacation);

            User defaultAdmin = new User();
            defaultAdmin.setActive(true);
            defaultAdmin.setUsername("admin");
            defaultAdmin.setPassword("pass");
            defaultAdmin.setRole(Collections.singleton(Role.ADMIN));

            Employee employeeAdmin = new Employee();
            employeeAdmin.setUser(defaultAdmin);
            employeeAdmin.setEmail("admin@mail.com");
            employeeAdmin.setFirstName("admin");
            employeeAdmin.setLastName("account");
            employeeAdmin.setPhoneNumber("phone number");
            employeeAdmin.setPosition("simple admin");
            employeeAdmin.setSalary(new BigDecimal(0));
            employeeAdmin.setStatus(Employee.Status.OnVacation);

            userRepository.save(defaultUser);
            userRepository.save(defaultAdmin);

            employeeRepository.save(employeeUser);
            employeeRepository.save(employeeAdmin);
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
