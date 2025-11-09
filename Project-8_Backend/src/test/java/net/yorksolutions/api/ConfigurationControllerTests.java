package net.yorksolutions.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

// NOTE: Using `@WebMvcTest`, in lieu of `@SpringBootTest`, will only
// ... start the web layer (not the entire Spring application)
@WebMvcTest(ConfigurationController.class)

// NOTE: Using `@AutoConfigureMockMvc` bypasses the need for a full
// ... server spin-up
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for the Configuration Controller")
public class ConfigurationControllerTests {

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
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    // DESC: Create an Instance of the Class to be tested
    // NOTE: Every "Class" is ultimately testing methods within itself
    // ... therefore this will be necessary for every *Tests.java file
    @InjectMocks
    private ConfigurationController configurationController;

    // DESC: Tell Mockito to initialize all Objects annotated with @Mock
    // NOTE: @BeforeEach is used to ensure that each test has a clean
    // ... slate with new instances of necessary dependencies
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * SECTION: Unit-Tests
     */
    @Test
    @DisplayName("Test New Configuration End-Point w/ Invalid Data")
    public void createConfiguration_return400_onInvalidInput() throws Exception {
        // Arrange
        // NOTE: Mocking the BindingResult is unnecessary because BindingResult
        // ... simply holds the results of the data-binding/validation and with
        // ... the 'invalid input' being passed directly, the BindingResult should,
        // ... in theory, run it against the ConfigurationDTO
        // NOTE: By cross-referencing with the Validators in ConfigurationDTO, you
        // ... will see that an empty string, for planYear, and a null value, for
        // ... monthlyAllowance, is unacceptable
        int testEmployerId = 301;
        ConfigurationDTO testConfigurationDTO = new ConfigurationDTO();
        testConfigurationDTO.setPlanYear("");
        testConfigurationDTO.setMonthlyAllowance(null);

        // Act
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders.post(
                                "/api/employers/{employerID}/hra-config", testEmployerId
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testConfigurationDTO)));

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
