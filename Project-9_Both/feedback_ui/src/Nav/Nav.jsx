import { NavLink } from "react-router-dom";
import './Nav.css'

function Nav() {

  return (
    <div className='Nav'>
      <NavLink to="/" className="nav-button">
        Home
      </NavLink>
      <NavLink to="/feedbackForm" className="nav-button">
        Feedback Form
      </NavLink>
      <NavLink to="/findFeedback" className="nav-button">
        Find feedback
      </NavLink>
    </div>
  );
}

export default Nav;