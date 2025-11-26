package net.yorksolutions.repository;

import net.yorksolutions.entity.Claims;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClaimsRepository extends JpaRepository<Claims, UUID> {

}
