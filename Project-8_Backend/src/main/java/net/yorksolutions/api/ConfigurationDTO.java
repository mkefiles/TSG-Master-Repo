package net.yorksolutions.api;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ConfigurationDTO {

    /**
     * SECTION: DTO / POJO Variables
     */
    @NotEmpty(message = "Plan Year is Required!")
    private String planYear;

    @NotNull(message = "Monthly Allowance is Required!")
    private Double monthlyAllowance;

    /**
     * SECTION: No-Args Constructor
     */
    public ConfigurationDTO() {}

    /**
     * SECTION: All-Args Constructor
     */
    public ConfigurationDTO(String planYear, Double monthlyAllowance) {
        this.planYear = planYear;
        this.monthlyAllowance = monthlyAllowance;
    }

    /**
     * SECTION: Getter and Setter methods
     */
    public String getPlanYear() {
        return planYear;
    }

    public void setPlanYear(String planYear) {
        this.planYear = planYear;
    }

    public Double getMonthlyAllowance() {
        return monthlyAllowance;
    }

    public void setMonthlyAllowance(Double monthlyAllowance) {
        this.monthlyAllowance = monthlyAllowance;
    }

    /**
     * SECTION: Handle the String Representation
     * - This will override the 'toString' and it provides an expected
     * ... input in the event that what is provided is wrong
     */
    public String validUserInput() {
        return "A valid HRA Configuration should be passed as follows:{" +
                "planYear='" + planYear + '\'' +
                ", monthlyAllowance=" + monthlyAllowance +
                '}';
    }

    @Override
    public String toString() {
        return "ConfigurationDTO{" +
                "planYear='" + planYear + '\'' +
                ", monthlyAllowance=" + monthlyAllowance +
                '}';
    }
}
