
import './App.css'

import { Routes, Route } from "react-router-dom";

import SubmitFeedbackPage from './pages/SubmitFeedbackPage/SubmitFeedbackPage'
import Nav from './Nav/Nav'
import FindFeedbackPage from './pages/FindFeedbackPage/FindFeedbackPage';
import HomePage from './pages/HomePage/HomePage';

function App() {

  return (
<div className="min-h-screen bg-gradient-to-br from-white to-[#279CF5]">

      <HomePage/>
      <Nav />
      <Routes>
        <Route path="/" />
        <Route path="/feedbackForm" element={<SubmitFeedbackPage />} />
        <Route path="/findFeedback" element={<FindFeedbackPage />} />
      </Routes>
    </div>
  )
}

export default App
