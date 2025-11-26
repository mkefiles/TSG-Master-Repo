package net.yorksolutions.api;

import org.springframework.context.ApplicationEvent;

public class SuccessfulConfigurationEvent extends ApplicationEvent {
    private final String employerId;
    private final ConfigurationDTO configurationDTO;

    // DESC: Create a Custom Application Event (to feed to the Publisher)
    public SuccessfulConfigurationEvent(
            Object source, String employerId, ConfigurationDTO configurationDTO
    ) {
        super(source);
        this.employerId = employerId;
        this.configurationDTO = configurationDTO;
    }

    public String getEmployerId() {
        return employerId;
    }

    public ConfigurationDTO getConfigurationDTO() {
        return configurationDTO;
    }
}
