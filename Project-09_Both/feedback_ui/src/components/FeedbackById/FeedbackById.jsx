import { useState } from 'react';

import axios from 'axios'

function FeedbackById( ) {
    
  const [feedbackId, setFeedbackId] = useState('');
  const [feedback, setFeedback] = useState(null);
  const [error, setError] = useState('');

const handleSearch = async (event) => {
  event.preventDefault();
  setError('');
  setFeedback(null);

  try {
    const response = await axios.get(`http://localhost:8080/api/v1/feedback/${feedbackId}`);
    setFeedback(response.data);
  } catch (err) {
    if (err.response && err.response.status === 400){
      setError("Feedback not found");
    } else {
      setError("Error fetching feedback");
    }
  }
};


  return (
     <div className='FeedbackById'>
      <h2  className="text-3xl font-semibold text-[#16A842] tracking-tight leading-tight"
      >Find feedback by ID</h2>
      <form onSubmit={handleSearch}
      className="max-w-md mx-auto p-6 rounded-2xl shadow-xl space-y-4">
        <input
        type="text" 
        placeholder='Enter a feedback ID'
        value={feedbackId}
        onChange={(e)=> setFeedbackId(e.target.value)}
        className="w-full px-4 py-2 rounded-lg border border-gray-300 bg-[#5DDE81] focus:ring-2 focus:ring-[#8C1531] focus:outline-none"
        />
     <button type="submit"
     className="w-full bg-[#BD1A41] text-white py-2 rounded-lg font-medium hover:bg-[#741126] transition">Submit</button>
      </form>
      {error && <p style={{ color: 'red' }}>{error}</p>}
{feedback && (
  <div className="mt-8">
    <h3 className="text-3xl font-semibold text-[#16A842] mb-4">
      Feedback Details
    </h3>

    <div
      className="border border-gray-300 rounded-xl p-4 mb-6 bg-[#5DDE81] shadow-sm w-[400px] mx-auto"
    >
      <p><strong>ID:</strong> {feedback.id}</p>
      <p><strong>Member ID:</strong> {feedback.memberId}</p>
      <p><strong>Provider Name:</strong> {feedback.providerName}</p>
      <p><strong>Rating:</strong> {feedback.rating}</p>
      <p><strong>Comment:</strong> {feedback.comment}</p>
      <p>
        <strong>Submitted At:</strong>{" "}
        {new Date(feedback.submittedAt).toLocaleString()}
      </p>
    </div>
  </div>
)}

    </div>
  );
  }

export default FeedbackById;
