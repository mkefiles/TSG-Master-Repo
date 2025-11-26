import React, { type JSX } from "react";

type CalculatorInputProps = {
    formData: {
        initialInvestment: number;
        annualInvestment: number;
        expectedReturn: number;
        duration: number;
    };
    onInputChange(e: React.ChangeEvent<HTMLInputElement>): void;
};

const CalculatorInput = (props: CalculatorInputProps): JSX.Element => {
    return (
        <section>
            <form>
                <div className="grid-item">
                    <label className="inp-lbl" htmlFor="init-inv">
                        Initial Investment
                    </label>
                    <input
                        className="inp-fld"
                        type="number"
                        name="initialInvestment"
                        id="init-inv"
                        value={props.formData.initialInvestment}
                        onChange={props.onInputChange}
                        required
                    />
                </div>

                <div className="grid-item">
                    <label className="inp-lbl" htmlFor="ann-inv">
                        Annual Investment
                    </label>
                    <input
                        className="inp-fld"
                        type="number"
                        name="annualInvestment"
                        id="ann-inv"
                        value={props.formData.annualInvestment}
                        onChange={props.onInputChange}
                        required
                    />
                </div>

                <div className="grid-item">
                    <label className="inp-lbl" htmlFor="exp-ret">
                        Expected Return
                    </label>
                    <input
                        className="inp-fld"
                        type="number"
                        name="expectedReturn"
                        id="exp-ret"
                        value={props.formData.expectedReturn}
                        onChange={props.onInputChange}
                        required
                    />
                </div>

                <div className="grid-item">
                    <label className="inp-lbl" htmlFor="duration">
                        Duration
                    </label>
                    <input
                        className="inp-fld"
                        type="number"
                        name="duration"
                        id="duration"
                        value={props.formData.duration}
                        onChange={props.onInputChange}
                        required
                    />
                </div>
            </form>
        </section>
    );
};

export default CalculatorInput;
