package com.effortix.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.effortix.backend.models.Employee;


import com.effortix.backend.models.TicketUpdates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketUpdatesRepository extends JpaRepository<TicketUpdates, Long> {

	 // Find all TicketUpdates related to a specific Ticket (using the ticket field)
//    List<TicketUpdates> findByTicketTId(Long tId);

    //Find all TicketUpdates related to a specific Employee (using the employee field)
    //@Query("SELECT t FROM TicketUpdates t WHERE t.employee.eId = ?1")
    @Query("SELECT tu FROM TicketUpdates tu WHERE tu.employee.eId = ?1")
    List<TicketUpdates> findByEmployeeEIdMade(Long employeeId);

    

	/*
	 * @Query("SELECT tu FROM TicketUpdates tu " + "JOIN tu.ticket t " +
	 * "WHERE tu.employee.eId = :employeeId " +
	 * "AND t.createdDate BETWEEN :startDate AND :endDate " +
	 * "AND t.deadline BETWEEN :startDate AND :endDate")
	 */
    
    @Query("SELECT tu FROM TicketUpdates tu " +
            "JOIN tu.ticket t " +
            "WHERE tu.employee.eId = :employeeId " +
            "AND :date BETWEEN t.createdDate AND t.deadline")
     List<TicketUpdates> findByEmployeeAndDateInRange(
             @Param("employeeId") Long employeeId,
             @Param("date") Date date);



    @Query("SELECT tu FROM TicketUpdates tu WHERE tu.ticket.tId = :ticketId AND tu.employee.eId = :employeeId AND tu.project.pId = :projectId")
    List<TicketUpdates> findByTicketIdAndEmployeeIdAndProjectId(@Param("ticketId") Long ticketId, @Param("employeeId") Long employeeId, @Param("projectId") Long projectId);

    
 
}


    // Find all TicketUpdates related to a specific Project (using the project field)
//    List<TicketUpdates> findByProjectPId(Long projectId);
//
//    // Find TicketUpdates records within a date range
//    List<TicketUpdates> findByDateTimeBetween(Date startDate, Date endDate);
//
//    // Find TicketUpdates records by FileLink (e.g., searching for tickets with a specific file link)
//    List<TicketUpdates> findByFileLink(String fileLink);
//
//    // Count the number of TicketUpdates for a specific Ticket (using the ticket field)
//    long countByTicketTId(Long tId);
//
//    // Count the number of TicketUpdates for a specific Employee (using the employee field)
//    long countByEmployeeEId(Long eId);
//
//    // Count the number of TicketUpdates for a specific Project (using the project field)
//    long countByProjectPId(Long pId);
