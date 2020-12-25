package ru.pahanjes.beautysaloon.crm.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pahanjes.beautysaloon.crm.backend.entity.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
