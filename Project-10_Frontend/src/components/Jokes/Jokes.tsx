import { useState, useEffect } from "react";
import "../Joke/Joke.css";
import { type JokePayload } from "../../models/customTypes";

function Jokes() {

    // DESC: `useState` allows React to track 'state'
    const [jokes, setJokes] = useState<JokePayload[]>([]);
    const [error, setError] = useState<null | Error | unknown | string>(null);
    const [refreshIndex, setRefreshIndex] = useState<number>(0);

    // DESC: `useEffect` enables 'side-effects' (e.g., fetching data)
    // NOTE: The empty array ensures that this only runs once on
    // ... the initial render (i.e., it prevents infinite loop)
    useEffect(() => {

        console.log("DATA REFRESHED!!");

        const dataFetcher = async () => {
            try {
                // NOTE: `fetch()` returns a Promise that resolves to a
                // ... Response for the initial Request -- once it has
                // ... rec'd a Response (just the headers / status), the
                // ... Promise is fulfilled
                // NOTE: Execution will pause until the Promise resolves
                // NOTE: The generic type of the Promise should correspond
                // ... to the non-error return-type ... in the event of an
                // ... error it will NOT resolve to the 'type' specified
                const fetchResponse: Response = await fetch(
                    "https://official-joke-api.appspot.com/random_ten"
                );

                if (fetchResponse.status != 200) {
                    throw new Error("ERR (HTTP Status Code): " + fetchResponse.status);
                } else {
                    const fetchResult: JokePayload[] = await fetchResponse.json();

                    setJokes(fetchResult);
                }
            } catch (err: Error | unknown) {
                setError(err);
            }
        }

        dataFetcher();


    }, [refreshIndex]);

    const outputHandler = (jokeArr: JokePayload[], err: null | Error | unknown | string) => {
        if (err !== null) {
            return (
                <div>
                    <p className="warning">{ !err }</p>
                </div>
            );
        } else if (Array.isArray(jokeArr) && jokeArr.length > 0) {
            return (
                <>
                { jokeArr.map((joke: JokePayload) => (
                    <div key={ joke.id } className="joke-group">
                        <p className="setup">{joke.setup}</p>
                        <p className="punchline">{joke.punchline}</p>
                        <hr />
                    </div>
                ))}
                </>
            );
        } else {
            return (
                <div>
                    <p className="warning">No jokes found or invalid data structure</p>
                </div>
            );
        }
    };

    const refreshHandler = () => {
        setRefreshIndex(previousIndex => previousIndex + 1);
    };

    return (
        <>
            { outputHandler(jokes, error) }
            <button onClick={ refreshHandler }>Get Some Jokes</button>
        </>
    );
}


export default Jokes;