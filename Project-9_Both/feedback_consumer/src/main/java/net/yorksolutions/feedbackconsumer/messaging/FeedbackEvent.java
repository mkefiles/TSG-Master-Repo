package net.yorksolutions.feedbackconsumer.messaging;

import java.time.OffsetDateTime;
import java.util.UUID;

public record FeedbackEvent(
        UUID id,
        String memberId,
        String providerName,
        Integer rating,
        String comment,
        OffsetDateTime submittedAt,
        String schemaVersion
) {}

