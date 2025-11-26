package net.yorksolutions.feedbackapi.services;

import net.yorksolutions.feedbackapi.dtos.FeedbackRequest;
import net.yorksolutions.feedbackapi.dtos.FeedbackResponse;
import net.yorksolutions.feedbackapi.entities.FeedbackEntity;
import net.yorksolutions.feedbackapi.repositories.FeedbackRepository;
import net.yorksolutions.feedbackapi.services.FeedbackService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for Feedback (Service Layer)")
public class FeedbackServiceTests {

    /*
     * SECTION: Service / Repository Mocking
     */

    // DESC: Initialize 'stubs' for external/complex dependencies
    @Mock
    private FeedbackRepository stubbedFeedbackRepository;

    // DESC: Initialize subject-file 'mock'
    @InjectMocks
    private FeedbackService mockedFeedbackService;

    // DESC: Pull in all 'stubs' before each test
    // NOTE: This provides a 'clean slate' for each test
    // ... in lieu of re-using a 'stub' across the board
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Ensure proper DTO-to-Entity conversion")
    public void mapRequestToEntity_returnFeedbackEntity_givenValidRequest() {
        /* SECTION: Given Input Arrange[ment]... */

        FeedbackRequest dummyFeedbackRequest = new FeedbackRequest(
                "m-123", "Dr. Smith",
                "5", "Great experience"
        );
        OffsetDateTime deterministicSubmittedAtDate = OffsetDateTime.now();

        FeedbackEntity expectedFeedbackEntity = new FeedbackEntity(
                dummyFeedbackRequest.getMemberId(), dummyFeedbackRequest.getProviderName(),
                Integer.valueOf(dummyFeedbackRequest.getRating()), dummyFeedbackRequest.getComment()
        );
        expectedFeedbackEntity
                .setSubmittedAt(deterministicSubmittedAtDate);

        /* SECTION: When Act[ed] Upon... */

        FeedbackEntity actualReturnedValue = mockedFeedbackService
                .mapRequestToEntity(dummyFeedbackRequest, deterministicSubmittedAtDate);

        /* SECTION: Then Assert Output Is... */

        // NOTE: This is testing against all properties within the subject
        // ... Object because there is no unique ID-field to compare against
        Assertions.assertAll("FeedbackEntity Properties",
                () -> Assertions.assertSame(expectedFeedbackEntity.getMemberId(), actualReturnedValue.getMemberId()),
                () -> Assertions.assertSame(expectedFeedbackEntity.getProviderName(), actualReturnedValue.getProviderName()),
                () -> Assertions.assertSame(expectedFeedbackEntity.getRating(), actualReturnedValue.getRating()),
                () -> Assertions.assertSame(expectedFeedbackEntity.getComment(), actualReturnedValue.getComment()),
                () -> Assertions.assertSame(expectedFeedbackEntity.getSubmittedAt(), actualReturnedValue.getSubmittedAt())
        );
    }

    @Test
    @DisplayName("Ensure Repo. `.save()` success (incl. stubbing the Feedback Repo.)")
    public void createNewFeedbackEntry_returnFeedbackEntity_onRepoSave() {
        /* SECTION: Given Input Arrange[ment]... */

        FeedbackEntity dummyFeedbackEntity = new FeedbackEntity(
                "m-123", "Dr. Smith",
                5, "Great experience"
        );
        OffsetDateTime deterministicSubmittedAtDate = OffsetDateTime.now();
        dummyFeedbackEntity.setSubmittedAt(deterministicSubmittedAtDate);

        UUID deterministicUniqueEntityId = UUID.randomUUID();
        FeedbackEntity expectedFeedbackEntity = new FeedbackEntity(
                deterministicUniqueEntityId, "m-123",
                "Dr. Smith", 5,
                "Great experience", deterministicSubmittedAtDate
        );

        Mockito.when(stubbedFeedbackRepository.save(dummyFeedbackEntity))
                .thenReturn(expectedFeedbackEntity);

        /* SECTION: When Act[ed] Upon... */

        FeedbackEntity actualFeedbackEntity = mockedFeedbackService.createNewFeedbackEntry(dummyFeedbackEntity);

        /* SECTION: Then Assert Output Is... */

        Assertions.assertSame(expectedFeedbackEntity, actualFeedbackEntity);
    }


    @Test
    @DisplayName("Ensure `FeedbackEntity` properly maps to `FeedbackResponse`")
    public void mapEntityToResponse_returnFeedbackResponse_onValidRun() {
        /* SECTION: Given Input Arrange[ment]... */

        OffsetDateTime deterministicSubmittedAtDate = OffsetDateTime.now();
        UUID deterministicUniqueEntityId = UUID.randomUUID();

        FeedbackEntity dummyFeedbackEntity = new FeedbackEntity(
                deterministicUniqueEntityId, "m-123",
                "Dr. Smith", 5,
                "Great experience",  deterministicSubmittedAtDate
        );

        FeedbackResponse expectedFeedbackResponse = new FeedbackResponse(
                deterministicUniqueEntityId, "m-123",
                "Dr. Smith", 5,
                "Great experience", deterministicSubmittedAtDate
        );

        /* SECTION: When Act[ed] Upon... */

        FeedbackResponse actualFeedbackResponse = mockedFeedbackService
                .mapEntityToResponse(dummyFeedbackEntity);

        /* SECTION: Then Assert Output Is... */

        Assertions.assertEquals(expectedFeedbackResponse, actualFeedbackResponse);
    }

    @Test
    @DisplayName(
            "Ensure ArrayList of `FeedbackEntity` objs. properly maps to ArrayList of `FeedbackResponse` objs"
    )
    public void mapEntitiesToResponse_returnFeedbackResponses_onValidRun() {
        /* SECTION: Given Input Arrange[ment]... */

        OffsetDateTime deterministicSubmittedAtDate01 = OffsetDateTime.now();
        UUID deterministicUniqueEntityId01 = UUID.randomUUID();
        FeedbackEntity dummyFeedbackEntity01 = new FeedbackEntity(
                deterministicUniqueEntityId01, "m-123",
                "Dr. Smith", 5,
                "Great experience",  deterministicSubmittedAtDate01
        );

        OffsetDateTime deterministicSubmittedAtDate02 = OffsetDateTime.now();
        UUID deterministicUniqueEntityId02 = UUID.randomUUID();
        FeedbackEntity dummyFeedbackEntity02 = new FeedbackEntity(
                deterministicUniqueEntityId02, "m-123",
                "Dr. Jones", 2,
                "Wait time was ridiculous... one star for being nice",
                deterministicSubmittedAtDate02
        );

        ArrayList<FeedbackEntity> dummyFeedbackEntityList = new ArrayList<>();
        dummyFeedbackEntityList.add(dummyFeedbackEntity01);
        dummyFeedbackEntityList.add(dummyFeedbackEntity02);

        FeedbackResponse expectedFeedbackResponse01 = new FeedbackResponse(
                deterministicUniqueEntityId01, "m-123",
                "Dr. Smith", 5,
                "Great experience",  deterministicSubmittedAtDate01
        );

        FeedbackResponse expectedFeedbackResponse02 = new FeedbackResponse(
                deterministicUniqueEntityId02, "m-123",
                "Dr. Jones", 2,
                "Wait time was ridiculous... one star for being nice",
                deterministicSubmittedAtDate02
        );
        ArrayList<FeedbackResponse> expectedFeedbackResponses = new ArrayList<>();
        expectedFeedbackResponses.add(expectedFeedbackResponse01);
        expectedFeedbackResponses.add(expectedFeedbackResponse02);

        /* SECTION: When Act[ed] Upon... */

        ArrayList<FeedbackResponse> actualFeedbackResponses =
                mockedFeedbackService.mapEntitiesToResponse(dummyFeedbackEntityList);

        /* SECTION: Then Assert Output Is... */

        Assertions.assertEquals(expectedFeedbackResponses, actualFeedbackResponses);
    }

    @Test
    @DisplayName(
            "Ensure empty ArrayList of `FeedbackEntity` objs. properly maps to empty ArrayList of `FeedbackResponse` objs"
    )
    public void mapEntitiesToResponse_returnEmptyFeedbackResponseArr_onValidRun() {
        /* SECTION: Given Input Arrange[ment]... */

        ArrayList<FeedbackEntity> dummyFeedbackEntityList = new ArrayList<>();

        ArrayList<FeedbackResponse> expectedFeedbackResponses = new ArrayList<>();

        /* SECTION: When Act[ed] Upon... */

        ArrayList<FeedbackResponse> actualFeedbackResponses =
                mockedFeedbackService.mapEntitiesToResponse(dummyFeedbackEntityList);

        /* SECTION: Then Assert Output Is... */

        Assertions.assertEquals(expectedFeedbackResponses, actualFeedbackResponses);
    }

    @Test
    @DisplayName("Ensure Repo. `.findById()` success (incl. stubbing the Feedback Repo.)")
    public void getFeedbackById_returnFeedbackEntity_givenUniqueId() {
        /* SECTION: Given Input Arrange[ment]... */

        UUID deterministicUniqueEntityId = UUID.randomUUID();

        UUID dummyUniqueEntityId = deterministicUniqueEntityId;

        FeedbackEntity expectedFeedbackEntity = new FeedbackEntity(
                deterministicUniqueEntityId, "m-123",
                "Dr. Smith", 5,
                "Great experience", OffsetDateTime.now()
        );

        Mockito.when(stubbedFeedbackRepository.findById(dummyUniqueEntityId))
                .thenReturn(Optional.of(expectedFeedbackEntity));

        /* SECTION: When Act[ed] Upon... */

        FeedbackEntity actualFeedbackEntity = mockedFeedbackService
                .getFeedbackById(dummyUniqueEntityId);

        /* SECTION: Then Assert Output Is... */

        Assertions.assertEquals(expectedFeedbackEntity, actualFeedbackEntity);
    }

    @Test
    @DisplayName("Ensure Repo. `.findById()` fail (incl. stubbing the Feedback Repo.)")
    public void getFeedbackById_returnNull_givenUniqueId() {
        /* SECTION: Given Input Arrange[ment]... */

        UUID dummyUniqueEntityId = UUID.randomUUID();

        Mockito.when(stubbedFeedbackRepository.findById(dummyUniqueEntityId))
                .thenReturn(Optional.empty());

        /* SECTION: When Act[ed] Upon... */

        FeedbackEntity actualFeedbackEntity = mockedFeedbackService
                .getFeedbackById(dummyUniqueEntityId);

        /* SECTION: Then Assert Output Is... */

        Assertions.assertNull(actualFeedbackEntity);
    }

    @Test
    @DisplayName(
            "Ensure Repo. `.findByMemberId()` success (incl. stubbing the Feedback Repo.)"
    )
    public void getAllFeedbackByMemberId_returnFeedbackEntities_givenMemberId() {
        /* SECTION: Given Input Arrange[ment]... */

        String dummyMemberId = "m-123";

        UUID deterministicUniqueEntityIdOne = UUID.randomUUID();
        OffsetDateTime deterministicSubmittedAtDateOne = OffsetDateTime.now();

        FeedbackEntity dummyFeedbackEntityOne = new FeedbackEntity(
                deterministicUniqueEntityIdOne, "m-123",
                "Dr. Smith", 5,
                "Great experience", deterministicSubmittedAtDateOne
        );

        UUID deterministicUniqueEntityIdTwo = UUID.randomUUID();
        OffsetDateTime deterministicSubmittedAtDateTwo = OffsetDateTime.now();

        FeedbackEntity dummyFeedbackEntityTwo = new FeedbackEntity(
                deterministicUniqueEntityIdTwo, "m-123",
                "Dr. Jones", 1,
                "Told me I was fine. I went in pain and left in pain...", deterministicSubmittedAtDateTwo
        );

        ArrayList<FeedbackEntity> expectedFeedbackEntities = new ArrayList<FeedbackEntity>();
        expectedFeedbackEntities.add(dummyFeedbackEntityOne);
        expectedFeedbackEntities.add(dummyFeedbackEntityTwo);

        Mockito.when(stubbedFeedbackRepository.findByMemberId(dummyMemberId))
                .thenReturn(expectedFeedbackEntities);

        /* SECTION: When Act[ed] Upon... */

        ArrayList<FeedbackEntity> actualFeedbackEntities = mockedFeedbackService
                .getAllFeedbackByMemberId(dummyMemberId);

        /* SECTION: Then Assert Output Is... */

        Assertions.assertArrayEquals(
                expectedFeedbackEntities.toArray(), actualFeedbackEntities.toArray()
        );
    }

    @Test
    @DisplayName("Ensure Repo. `.findByMemberId()` fail (incl. stubbing the Feedback Repo.)")
    public void getAllFeedbackByMemberId_returnEmptyArrayList_givenMemberId() {
        /* SECTION: Given Input Arrange[ment]... */

        String dummyMemberId = "m-123";

        ArrayList<FeedbackEntity> expectedFeedbackEntities = new ArrayList<FeedbackEntity>();

        Mockito.when(stubbedFeedbackRepository.findByMemberId(dummyMemberId))
                .thenReturn(expectedFeedbackEntities);

        /* SECTION: When Act[ed] Upon... */

        ArrayList<FeedbackEntity> actualFeedbackEntities = mockedFeedbackService
                .getAllFeedbackByMemberId(dummyMemberId);

        /* SECTION: Then Assert Output Is... */

        Assertions.assertArrayEquals(
                expectedFeedbackEntities.toArray(), actualFeedbackEntities.toArray()
        );
    }
}

//    @Test
//    @DisplayName("...")
//    public void funcName_returnExpectation_expectedOutput() {
//        /* SECTION: Given Input Arrange[ment]... */
//
//        /* SECTION: When Act[ed] Upon... */
//
//        /* SECTION: Then Assert Output Is... */
//    }




