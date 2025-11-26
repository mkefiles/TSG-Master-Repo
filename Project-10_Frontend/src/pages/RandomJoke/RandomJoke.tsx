import Nav from "../../components/Nav/Nav";
import joker from "../../assets/so_serious.jpg";
import Joke from "../../components/Joke/Joke";

function RandomJoke() {
    return (
        <>
            <Nav />
            <main>
                <img src={ joker } alt="Why so serious?" />
                <h1>
                    Random Joke Generator
                </h1>
                <Joke />
            </main>
        </>
    );
}

export default RandomJoke;
