import Nav from "../../components/Nav/Nav";
import "./Home.css";
import joker from "../../assets/so_serious.jpg";

function Home() {
    return (
        <>
            <Nav />
            <main>
                <img src={ joker } alt="Why so serious?" />
                <h1>
                    Welcome to "Random Joke"
                </h1>
                <p>
                    This is a simple example of working with the Random Joke API
                </p>
            </main>
        </>
    );
}

export default Home;
