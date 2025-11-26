import { type JSX} from "react";

import NewTodo from "./components/NewTodo.tsx";
import Todos from "./components/Todos.tsx";
import TodosContextProvider from "./components/TodosContextProvider.tsx";

const App = (): JSX.Element => {

	return (
		<TodosContextProvider>
			<div className="container">
				<h1>Fancy To-Do App</h1>
				<NewTodo />
				<Todos />
			</div>
		</TodosContextProvider>
	);
}

export default App;
