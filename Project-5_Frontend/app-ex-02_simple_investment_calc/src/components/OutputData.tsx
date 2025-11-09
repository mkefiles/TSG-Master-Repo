import { type JSX } from "react";
import {
    formatter,
    type calculatedInvestmentData,
} from "../utils/investment.ts";

type OutputDataProps = {
    investmentData: calculatedInvestmentData[];
};

const OutputData = (props: OutputDataProps): JSX.Element => {
    console.log(props);

    const initialInvestment =
        props.investmentData[0].valueEndOfYear -
        props.investmentData[0].interest -
        props.investmentData[0].annualInvestment;
    return (
        <tbody>
            {/* START: Dynamically Populate/Create the `<tr>` and `<td>` tags */}
            {props.investmentData.map(
                (cId: calculatedInvestmentData): JSX.Element => {
                    const totalInterest =
                        cId.valueEndOfYear -
                        cId.annualInvestment * cId.year -
                        initialInvestment;
                    const totalAmountInvested =
                        cId.valueEndOfYear - totalInterest;
                    return (
                        <tr key={cId.year}>
                            <td>{cId.year}</td>
                            <td>{formatter.format(cId.valueEndOfYear)}</td>
                            <td>{formatter.format(cId.interest)}</td>
                            <td>{formatter.format(totalInterest)}</td>
                            <td>{formatter.format(totalAmountInvested)}</td>
                        </tr>
                    );
                },
            )}
            {/* END: Dynamically Populate/Create the `<tr>` and `<td>` tags */}
        </tbody>
    );
};

export default OutputData;
