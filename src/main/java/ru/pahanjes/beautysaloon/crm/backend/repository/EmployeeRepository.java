package ru.pahanjes.beautysaloon.crm.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pahanjes.beautysaloon.crm.backend.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
