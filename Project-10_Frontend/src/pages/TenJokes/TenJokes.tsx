import Nav from "../../components/Nav/Nav";
import joker from "../../assets/so_serious.jpg";
import Jokes from "../../components/Jokes/Jokes";

function TenJokes() {
    return (
        <>
            <Nav />
            <main>
                <img src={ joker } alt="Why so serious?" />
                <h1>
                    Lots o' Laughs
                </h1>
                <Jokes />
            </main>
        </>
    );
}

export default TenJokes;
