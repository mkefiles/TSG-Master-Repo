# README

This will be a simple React project that explores getting data from an API. The API used is the [Random Jokes API](https://official-joke-api.appspot.com).

## GitHub Instructions

The project repo. will **not** include the *node_modules* folder (to reduce resources stored in the cloud). When the project is cloned, make sure to run `npm install` to pull down all applicable dependencies.

## Good-to-Knows

- Chain of command:
    - _index.html_ - contains the HTML "spring-board" (not much changes here)
        - Modifications: HTML Title, Site Favicon, etc.
    - _main.tsx_ - contains the React "spring-board"
        - Modifications: Setting up `StrictMode` and `BrowserRouter` for React debugging and React Router (respectively), initializing the `<App />` component, setting site-wide CSS, etc.
    - _App.tsx_ - contains the main React app
        - **Note:** This is a _boilerplate_ starting-point for the React application and, as such, is not necessary
- `<StrictMode>` - for debugging React apps (development only)
  - **Note:** Using `<StrictMode>` causes erratic behavior (e.g., `useEffect()` to double-render)
- Using `<NavLink >`, in lieu of `<Link >` makes it easier to show active states
    - Ensure that, for the index directory, `end` is placed prior to the `<NavLink>` closing-tag (e.g., `<NavLink end>`)
        - This ensures that the `\` does not behave like a wild-card for any route that comes after it
- When working in JSX / TSX, the `{ /* -- JS / TS Logic -- /* }` **must** be within a set of HTML tags **or** a *Fragment*, otherwise it will **not** work (odd errors will pop up and auto-complete wont work)
- A function can be created with both standard function-syntax (e.g., `function dataFetcher() {`) **or** const-syntax (e.g., `const dataFetcher = () => {`), however *const-syntax* is more commonly used
- If you place a `console.log()` on the data, within `useEffect()`, right after the state "setter" method, it will appear as if nothing happened... that is because the `console.log()` is on the "prior" state not the one that will be shown as soon as the re-render completes (i.e., the logging is outdated)
  - If it is not appearing on your screen, then it is likely that the JSX / TSX is incorrect

## Steps Taken

### Steps: Installation

1. Initiate Vite project: `sudo npm create vite@latest .`
2. Install any dependencies: `sudo npm install`
3. Install React Router DOM: `sudo npm install react-router-dom`
4. Update Vite Server Port:

```TypeScript
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";

// https://vite.dev/config/
export default defineConfig({
    plugins: [react()],
    server: {
        port: 3000,
    },
});
```

5. Install React's type definitions: `npm install --save-dev @types/react @types/react-dom`

**Note:**

- The is a standard _React_ and _TypeScript + SWC_ project
    - Per recommendation, SWC is great for most new projects otherwise use the standard TypeScript build
- Use the `.` to install the project in the working directory (not as a sub-directory)
- `react-router` is the _core_ library whereas `react-router-dom` is specific to websites, however when you install `react-router-dom` you get `react-router` as a dependency
- The *port* was updated to `3030` because an accidental problem caused an infinite loop on `useEffect()`, which resulted in an API CORS-block of port `3000`

### Steps: Recurring

1. Start the server: `npm run dev`

### Steps: Project Setup

This project will need to have a couple of routes (using React Router) and will need to communicate with an API (using `fetch` in lieu of `axios`).

1. Remove unnecessary files / code (see notes)
2. Add the following code, for React Router (Declarative), to _main.tsx_:

```TSX
import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router";
import App from "./app";

const root = document.getElementById("root");

ReactDOM.createRoot(root).render(
    <BrowserRouter>
        <App />
    </BrowserRouter>,
);
```

**Note:**

- Unnecessary files / folders removed:
    - Contents in _public/_ and _assets/_
    - Wiped out _index.css_
    - Removed _App.css_ and _App.tsx_

## Scenario-Specific Notes

### Buttons and Components

If the button directly manipulates the state of its immediate parent or itself, it's typically placed within that stateful component (see snippet below):

```JSX
import React, { useState } from 'react';

function Counter() {
    const [count, setCount] = useState(0);
    
    const increment = () => {
        setCount(count + 1);
    };
    
    return (
        <div>
        <p>Count: {count}</p>
        <button onClick={increment}>Increment</button>
        </div>
    );
}
```

If the button's role is to trigger an action in a higher-level component without managing its own state, it can be a separate, often stateless, component that receives a handler function via props. This promotes reusability and separation of concerns (see snippet below):

```JSX
import React, { useState } from 'react';

function MyButton({ onClickHandler, label }) {
    return (
        <button onClick={onClickHandler}>{label}</button>
    );
}

function ParentComponent() {
    const [message, setMessage] = useState("Hello");

    const changeMessage = () => {
        setMessage("Goodbye");
    };
    
    return (
        <div>
            <p>{message}</p>
            <MyButton
                onClickHandler={changeMessage}
                label="Change Message" />
        </div>
    );
}
```