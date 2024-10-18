	package com.effortix.backend.repository;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.effortix.backend.models.Employee;
import com.effortix.backend.models.EmployeeCredits;
import com.effortix.backend.models.Ticket;

@Repository
public interface EmployeeCreditsRepository extends JpaRepository<EmployeeCredits, Long> {
	 
	
    @Query("SELECT ec FROM EmployeeCredits ec WHERE ec.employee.eId = :eId AND ec.date BETWEEN :startDate AND :endDate")
    List<EmployeeCredits> findByEmployeeAndDateBetween(@Param("eId") Long eId, 
                                                       @Param("startDate") Date startDate, 
                                                       @Param("endDate") Date endDate);
    
    
    @Query("SELECT ec FROM EmployeeCredits ec WHERE ec.EId = :eId")
    List<EmployeeCredits> findByEmployeeId(Long eId);
}
