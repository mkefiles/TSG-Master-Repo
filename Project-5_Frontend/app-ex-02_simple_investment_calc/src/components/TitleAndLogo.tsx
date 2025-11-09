import { type JSX } from "react";
import logo from "../assets/img--investment_calculator.webp";

const TitleAndLogo = (): JSX.Element => {
    return (
        <header>
            <img src={logo} alt="Calculator for Investment" />
            <h1>Investment Calculator</h1>
        </header>
    );
};

export default TitleAndLogo;
