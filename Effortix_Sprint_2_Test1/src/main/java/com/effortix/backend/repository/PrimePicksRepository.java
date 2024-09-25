package com.effortix.backend.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.effortix.backend.models.Employee;
import com.effortix.backend.models.PrimePicks;
import com.effortix.backend.models.Ticket;

@Repository
public interface PrimePicksRepository extends JpaRepository<PrimePicks, Long> {

    // Custom query to fetch all active PrimePicks
    List<PrimePicks> findByStatus(String status);
}