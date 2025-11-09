package net.yorksolutions.service;

import net.yorksolutions.entity.Enrollments;
import net.yorksolutions.entity.Members;
import net.yorksolutions.entity.Plans;
import net.yorksolutions.entity.Users;
import net.yorksolutions.repository.EnrollmentsRepository;
import net.yorksolutions.repository.MembersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class EnrollmentsService {
    // DESC: Inject the following dependencies for Spring Context
    // ... to auto-wire upon initialization
    private final MembersRepository membersRepository;
    private final EnrollmentsRepository enrollmentsRepository;

    // DESC: Constructor-method to initialize all necessary dependencies
    public EnrollmentsService(
            MembersRepository membersRepository, EnrollmentsRepository enrollmentsRepository
    ) {
        this.membersRepository = membersRepository;
        this.enrollmentsRepository = enrollmentsRepository;
    }

    @Transactional
    public Enrollments createNewEnrollment(Users tempUser, Plans tempPlan) {
        // DESC: Locate Member ID based on User ID
        Optional<Members> currentMemberOptional =
                membersRepository.findByUserId(tempUser);

        // DESC: Ensure 'Optional' value returned is not null
        if (currentMemberOptional.isPresent()) {
            Enrollments enrollments = new Enrollments();

            // DESC: Pull the Members object out of the Optional object
            Members currentMember = currentMemberOptional.get();
            enrollments.setMemberId(currentMember);

            // DESC: Set the plan id for the proposed new enrollment
            enrollments.setPlanId(tempPlan);

            // DESC: Set the start date for the proposed new enrollment
            enrollments.setEffectiveStart(LocalDate.now());

            // DESC: Check if enrollment, with Member ID, already exists
            // NOTE: If a current enrollment exists, then update the end-date
            // ... to todays date then create the new enrollment, otherwise,
            // ... create a new enrollment
            Optional<Enrollments> existingEnrollmentOptional =
                    enrollmentsRepository.findByMemberId(currentMember);

            if (existingEnrollmentOptional.isPresent()) {
                // NOTE: This suggests that a current enrollment exists
                // DESC: Pull the Enrollments object out of the Optional object
                Enrollments existingEnrollment = existingEnrollmentOptional.get();

                // DESC: Set the effective end-date to today
                // NOTE: This will 'end' their current enrollment to
                // ... make way for the new enrollment
                existingEnrollment.setEffectiveEnd(LocalDate.now());

                System.out.println("EFFECTIVE END DATE: " + existingEnrollment.getEffectiveEnd());
                System.out.println("ENROLLMENT ID: " + existingEnrollment.getId());

                // DESC: Update the database for the revised
                // ... old enrollment data
                enrollmentsRepository.updateEnrollments(
                        existingEnrollment.getEffectiveEnd(), existingEnrollment.getId()
                );
            }

            // DESC: Add the newer enrollment to the database
            enrollmentsRepository.save(enrollments);

            return enrollments;
        } else {
            // DESC: Return an empty Enrollment (member was not found by ID)
            return new Enrollments();
        }
    }
}