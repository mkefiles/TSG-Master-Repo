package net.yorksolutions.service;

import net.yorksolutions.entity.Plans;
import net.yorksolutions.repository.PlansRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Step 000: Annotation-based Configuration
// Step 01A: Identify a Service "Bean" (Annotation)
@Service
public class PlansService {
    // DESC: Inject the following dependencies for Spring Context
    // ... to auto-wire upon initialization
    private final PlansRepository plansRepository;

    // DESC: Constructor-method to initialize all necessary dependencies
    public PlansService(PlansRepository plansRepository) {
        this.plansRepository = plansRepository;
    }

    // PT01 - Service - For createNewPlan()
    // HTTP [Method - URL]: POST - localhost:8080/api/v1/plans
    public int createNewPlan(Plans newPlan) {
        // DESC: Save new plan to DB and ensure the return is not null
        // NOTE: Additional logic could go here, however this is an
        // ... optional end-point that I am using so I can add plans
        // NOTE: save() will return the saved entity (incl. the ID)
        Plans savedPlan = plansRepository.save(newPlan);
        if (savedPlan == null) {
            return 400;
        } else {
            return 200;
        }
    }

    // PT02 - Service - For readAllPlans()
    // HTTP [Method - URL]: GET - localhost:8080/api/v1/plans
    public List<Plans> readAllPlans() {
        return plansRepository.findAll();
    }

    // PT03 - Service - For readPlanById()
    // HTTP [Method - URL]: GET - localhost:8080/api/v1/plans/{id}
    public List<Plans> readPlanById(UUID uuid) {
        // DESC: Get by ID
        // NOTE: Use Optional in event that nothing is there
        Optional<Plans> planById = plansRepository.findById(uuid);

        // DESC: Create a List to hold Plans (if applicable)
        List<Plans> planEntries = new ArrayList<>();

        // DESC: Add message from Optional to List (only if it exists)
        planById.ifPresent(planEntries::add);

        return planEntries;
    }

}
