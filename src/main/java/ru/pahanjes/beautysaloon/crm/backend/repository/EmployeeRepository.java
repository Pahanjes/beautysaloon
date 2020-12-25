package ru.pahanjes.beautysaloon.crm.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.pahanjes.beautysaloon.crm.backend.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("Select e from Employee e " +
            "where lower(e.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(e.lastName) like lower(concat('%', :searchTerm, '%'))")
    List<Employee> search(@Param("searchTerm") String searchTerm);

    @Query("Select e from Employee e where e.id= :searchTerm")
    Employee search(@Param("searchTerm") Long id);
}
