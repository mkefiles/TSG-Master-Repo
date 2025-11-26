import React from 'react';
import Todo from "../models/todo.ts";

export type TodosContextObject = {
	items: Todo[];
	handleCreateTodo: (text: string) => void;
	handleRemoveTodo: (id: string) => void;
};

export const TodosContext = React.createContext<TodosContextObject>({
	items: [],
	handleCreateTodo: () => {},
	handleRemoveTodo: () => {},
});



