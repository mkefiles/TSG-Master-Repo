package net.yorksolutions.repository;

import net.yorksolutions.entity.Plans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlansRepository extends JpaRepository<Plans, UUID> {
    // PT01 - Repository - For createNewPlan()
    // HTTP [Method - URL]: POST - localhost:8080/api/v1/plans
    // Handled by Spring Data JPA (Built-In Method(s))

    // PT02 - Repository - For readAllPlans()
    // HTTP [Method - URL]: GET - localhost:8080/api/v1/plans
    // Handled by Spring Data JPA (Built-In Method(s))

    // PT03 - Repository - For readPlanById()
    // HTTP [Method - URL]: GET - localhost:8080/api/v1/plans/{id}
    // Handled by Spring Data JPA (Built-In Method(s))
}
