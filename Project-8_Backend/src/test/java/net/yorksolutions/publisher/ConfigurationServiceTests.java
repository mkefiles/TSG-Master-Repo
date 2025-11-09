package net.yorksolutions.publisher;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for the RabbitMQ Publisher")
public class ConfigurationServiceTests {
    /**
     * SECTION: Java-specific Dependencies
     */
    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream originalOut = System.out;

    /**
     * SECTION: Controller-specific Mocking
     * <p>
     * This ensures that the Controller itself will be instantiated
     * and all complex/external dependencies are mocked.
     */
    // DESC: Create "mocks" for necessary complex/external dependencies
    @Mock
    private RabbitTemplate rabbitTemplate;

    // DESC: Create an Instance of the Class to be tested
    // NOTE: Every "Class" is ultimately testing methods within itself
    // ... therefore this will be necessary for every *Tests.java file
    @InjectMocks
    private ConfigurationService configurationService;

    // DESC: Tell Mockito to initialize all Objects annotated with @Mock
    // NOTE: @BeforeEach is used to ensure that each test has a clean
    // ... slate with new instances of necessary dependencies
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    /**
     * SECTION: Unit-Tests
     */
    @Test
    @DisplayName("Test that convertAndSend is called w/ correct arguments")
    public void publishMessageToQueue_returnConsole_onValidInput() {
        // Arrange
        String testInputMessage = "Test Message";
        String expectedOutput = "Message was published!\n";

        // Act
        configurationService.publishMessageToQueue(testInputMessage);

        // Assert
        Assertions.assertThat(expectedOutput).isEqualTo(outContent.toString());
    }

}
