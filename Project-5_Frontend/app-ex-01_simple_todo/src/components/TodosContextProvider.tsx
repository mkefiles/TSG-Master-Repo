import {type JSX, type ReactNode, useState} from "react";
import Todo from "../models/todo.ts";
import {type TodosContextObject, TodosContext} from "../utils/todos-context.tsx";

type TodosContextProviderProps = {
	children?: ReactNode;
};

const TodosContextProvider = (props: TodosContextProviderProps): JSX.Element => {
	const [todos, setTodos] = useState<Todo[]>([]);

	const handleCreateTodo = (todoText: string) => {
		const newTodo = new Todo(todoText);

		setTodos((previousTodos) => {
			return previousTodos!.concat(newTodo);
		});
	};

	const handleRemoveTodo = (todoId: string): void => {
		setTodos((previousTodos) => {
			return previousTodos.filter(todo => todo.id !== todoId);
		});
	}

	const contextValue: TodosContextObject = {
		items: todos,
		handleCreateTodo: handleCreateTodo,
		handleRemoveTodo: handleRemoveTodo,
	};

	return (
		<TodosContext.Provider value={contextValue}>{props.children}</TodosContext.Provider>
	);
};

export default TodosContextProvider;