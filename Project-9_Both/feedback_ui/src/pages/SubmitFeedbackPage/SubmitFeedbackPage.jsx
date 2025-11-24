import FeedbackForm from "../../components/FeedbackForm/FeedbackForm";

function SubmitFeedbackPage( ) {
  
  return (
     <div className='SubmitFeedbackPage'>
      <p className="text-3xl font-semibold text-[#16A842] tracking-tight leading-tight">
      Submit your feedback
      </p>
      <FeedbackForm/>
    </div>
  );
  }

export default SubmitFeedbackPage;
