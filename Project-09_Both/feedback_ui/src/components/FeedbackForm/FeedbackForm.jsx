import { useState } from 'react';
import axios from 'axios'

function FeedbackForm() {

  const [newFeedback, setNewFeedback] = useState({ memberId: '', providerName: '', rating: '', comment: '' })
  const [errors, setErrors] = useState({});

  //? This is for actual functionality
  const submitFeedback = async (event) => {
    event.preventDefault();

    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return; 
    }

    try {
      const response = await axios.post("http://localhost:8080/api/v1/feedback", newFeedback)

      console.log("Feedback submitted: ", response.data);

      setNewFeedback({ memberId: '', providerName: '', rating: 0, comment: '' });
      console.log("this is new feedback", newFeedback)
      setErrors({});
      alert("Thank you for feedback");
    } catch (err) {
      console.log("Error Submitting Feedback", err)
    }
  }

  const validate = () => {
  const errs = {};

  const memberIdRegex = /(?=.*[a-zA-Z])^[a-zA-Z0-9-]{3,36}$/;
  const providerNameRegex = /^[a-zA-Z][a-zA-Z\s.-]{1,78}[a-zA-Z]$/;
  const ratingRegex = /^[1-5]$/;

  if (!newFeedback.memberId.trim()) {
    errs.memberId = "Member ID is required.";
  } else if (!memberIdRegex.test(newFeedback.memberId)) {
    errs.memberId = "Invalid Member ID format. Use 3–36 letters/numbers, at least one letter.";
  }

  if (!newFeedback.providerName.trim()) {
    errs.providerName = "Provider name is required.";
  } else if (!providerNameRegex.test(newFeedback.providerName)) {
    errs.providerName = "Invalid Provider Name format (letters, spaces, periods, hyphens only).";
  }

  if (!ratingRegex.test(newFeedback.rating)) {
      errs.rating = "Rating must be between 1 and 5.";
    }

  if (newFeedback.comment.length > 200) {
    errs.comment = "Comment too long (max 200 characters).";
  }

  return errs;
};

  return (
    <div className='FeedbackForm'>
      <form onSubmit={submitFeedback}
      className="max-w-md mx-auto p-6 rounded-2xl shadow-xl space-y-4"
      >
        <input
          type='text'
          placeholder='Member Id'
          value={newFeedback.memberId}
          onChange={(e) => setNewFeedback({ ...newFeedback, memberId: e.target.value })}
          className="w-full px-4 py-2 rounded-lg border border-gray-300 bg-[#5DDE81] focus:ring-2 focus:ring-[#8C1531] focus:outline-none"

        />
        <input
          type='text'
          placeholder='Provider Name'
          value={newFeedback.providerName}
          onChange={(e) => setNewFeedback({ ...newFeedback, providerName: e.target.value })}
          className="w-full px-4 py-2 rounded-lg border border-gray-300 bg-[#5DDE81] focus:ring-2 focus:ring-[#8C1531] focus:outline-none"

        />
        <input
          type='text'
          placeholder='Rating from 1-5'
          value={newFeedback.rating}         
          onChange={(e) => setNewFeedback({ ...newFeedback, rating: e.target.value })}          
          className="w-full px-4 py-2 rounded-lg border border-gray-300 bg-[#5DDE81] focus:ring-2 focus:ring-[#8C1531] focus:outline-none"
        />
        <input
          type='text'
          placeholder='Comment(optional)'
          value={newFeedback.comment}
          onChange={(e) => setNewFeedback({ ...newFeedback, comment: e.target.value })}
          className="w-full px-4 py-2 rounded-lg border border-gray-300 bg-[#5DDE81] focus:ring-2 focus:ring-[#8C1531] focus:outline-none"

        />
        <button
          type="submit"
          className="w-full bg-[#BD1A41] text-white py-2 rounded-lg font-medium hover:bg-[#741126] transition"
        >Submit
        </button>
      </form>
      {errors.memberId && <p style={{ color: 'red' }}>{errors.memberId}</p>}
      {errors.providerName && <p style={{ color: 'red' }}>{errors.providerName}</p>}
      {errors.rating && <p style={{ color: 'red' }}>{errors.rating}</p>}
      {errors.comment && <p style={{ color: 'red' }}>{errors.comment}</p>}
    </div>
  );
}

export default FeedbackForm;
