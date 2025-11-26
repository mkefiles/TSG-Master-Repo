package net.yorksolutions.consumer;

import net.yorksolutions.publisher.ConfigurationMessageDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for RabbitMQ Listener")
public class ConfigurationListenerTests {

    // NOTE: Nesting the Listener Test in the event that
    // ... add't'l tests were desired as I would not want the
    // ... effects of @BeforeEach and @AfterEach to alter other
    // ... tests
    @Nested
    class TestListenerOutput {
        /**
         * SECTION: Java-specific Dependencies
         */
        private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        private PrintStream originalOut = System.out;

        /**
         * SECTION: Pre- and Post-Run Adjustments
         * <p>
         * With the Java-specific dependencies, this needs to temporarily
         * redirect the standard System.out functionality in order to test
         * the 'logged' output against the input message.
         *
         */
        @BeforeEach
        public void redirectStreams() {
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
        @DisplayName("Test Logged Output w/ Mocked Input Message")
        public void receiveMessage_returnOutputLog_onReceiptOfMessage() {
            // Arrange
            // DESC: Create a dummy-instance of the ConfigurationMessageDTO
            ConfigurationMessageDTO configurationMessageDTO = new ConfigurationMessageDTO(
                    UUID.randomUUID(), Instant.now(), "301",
                    "2026", 350.00
            );

            // DESC: Create a String-Repr. of the ConfigurationDTO
            String mockedInputMessage = configurationMessageDTO.toString();

            // DESC: Create the expected Logged output
            // NOTE: It needs the ending new-line to match the `.println()`
            String expectedOutputMessage = "OPS NOTIFICATION: "
                    + configurationMessageDTO.toString() + "\n";

            // Act
            // DESC: Initiate the ConfigurationListener to handle the
            // ... Consumer logic
            ConfigurationListener configurationListener = new ConfigurationListener();
            configurationListener.receiveMessage(mockedInputMessage);

            // Assert
            Assertions.assertThat(expectedOutputMessage).isEqualTo(outContent.toString());
        }
    }
}
