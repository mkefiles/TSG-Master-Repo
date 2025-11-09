package net.yorksolutions.repository;

import net.yorksolutions.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID> {
    // PT04 - Repository - For createNewMemberUser()
    // HTTP [Method - URL]: POST - localhost:8080/api/v1/auth/register
    // Spring Data JPA will AUTO-GENERATE the logic
    public boolean existsByEmailIgnoreCase(String email);

    // DESC: This is necessary for the `UserDetailsService` override
    // ... handled by the `CustomUserDetailsService`
    public Optional<Users> findByEmailIgnoreCase(String email);
}

/*
    // PT01 - Repository - For createNewPlan()
    // HTTP [Method - URL]: POST - localhost:8080/api/v1/plans
    // Handled by Spring Data JPA (Built-In Method(s))
 */