package com.effortix.backend.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.effortix.backend.models.Employee;

@Repository
public
interface EmployeeRepository extends JpaRepository<Employee, Long> {
	 // Additional query methods can be defined here if needed
	  Employee findByEIdAndEPassword(Long eId, String ePassword);
	  
	  @Query("SELECT e FROM Employee e WHERE e.eISLead = true")
	    List<Employee> findAllLeads();
	  
	  long countByEInBenchTrue();

	    // Query to count the employees where eInBench is false
	    long countByEInBenchFalse();

	    // Query to fetch all employees where eInBench is true
	    List<Employee> findByEInBenchTrue();

	    // Query to fetch all employees where eInBench is false
	    List<Employee> findByEInBenchFalse();
	  
}