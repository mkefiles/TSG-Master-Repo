/* SECTION: React Imports */
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";

/* SECTION: React Router Imports */
import { BrowserRouter, Routes, Route } from "react-router-dom";

/* SECTION: Project Imports */
import "./index.css";
import Home from "./pages/Home/Home.tsx";
import RandomJoke from "./pages/RandomJoke/RandomJoke.tsx";
import TenJokes from "./pages/TenJokes/TenJokes.tsx";

createRoot(document.getElementById("root")!).render(
    <StrictMode>
        <BrowserRouter>
            {/* DESC: Provide all pages */}
            <Routes>
                <Route index element={<Home />} />
                <Route path="random_joke" element={<RandomJoke />} />
                <Route path="ten_jokes" element={<TenJokes />} />
            </Routes>
        </BrowserRouter>
    </StrictMode>
);
