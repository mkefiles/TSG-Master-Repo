import { useState, useEffect } from "react";
import "./Joke.css";
import { type JokePayload } from "../../models/customTypes";

function Joke() {

    // DESC: `useState` allows React to track 'state'
    const [joke, setJoke] = useState<JokePayload | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<null | Error | unknown>(null);

    // NOTE: Place `fetchJoke` in standalone function so
    // ... the button can call to re-fetch a joke on each click
    const fetchJoke = async () => {
        try {
            const response = await
                fetch("https://official-joke-api.appspot.com/random_joke");

            if (!response.ok) {
                throw new Error(`HTTP Error -- ${response.status}`);
            }
            const jokeResult: JokePayload = await response.json();
            setJoke(jokeResult);
        } catch (err) {
            setError(err);
        } finally {
            setLoading(false);
        }
    };

    // DESC: `useEffect` enables 'side-effects' (e.g., fetching data)
    // NOTE: The empty array ensures that this only runs once on
    // ... the initial render (i.e., it prevents infinite loop)
    useEffect(() => {
        fetchJoke();
    }, []);

    // DESC: Handle possible responses ('error', 'loading, or success)
    const renderContent = () => {
        if (error) {
            return (
                <>
                    <p className="error">
                        An error has been encountered
                    </p>
                </>
            );
        } else if (loading) {
            return (
                <>
                    <p className="loading">
                        { loading }
                    </p>
                </>
            );
        } else {
            console.log(joke);
            return (
                <>
                    <p className="setup">
                        { joke?.setup }
                    </p>
                    <p className="punchline">
                        { joke?.punchline }
                    </p>
                </>
            );
        }
    };

    // DESC: Display the joke and the button
    return (
        <>
            { renderContent() }
            <button onClick={fetchJoke} >Get New Joke</button>
        </>
    );
}

export default Joke;
