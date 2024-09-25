package com.effortix.backend.repository;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.effortix.backend.models.Employee;
import com.effortix.backend.models.EmployeeTimesheetEntries;

@Repository
public
interface EmployeeTimesheetEntriesRepository extends JpaRepository<EmployeeTimesheetEntries, Long> {
	 // Additional query methods can be defined here if needed
	
	@Query("SELECT ete FROM EmployeeTimesheetEntries ete WHERE ete.employee.eId = :employeeId")
	List<EmployeeTimesheetEntries> findAllByEmployeeId(@Param("employeeId") Long employeeId);
	
	
	@Query("SELECT ete FROM EmployeeTimesheetEntries ete WHERE ete.employee.eId = :employeeId AND ete.date BETWEEN :fromDate AND :toDate")
	List<EmployeeTimesheetEntries> findAllByEmployeeIdAndDateBetween(@Param("employeeId") Long employeeId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	
	@Query("SELECT ete FROM EmployeeTimesheetEntries ete WHERE ete.employee.eId = :employeeId AND ete.date = :date")
	List<EmployeeTimesheetEntries> findByEmployeeIdAndDate(@Param("employeeId") Long employeeId, @Param("date") Date date);

}