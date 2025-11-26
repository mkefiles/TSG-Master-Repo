package net.yorksolutions.feedbackapi.services;

import jakarta.transaction.Transactional;
import net.yorksolutions.feedbackapi.dtos.FeedbackRequest;
import net.yorksolutions.feedbackapi.dtos.FeedbackResponse;
import net.yorksolutions.feedbackapi.entities.FeedbackEntity;
import net.yorksolutions.feedbackapi.repositories.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

/**
 * Handle all necessary business logic up to and including
 * the Kafka publishing.
 */
@Service
public class FeedbackService {

    /*
     * SECTION: Constructor-based D.I. (w/o @Autowired)
     */

    private FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    /*
     * SECTION: FeedbackService Methods
     */

    public FeedbackEntity mapRequestToEntity(
            FeedbackRequest clientInput, OffsetDateTime submittedAt
    ) {
        // DESC: Map `FeedbackRequest` into `FeedbackEntity`
        FeedbackEntity convertedInput = new FeedbackEntity();

        // DESC: Initialize `FeedbackEntity` (add / incl. submittedAt)
        convertedInput.setMemberId(clientInput.getMemberId());
        convertedInput.setProviderName(clientInput.getProviderName());
        convertedInput.setRating(Integer.valueOf(clientInput.getRating()));
        convertedInput.setComment(clientInput.getComment());
        convertedInput.setSubmittedAt(submittedAt);

        return convertedInput;
    }

    @Transactional
    public FeedbackEntity createNewFeedbackEntry(
            FeedbackEntity entityWithoutId
    ){
        return feedbackRepository.save(entityWithoutId);
    }

    public FeedbackResponse mapEntityToResponse(
            FeedbackEntity returnedDatabaseEntity
    ) {
        // DESC: Map `FeedbackEntity` into `FeedbackResponse`
        FeedbackResponse convertedOutput = new FeedbackResponse(
                returnedDatabaseEntity.getId(), returnedDatabaseEntity.getMemberId(),
                returnedDatabaseEntity.getProviderName(), returnedDatabaseEntity.getRating(),
                returnedDatabaseEntity.getComment(), returnedDatabaseEntity.getSubmittedAt()
        );

        return convertedOutput;
    }

    public ArrayList<FeedbackResponse> mapEntitiesToResponse(
            ArrayList<FeedbackEntity> returnedDatabaseEntities
    ) {
        ArrayList<FeedbackResponse> convertedOutput = new ArrayList<>();

        // DESC: Short-circuit if nothing in list (i.e., return empty list)
        if (returnedDatabaseEntities.isEmpty()) {
            return convertedOutput;
        }

        // DESC: Pass each to the singular method above
        for (FeedbackEntity returnedDatabaseEntity : returnedDatabaseEntities) {
            convertedOutput.add(mapEntityToResponse(returnedDatabaseEntity));
        }

        return convertedOutput;
    }

    public FeedbackEntity getFeedbackById(UUID id) {
        Optional<FeedbackEntity> feedbackThatMayExist = feedbackRepository.findById(id);
        return feedbackThatMayExist.orElse(null);
    }

    public ArrayList<FeedbackEntity> getAllFeedbackByMemberId(
            String memberId
    ) {
        ArrayList<FeedbackEntity> returnedDatabaseEntities = feedbackRepository
                .findByMemberId(memberId);

        return returnedDatabaseEntities;
    }
}
