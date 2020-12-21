package ru.pahanjes.beautysaloon.crm.backend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.pahanjes.beautysaloon.crm.backend.entity.Role;
import ru.pahanjes.beautysaloon.crm.backend.entity.User;
import ru.pahanjes.beautysaloon.crm.backend.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private static final Logger LOGGER = Logger.getLogger(CustomerService.class.getName());

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

            User defaultAdmin = new User();
            defaultAdmin.setActive(true);
            defaultAdmin.setUsername("admin");
            defaultAdmin.setPassword("pass");
            defaultAdmin.setRole(Collections.singleton(Role.ADMIN));

            userRepository.save(defaultUser);
            userRepository.save(defaultAdmin);
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
