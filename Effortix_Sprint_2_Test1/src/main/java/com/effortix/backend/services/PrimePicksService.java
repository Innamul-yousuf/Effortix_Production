package com.effortix.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.effortix.backend.models.PrimePicks;
import com.effortix.backend.models.Project;
import com.effortix.backend.repository.PrimePicksRepository;
import com.effortix.backend.repository.ProjectRepository;

@Service
public class PrimePicksService {

    @Autowired
    private PrimePicksRepository primePicksRepository;

    // Fetch all PrimePicks with 'Active' status
    public List<PrimePicks> getActivePrimePicks() {
        return primePicksRepository.findByStatus("Active");
    }
    
 // Create a new PrimePicks record
    public PrimePicks createPrimePicks(PrimePicks primePicks) {
        return primePicksRepository.save(primePicks);
    }
}