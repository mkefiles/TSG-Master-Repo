package net.yorksolutions.feedbackapi.controllers;

import jakarta.validation.Valid;
import net.yorksolutions.feedbackapi.dtos.ErrorResponse;
import net.yorksolutions.feedbackapi.dtos.FeedbackRequest;
import net.yorksolutions.feedbackapi.dtos.FeedbackResponse;
import net.yorksolutions.feedbackapi.entities.FeedbackEntity;
import net.yorksolutions.feedbackapi.messaging.FeedbackEventPublisher;
import net.yorksolutions.feedbackapi.services.FeedbackService;
import net.yorksolutions.feedbackapi.services.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")

// gt get added cross origins here
@CrossOrigin(origins = "http://localhost:5173")
public class FeedbackController {

    /*
     * SECTION: Constructor-based D.I. (w/o @Autowired)
     */

    private final FeedbackService feedbackService;
    private final ValidationException validationExceptionService;
    private final FeedbackEventPublisher feedbackEventPublisher;

    public FeedbackController(
            FeedbackService feedbackService,
            ValidationException validationExceptionService,
            FeedbackEventPublisher feedbackEventPublisher
    ) {
        this.feedbackService = feedbackService;
        this.validationExceptionService = validationExceptionService;
        this.feedbackEventPublisher = feedbackEventPublisher;
    }

    /*
     * SECTION: End-Points
     */

    /**
     * Client shall provide the following JSON payload:
     * <p>
     * EX: { "memberId": "m-123", "providerName": "Dr. Smith",
     * "rating": 4, "comment": "Great experience." }
     * <p>
     * This method will validate input, interact with necessary methods
     * in the `FeedbackService` method and return the appropriate response
     * @param clientInput mapped to FeedbackRequest DTO
     * @param result the required Spring Validation mechanism
     * @return 200 and created resource | 400 and validation errors (PASS | FAIL)
     */
    // NOTE: `ResponseEntity<?>` is using the wild-card to handle returning
    // ... different DTOs based on PASS or FAIL
    // NOTE: It is *best-practice* to return a DTO ... NOT an Entity
    @PostMapping("/feedback")
    public ResponseEntity<?> createNewFeedbackEntry(
            @Valid @RequestBody FeedbackRequest clientInput,
            BindingResult result
    ) {

        /*
         * SECTION: Handle necessary Validation-error steps
         */

        if (result.hasErrors()) {
            // DESC: Convert Validation Errors to Client-friendly DTO
            ErrorResponse returnedErrRespDto = validationExceptionService
                    .convertBindingResultToErrorResponse(result);

            // DESC: Return Client-friendly DTO and HTTP Bad Request
            return ResponseEntity
                    .status(400)
                    .body(returnedErrRespDto);
        }

        /*
         * SECTION: Handle necessary Validation-pass steps
         */

        // DESC: Map `FeedbackRequest` to `FeedbackEntity`
        FeedbackEntity entityWithoutId = feedbackService
                .mapRequestToEntity(clientInput, OffsetDateTime.now());

        // DESC: Map `FeedbackEntity` to D.B. Managed Entity (i.e., persist)
        FeedbackEntity persistedEntityWithId = feedbackService
                .createNewFeedbackEntry(entityWithoutId);

        // DESC: Map `FeedbackEntity` to `FeedbackResponse` (incl. `id` and `submittedAt`)
        FeedbackResponse responseForClient = feedbackService
                .mapEntityToResponse(persistedEntityWithId);

        // TODO: POSSIBLY necessary to prevent Kafka-Publish if Persist fails

        // DESC: Handle `FeedbackResponse` (i.e., "Publish" to Kafka)
        feedbackEventPublisher.sendFeedbackEvent(responseForClient);

        return ResponseEntity
                .status(200)
                .body(responseForClient);
    }

    /**
     * Query the Feedback table by the unique ID value.
     * @param uuidAsString ID rec'd as String and converted to UUID
     * @return 200 and resource | 404 (PASS | FAIL)
     */
    @GetMapping("/feedback/{id}")
    public ResponseEntity<FeedbackResponse> readFeedbackById(
            @PathVariable(name = "id") String uuidAsString
    ) {
        // DESC: Get Entity by ID (return `null` if not available)
        // POSSIBLE FIXME: This may need to be rec'd as `Optional.of()`
        FeedbackEntity databaseReturnedEntity = feedbackService
                .getFeedbackById(UUID.fromString(uuidAsString));

        // DESC: FAILED -- Return 404
        if (databaseReturnedEntity == null) {
            return ResponseEntity
                    .status(404)
                    .build();
        }

        // DESC: SUCCESSFUL -- Return 200 with `FeedbackResponse`
        // DESC: Map `FeedbackEntity` to `FeedbackResponse` (incl. `id` and `submittedAt`)
        FeedbackResponse responseForClient = feedbackService.mapEntityToResponse(databaseReturnedEntity);

        return ResponseEntity
                .status(200)
                .body(responseForClient);
    }

    /**
     * Query Feedback table by memberId and return all results
     * @param memberId
     * @return 200 and resource (empty list if none)
     */
    @GetMapping("/feedback")
    public ResponseEntity<ArrayList<FeedbackResponse>> readAllFeedbackByMemberId(
            @RequestParam(name = "memberId") String memberId
    ) {
        // DESC: Get Entities by ID (return empty list if not available)
        ArrayList<FeedbackEntity> databaseReturnedEntities = feedbackService
                .getAllFeedbackByMemberId(memberId);

        // DESC: Map List of `FeedbackEntity` to List of `FeedbackResponse`
        ArrayList<FeedbackResponse> responseForClient = feedbackService
                .mapEntitiesToResponse(databaseReturnedEntities);

        // DESC: Returns a list will all feedback (empty if none)
        return ResponseEntity
                .status(200)
                .body(responseForClient);
    }

    /*
     * SECTION: Handle the exceptions
     */

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJackSONExceptions(
            HttpMessageNotReadableException ex
    ) {
        // DESC: Extract the 'Cause' from the Exception
        Throwable cause = ex.getCause();

        // DESC: Return appropriate JackSON Error
        // NOTE: This is, more so, a catch-all for any JackSON Errors
        // ... that are not caught by the Validator using the RegEx's
        return validationExceptionService.unreadableMessageHandler(cause);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleRatingOmittedException() {
        return validationExceptionService.ratingOmittedHandler();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataFieldsBeingOmittedException() {
        return validationExceptionService.dataFieldsOmittedHandler();
    }

    /*
     * SECTION: Override the end-point for Spring Actuator: Health
     */

    /* GET - /health - Handled by Spring Actuator */
}
