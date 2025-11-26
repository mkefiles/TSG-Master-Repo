package net.yorksolutions.repository;

import net.yorksolutions.entity.Enrollments;
import net.yorksolutions.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnrollmentsRepository extends JpaRepository<Enrollments, UUID> {

    public Optional<Enrollments> findByMemberId(Members memberId);

    @Modifying
    @Query(value = "UPDATE enrollments SET effective_end = :effective_end WHERE id = CAST(:id AS UUID)", nativeQuery = true)
    public void updateEnrollments(
            @Param(value = "effective_end") LocalDate effectiveEnd, @Param(value = "id")  UUID id
            );
}