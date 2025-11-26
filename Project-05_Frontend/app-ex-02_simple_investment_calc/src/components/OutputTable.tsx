import { type JSX } from "react";
import OutputData from "./OutputData.tsx";
import type { calculatedInvestmentData } from "../utils/investment.ts";

type OutputTableProps = {
    investmentData: calculatedInvestmentData[];
};

const OutputTable = (props: OutputTableProps): JSX.Element => {
    return (
        <main>
            <table>
                <thead>
                    <tr>
                        <th>Year</th>
                        <th>Investment Value</th>
                        <th>Interest (Year)</th>
                        <th>Total Interest</th>
                        <th>Invested Capital</th>
                    </tr>
                </thead>
                <OutputData investmentData={props.investmentData} />
            </table>
        </main>
    );
};

export default OutputTable;
