package net.yorksolutions.feedbackapi.repositories;

import net.yorksolutions.feedbackapi.entities.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, UUID> {

    /*
     * SECTION: Database Query Methods
     */

    /* createNewFeedbackEntry() --> save() - Handled by built-in JPA methods */

    /**
     * A JPA Derived Query to get all feedback by memberId
     * @param memberId
     * @return an ArrayList (empty if none present)
     */
    ArrayList<FeedbackEntity> findByMemberId(String memberId);
}
