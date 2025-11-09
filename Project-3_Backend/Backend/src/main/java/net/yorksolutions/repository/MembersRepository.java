package net.yorksolutions.repository;

import net.yorksolutions.entity.Members;
import net.yorksolutions.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MembersRepository extends JpaRepository<Members, UUID> {
    // PT04 - Repository - For createNewMemberUser()
    // HTTP [Method - URL]: POST - localhost:8080/api/v1/auth/register
    // A Custom Query is necessary to run a single-column update in lieu
    // ... of updating all values, which is not as efficient
    @Modifying
    @Query(value = "UPDATE members SET user_id = CAST(:user_id AS UUID) WHERE id = CAST(:id AS UUID)", nativeQuery = true)
    public void updateMembers(
            @Param(value = "user_id") UUID userId, @Param(value = "id") UUID id
    );

    public Optional<Members> findByUserId(Users userId);


}


/*
    // PT01 - Repository - For createNewPlan()
    // HTTP [Method - URL]: POST - localhost:8080/api/v1/plans
    // Handled by Spring Data JPA (Built-In Method(s))

    // PT02 - Repository - For readAllPlans()
    // HTTP [Method - URL]: GET - localhost:8080/api/v1/plans
    // Handled by Spring Data JPA (Built-In Method(s))

    // PT03 - Repository - For readPlanById()
    // HTTP [Method - URL]: GET - localhost:8080/api/v1/plans/{id}
    // Handled by Spring Data JPA (Built-In Method(s))
 */