import React, { type JSX, useState } from "react";
import TitleAndLogo from "./TitleAndLogo.tsx";
import CalculatorInput from "./CalculatorInput.tsx";
import OutputTable from "./OutputTable.tsx";
import {
    calculateInvestmentResults,
    type calculatedInvestmentData,
} from "../utils/investment.ts";

const InvestmentCalculator = (): JSX.Element => {
    // DESC: Track State on Form-field Data
    const [formData, setFormData] = useState({
        initialInvestment: 10000,
        annualInvestment: 1200,
        expectedReturn: 6,
        duration: 10,
    });

    // DESC: Confirm User Input is valid
    // NOTE: This only validates against the `duration`
    const inputIsValid = formData.duration >= 1;

    // DESC: Event-handler for Constant Change on User-Input
    const handleInputChange = (
        e: React.ChangeEvent<HTMLInputElement>,
    ): void => {
        const { name, value } = e.target;
        setFormData((previousFormData) => {
            return {
                ...previousFormData,
                [name]: parseFloat(value),
            };
        });
    };

    // DESC: Calculate Investment Results on every Re-Render
    const investmentData: calculatedInvestmentData[] =
        calculateInvestmentResults(
            formData.initialInvestment,
            formData.annualInvestment,
            formData.expectedReturn,
            formData.duration,
        );

    return (
        <>
            <TitleAndLogo />
            <CalculatorInput
                formData={formData}
                onInputChange={handleInputChange}
            />
            {/* DESC: Conditionally render either the table OR a warning message */}
            { !inputIsValid &&
                <p>Please enter Duration greater-than zero</p>
            }
            { inputIsValid &&
                <OutputTable investmentData={investmentData} />
            }
        </>
    );
};

export default InvestmentCalculator;
