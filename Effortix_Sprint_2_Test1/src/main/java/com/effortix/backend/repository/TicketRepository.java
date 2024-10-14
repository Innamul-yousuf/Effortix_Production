package com.effortix.backend.repository;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.effortix.backend.models.Employee;
import com.effortix.backend.models.Ticket;

import jakarta.transaction.Transactional;

@Repository
public
interface TicketRepository extends JpaRepository<Ticket, Long> {
	 
	List<Ticket> findByFromEmployeeEId(Long employeeId);
    List<Ticket> findByProjectPId(Long projectId);
    List<Ticket> findBytStatus(String status);
    List<Ticket> findBytType(String type);
    List<Ticket> findBytFlag(int tFlag);
    
    @Query("SELECT t FROM Ticket t WHERE t.toEmployee.eId = :employeeId AND t.tFlag = :flag")
    List<Ticket> findByEmployeeIdAndFlag(@Param("employeeId") Long employeeId, @Param("flag") int flag);
    
    @Query("SELECT t FROM Ticket t WHERE t.tType = :type AND t.tFlag = :flag")
    List<Ticket> findByTypeAndTFlag(String type, int flag);
    
    @Query("SELECT t FROM Ticket t WHERE FUNCTION('DATE', t.deadline) BETWEEN :monday AND :friday AND t.tType = 'Fun Friday'")
    List<Ticket> findFunFridayTicketsWithinWeek(@Param("monday") String mondayFormatted, @Param("friday") String fridayFormatted);

    
    @Modifying
    @Transactional
    @Query("UPDATE Ticket t SET t.tFlag = :flag WHERE t.tId = :tId")
    int updateTicketFlag(@Param("tId") Long tId, @Param("flag") int flag);
}