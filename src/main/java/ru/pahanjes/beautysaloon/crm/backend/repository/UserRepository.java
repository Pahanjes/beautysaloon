package ru.pahanjes.beautysaloon.crm.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pahanjes.beautysaloon.crm.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
