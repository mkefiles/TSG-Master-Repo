import { createContext, useContext, useState } from 'react';

//  Create a new context
const FeedbackContext = createContext();

// Custom hook for easy access
export function useFeedback() {
  return useContext(FeedbackContext);
}

// Provider component that wraps your app
export function FeedbackProvider({ children }) {
  // Global mock data
  const [feedbackList, setFeedbackList] = useState([
    {
      id: '1',
      memberId: 'm-123',
      providerName: 'Dr. Smith',
      rating: 4,
      comment: 'Great experience!',
      submittedAt: '2025-11-12T12:00:00Z'
    },
    {
      id: '2',
      memberId: 'm-999',
      providerName: 'Dr. Lee',
      rating: 5,
      comment: 'Very friendly staff!',
      submittedAt: '2025-11-12T13:00:00Z'
    },
    {
      id: '3',
      memberId: 'm-777',
      providerName: 'Dr. Taylor',
      rating: 3,
      comment: 'Wait time was long but overall fine.',
      submittedAt: '2025-11-12T14:30:00Z'
    },
    {
      id: '4',
      memberId: 'm-123',
      providerName: 'Dr. Banner',
      rating: 5,
      comment: 'Top-notch care!',
      submittedAt: '2025-11-12T15:00:00Z'
    }
  ]);

  // Add feedback to mock array
  const addFeedback = (feedback) => {
    console.log('🧩 Adding feedback to global array:', feedback);
    const fakeId = String(feedbackList.length + 1);
    const submittedAt = new Date().toISOString();

    setFeedbackList((prev) => [
      ...prev,
      { id: fakeId, submittedAt, ...feedback }
    ]);
  };

  // 🔹 NEW: helper function to get feedback by memberId (for mocking)
  const getFeedbackByMemberId = (memberId) => {
    return feedbackList.filter(
      (fb) => fb.memberId.trim().toLowerCase() === memberId.trim().toLowerCase()
    );
  };

  return (
    <FeedbackContext.Provider value={{ feedbackList, addFeedback, getFeedbackByMemberId }}>
      {children}
    </FeedbackContext.Provider>
  );
}
