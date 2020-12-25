package ru.pahanjes.beautysaloon.crm.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.pahanjes.beautysaloon.crm.backend.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Query("Select u from User u where lower(u.username) like lower(concat('%', :searchTerm, '%'))")
    List<User> search(@Param("searchTerm") String searchTerm);

    @Query("Select u from User u where u.id= :searchTerm")
    User search(@Param("searchTerm") Long id);
}
