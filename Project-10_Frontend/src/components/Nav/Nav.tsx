import { NavLink } from "react-router-dom";
import "./Nav.css";

function Nav() {
    return (
        <header>
            <nav>
                <ul>
                    <li>
                        <NavLink
                            to="/"
                            className={
                                ({ isActive }) =>
                                    isActive ? "active" : "" } end>
                            Home
                        </NavLink>
                    </li>

                    <li>
                        <NavLink
                            to="/random_joke"
                            className={
                                ({ isActive }) =>
                                    isActive ? "active" : "" }>
                            Random Joke
                        </NavLink>
                    </li>

                    <li>
                        <NavLink
                            to="/ten_jokes"
                            className={
                                ({ isActive }) =>
                                    isActive ? "active" : "" }>
                            Ten Jokes
                        </NavLink>
                    </li>
                </ul>
            </nav>
        </header>
    );
}

export default Nav;
