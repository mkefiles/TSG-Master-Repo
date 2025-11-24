import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { BrowserRouter } from 'react-router-dom'
import { FeedbackProvider } from './Mocking/FeedbackContext.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
    {/* FeedbackProvider can probably be deleted after mocking is done */}
    <FeedbackProvider>
    <App />
    </FeedbackProvider>
    </BrowserRouter>
  </StrictMode>
)
