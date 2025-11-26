package net.yorksolutions.api;

import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class ConfigurationController {

    // DESC: Initialize ApplicationEventPublisher
    // NOTE: This allows this module to communicate with other modules
    // ... by 'publishing' an event for the other module to 'consume'
    // ... in lieu of giving other modules access to unnecessary aspects
    // ... of this module (assuming the code were to be made 'internal')
    private final ApplicationEventPublisher applicationEventPublisher;

    // DESC: Constructor-based Dependency Injection
    public ConfigurationController(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Handle the creation of a new HRA Configuration.
     * <p>
     * End-Point: POST - /api/employers/{employerID}/hra-config
     * @param employerID received from URL parameter
     * @param configurationDTO the DTO to map the request-body to
     * @param userInput used to confirm user-input is valid
     * @return an HTTP 201 or 400 (if 400, add't'l functionality
     * for expected JSON structure)
     */
    @PostMapping("/api/employers/{employerID}/hra-config")
    public ResponseEntity<String> createConfiguration (
            @PathVariable("employerID") int employerID,
            @Valid @RequestBody ConfigurationDTO configurationDTO,
            BindingResult userInput
    ) {
        // DESC: Confirm valid JSON Payload
        if (userInput.hasErrors()) {
            // DESC: Create a Valid Input Example for Response
            ConfigurationDTO exampleOutput = new ConfigurationDTO(
                    "2026", 350.00
            );

            // DESC: Return Bad Request (400) and Valid Input Example
            return ResponseEntity.status(400).body(exampleOutput.validUserInput());
        } else {
            // DESC: Log configurationDto to console
            System.out.println(configurationDTO.toString());

            // DESC: Publish 'event' (see Spring Modulith: Application Events)
            // NOTE: It is common to call Application Event Publisher from a
            // ... Service file, however with this calling it from the controller
            // ... it passes the custom configuration event as the "source" parameter
            applicationEventPublisher.publishEvent(
                    new SuccessfulConfigurationEvent(
                            this, String.valueOf(employerID),  configurationDTO
                    )
            );

            // DESC: Return '201 Created' (and the created Configuration)
            return ResponseEntity.status(201).body(configurationDTO.toString());
        }
    }
}
