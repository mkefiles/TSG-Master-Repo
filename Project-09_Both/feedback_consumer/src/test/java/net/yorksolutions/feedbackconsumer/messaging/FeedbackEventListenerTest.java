package net.yorksolutions.feedbackconsumer.messaging;

import net.yorksolutions.feedbackconsumer.messaging.FeedbackEvent;
import net.yorksolutions.feedbackconsumer.messaging.FeedbackEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackEventListenerTest {

    private FeedbackEventListener listener;

    @BeforeEach
    void setUp() {
        listener = new FeedbackEventListener();
    }

    @Test
    void handleFeedbackSubmitted_whenValidEvent_doesNotThrow() {
        // Arrange
        FeedbackEvent event = new FeedbackEvent(
                UUID.randomUUID(),
                "member-123",
                "Dr. Strange",
                5,
                "Magical service!",
                OffsetDateTime.now(),
                "1.0.0"
        );

        // Act + Assert
        assertDoesNotThrow(() ->
                listener.handleFeedbackSubmitted(event)
        );
    }

    @Test
    void handleFeedbackSubmitted_whenNullEvent_doesNotThrow() {
        assertDoesNotThrow(() ->
                listener.handleFeedbackSubmitted(null)
        );
    }
}
