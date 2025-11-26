import './FindFeedbackPage.css'

import FeedbackById from "../../components/FeedbackById/FeedbackById";
import FeedbackByMemberId from "../../components/FeedbackByMemberId/FeedbackByMemberId";

function FindFeedbackPage( ) {
  
  return (
     <div className='FindFeedbackPage'>
        <FeedbackById/> 
        <FeedbackByMemberId/>
    </div>
  );
  }

export default FindFeedbackPage;
