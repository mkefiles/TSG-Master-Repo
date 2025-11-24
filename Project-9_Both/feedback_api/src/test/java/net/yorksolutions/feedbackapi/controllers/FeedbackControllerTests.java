package net.yorksolutions.feedbackapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.yorksolutions.feedbackapi.controllers.FeedbackController;
import net.yorksolutions.feedbackapi.dtos.ErrorResponse;
import net.yorksolutions.feedbackapi.dtos.FeedbackRequest;
import net.yorksolutions.feedbackapi.dtos.FeedbackResponse;
import net.yorksolutions.feedbackapi.entities.FeedbackEntity;
import net.yorksolutions.feedbackapi.messaging.FeedbackEventPublisher;
import net.yorksolutions.feedbackapi.services.FeedbackService;
import net.yorksolutions.feedbackapi.services.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// NOTE: Using `@WebMvcTest`, in lieu of `@SpringBootTest`, will only
// ... start the web layer (not the entire Spring application)
@WebMvcTest(FeedbackController.class)

// NOTE: Using `@AutoConfigureMockMvc` bypasses the need for a full
// ... server spin-up
@AutoConfigureMockMvc

@DisplayName("Tests for Feedback Controller")
public class FeedbackControllerTests {

    /**
     * SECTION: Spring-specific Dependency Injection
     * <p>
     * `@Autowired` will pull in the necessary Spring dependencies
     * for the overall test to function properly
     */

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * SECTION: Controller-specific Mocking
     * <p>
     * This ensures that the Controller itself will be instantiated
     * and all complex/external dependencies are mocked.
     */

    // DESC: Create "mocks" for necessary complex/external dependencies
    // NOTE: This is used by the end-point being tested
    @MockitoBean
    private FeedbackService stubbedFeedbackService;

    @MockitoBean
    private FeedbackEventPublisher stubbedFeedbackEventPublisher;

    @MockitoBean
    private ValidationException stubbedValidationException;

    // DESC: Create an Instance of the Class to be tested
    // NOTE: Every "Class" is ultimately testing methods within itself
    // ... therefore this will be necessary for every *Tests.java file
    @InjectMocks
    private FeedbackController mockedFeedbackController;
    @Autowired
    private FeedbackEventPublisher feedbackEventPublisher;

    // DESC: Tell Mockito to initialize all Objects annotated with @Mock
    // NOTE: @BeforeEach is used to ensure that each test has a clean
    // ... slate with new instances of necessary dependencies
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Test the Happy Path for a Successful POST")
    public void createNewFeedbackEntry_returnValidResponse_givenValidInput() throws Exception {
        /* SECTION: Given Input Arrange[ment]... */

        UUID deterministicUUID = UUID.randomUUID();
        OffsetDateTime deterministicSubmittedAt = OffsetDateTime.now();

        FeedbackRequest dummyFeedbackRequest = new FeedbackRequest(
                "123abc", "Dr. John Doe",
                "5", "A great doctor!"
        );

        FeedbackEntity expectedFeedbackEntity = new FeedbackEntity(
                deterministicUUID, "123abc", "Dr. John Doe", 5, "A great doctor!", deterministicSubmittedAt
        );

        FeedbackResponse expectedFeedbackResponse = new FeedbackResponse(
                deterministicUUID, "123abc", "Dr. John Doe", 5, "A great doctor!", deterministicSubmittedAt
        );

        Mockito.when(stubbedFeedbackService
                        .mapRequestToEntity(dummyFeedbackRequest, deterministicSubmittedAt))
                .thenReturn(expectedFeedbackEntity);

        Mockito.when(stubbedFeedbackService
                .createNewFeedbackEntry(any())).thenReturn(expectedFeedbackEntity);

        Mockito.when(stubbedFeedbackService.mapEntityToResponse(expectedFeedbackEntity))
                .thenReturn(expectedFeedbackResponse);

        Mockito.doNothing().when(feedbackEventPublisher).sendFeedbackEvent(any(FeedbackResponse.class));

        /* SECTION: When Act[ed] Upon... */

        mockMvc.perform(post("/api/v1/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyFeedbackRequest)))

        /* SECTION: Then Assert Output Is... */

                .andExpect(status().is(200))
                .andExpect(jsonPath("$.memberId")
                        .value("123abc"));
    }

    @Test
    @DisplayName("Ensure a bad memberId outputs proper validation msg.")
    public void createNewFeedbackEntry_returnValidationErr_givenBadMemberId() {
        /* SECTION: Given Input Arrange[ment]... */

        FeedbackRequest dummyFeedbackRequest = new FeedbackRequest(
                "1", "Dr. John Doe",
                "5", "A great doctor!"
        );

        BindingResult dummyBindingResult =
                new BeanPropertyBindingResult(
                        dummyFeedbackRequest, "dummyFeedbackRequest");


        dummyBindingResult.addError(
                new FieldError(
                        "dummyFeedbackRequest",
                        "memberId",
                        "memberId MUST be between 3 and 36 characters AND use alphanumeric characters (e.g., m-123)")
        );

        Map<String, String> expectedErrsAndMsgs = new HashMap<>();
        expectedErrsAndMsgs.put(
                "memberId",
                "memberId MUST be between 3 and 36 characters AND use alphanumeric characters (e.g., m-123)"
        );

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                "Errors", "Validation Failed", expectedErrsAndMsgs
        );

        Mockito.when(stubbedValidationException
                .convertBindingResultToErrorResponse(dummyBindingResult))
                .thenReturn(expectedErrorResponse);

        /* SECTION: When Act[ed] Upon... */

        ResponseEntity<?> response = mockedFeedbackController
                .createNewFeedbackEntry(dummyFeedbackRequest, dummyBindingResult);

        /* SECTION: Then Assert Output Is... */

        Assertions.assertAll(
                () -> Assertions.assertEquals(
                        400, response.getStatusCode().value()),
                () -> Assertions.assertNotNull(
                        response.getBody())
        );


    }


    /**
     * The `HttpMessageNotReadableException` error is thrown at the Web /
     * Controller layer and, as such, is caught at the surface-level. When
     * a simple JSON payload, that is broken, is passed to an end-point,
     * Spring will immediately kick-out the error before getting to the
     * internal method-logic
     * @throws Exception
     */
    @Test
    @DisplayName("Ensure broken JSON outputs HttpMessageNotReadableException")
    public void createNewFeedbackEntry_returnHttpMsgNotReadableEx_givenBadJson() throws Exception {
        /* SECTION: Given Input Arrange[ment]... */

        String brokenJson = """
                {
                    "memberId": "123abc"
                """;

        /* SECTION: When Act[ed] Upon... */

        // NOTE: Oddly enough, a throw of `HttpMessageNotReadableException` will,
        // ... under the hood, provide a HTTP Status Code of 200 so this is catching
        // ... the error on the Exception itself not the status code
        // NOTE: MockMvc does NOT prepend the `@RequestMapping("/api/v1")` annotation
        mockMvc.perform(post("/api/v1/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(brokenJson))

        /* SECTION: Then Assert Output Is... */

                .andExpect(
                        result -> assertTrue(
                                result.getResolvedException() instanceof HttpMessageNotReadableException)
                );
    }

    @Test
    @DisplayName("Ensure that the GET to read by Unique ID is successful")
    public void readFeedbackById_return200_givenValidId() throws Exception {
        /* SECTION: Given Input Arrange[ment]... */

        UUID deterministicUUID = UUID.randomUUID();
        OffsetDateTime deterministicSubmittedAtDate = OffsetDateTime.now();

        String dummyUniqueId = deterministicUUID.toString();

        FeedbackEntity expectedFeedbackEntity = new FeedbackEntity(
                deterministicUUID, "m-123",
                "Dr. Smith", 5,
                "Great experience", deterministicSubmittedAtDate
        );

        FeedbackResponse expectedFeedbackResponse = new FeedbackResponse(
                deterministicUUID, "m-123",
                "Dr. Smith", 5,
                "Great experience", deterministicSubmittedAtDate
        );

        ResponseEntity<FeedbackResponse> expectedResponseEntity =
                new ResponseEntity<>(
                        expectedFeedbackResponse,
                        HttpStatus.valueOf(200)
                );

        Mockito.when(
                stubbedFeedbackService.getFeedbackById(UUID.fromString(dummyUniqueId)))
                .thenReturn(expectedFeedbackEntity);

        Mockito.when(
                stubbedFeedbackService.mapEntityToResponse(expectedFeedbackEntity))
                .thenReturn(expectedFeedbackResponse);

        /* SECTION: When Act[ed] Upon... */

        ResponseEntity<FeedbackResponse> actualResponseEntity =
                mockedFeedbackController.readFeedbackById(dummyUniqueId);

        /* SECTION: Then Assert Output Is... */

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedResponseEntity.getStatusCode(),
                        actualResponseEntity.getStatusCode()),
                () -> Assertions.assertEquals(expectedResponseEntity.getBody(),
                        actualResponseEntity.getBody())
        );
    }

    @Test
    @DisplayName("Ensure that the GET to read by Unique ID fails")
    public void readFeedbackById_return404_givenInvalidId() throws Exception {
        /* SECTION: Given Input Arrange[ment]... */

        UUID deterministicUUID = UUID.randomUUID();

        String dummyUniqueId = deterministicUUID.toString();

        ResponseEntity<FeedbackResponse> expectedResponseEntity =
                new ResponseEntity<>(HttpStatus.valueOf(404));

        Mockito.when(
                stubbedFeedbackService.getFeedbackById(UUID.fromString(dummyUniqueId)))
                .thenReturn(null);

        /* SECTION: When Act[ed] Upon... */

        ResponseEntity<FeedbackResponse> actualResponseEntity =
                mockedFeedbackController.readFeedbackById(dummyUniqueId);

        /* SECTION: Then Assert Output Is... */

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedResponseEntity.getStatusCode(),
                        actualResponseEntity.getStatusCode())
        );
    }

    @Test
    @DisplayName("Ensure that, given valid Member ID, a list of feedback is returned")
    public void readAllFeedbackByMemberId_returnList_givenValidMemberId() {
        /* SECTION: Given Input Arrange[ment]... */

        UUID deterministicUUID = UUID.randomUUID();
        OffsetDateTime deterministicSubmittedAtDate = OffsetDateTime.now();

        String dummyMemberId = deterministicUUID.toString();

        FeedbackEntity expectedFeedbackEntity = new FeedbackEntity(
                deterministicUUID, "m-123",
                "Dr. Smith", 5,
                "Great experience", deterministicSubmittedAtDate
        );

        ArrayList<FeedbackEntity> expectedFeedbackEntities = new ArrayList<>();
        expectedFeedbackEntities.add(expectedFeedbackEntity);


        FeedbackResponse expectedFeedbackResponse = new FeedbackResponse(
                deterministicUUID, "m-123",
                "Dr. Smith", 5,
                "Great experience", deterministicSubmittedAtDate
        );

        ArrayList<FeedbackResponse> expectedFeedbackResponses = new ArrayList<>();
        expectedFeedbackResponses.add(expectedFeedbackResponse);

        ResponseEntity<ArrayList<FeedbackResponse>> expectedResponseEntity =
                new ResponseEntity<>(
                        expectedFeedbackResponses,
                        HttpStatus.valueOf(200)
                );

        Mockito.when(
                stubbedFeedbackService.getAllFeedbackByMemberId(dummyMemberId))
                .thenReturn(expectedFeedbackEntities);

        Mockito.when(
                stubbedFeedbackService.mapEntitiesToResponse(expectedFeedbackEntities))
                .thenReturn(expectedFeedbackResponses);

        /* SECTION: When Act[ed] Upon... */

        ResponseEntity<ArrayList<FeedbackResponse>> actualResponseEntity =
                mockedFeedbackController.readAllFeedbackByMemberId(dummyMemberId);

        /* SECTION: Then Assert Output Is... */

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedResponseEntity.getStatusCode(),
                        actualResponseEntity.getStatusCode()),
                () -> Assertions.assertEquals(expectedResponseEntity.getBody(),
                        actualResponseEntity.getBody())
        );
    }

    @Test
    @DisplayName(
            "Ensure that, given invalid Member ID, an empty list of feedback is returned"
    )
    public void readAllFeedbackByMemberId_returnEmptyList_givenInvalidMemberId() {
        /* SECTION: Given Input Arrange[ment]... */

        String dummyMemberId = UUID.randomUUID().toString();

        ArrayList<FeedbackEntity> expectedFeedbackEntities = new ArrayList<>();

        ArrayList<FeedbackResponse> expectedFeedbackResponses = new ArrayList<>();

        ResponseEntity<ArrayList<FeedbackResponse>> expectedResponseEntity =
                new ResponseEntity<>(
                        expectedFeedbackResponses,
                        HttpStatus.valueOf(200)
                );

        Mockito.when(
                stubbedFeedbackService.getAllFeedbackByMemberId(dummyMemberId))
                .thenReturn(expectedFeedbackEntities);

        Mockito.when(
                stubbedFeedbackService.mapEntitiesToResponse(expectedFeedbackEntities))
                .thenReturn(expectedFeedbackResponses);

        /* SECTION: When Act[ed] Upon... */

        ResponseEntity<ArrayList<FeedbackResponse>> actualResponseEntity =
                mockedFeedbackController.readAllFeedbackByMemberId(dummyMemberId);

        /* SECTION: Then Assert Output Is... */

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedResponseEntity.getStatusCode(),
                        actualResponseEntity.getStatusCode()),
                () -> Assertions.assertEquals(expectedResponseEntity.getBody(),
                        actualResponseEntity.getBody())
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