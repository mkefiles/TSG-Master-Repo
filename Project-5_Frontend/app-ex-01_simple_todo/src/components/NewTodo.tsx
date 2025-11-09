import React, {useRef, type FormEvent, type JSX, type RefObject, useContext} from "react";
import { TodosContext } from "../utils/todos-context.tsx";

const NewTodo = (): JSX.Element => {
	const todosCtx = useContext(TodosContext);
	const todoTextInputRef: RefObject<HTMLInputElement | null> = useRef<HTMLInputElement>(null);
	const handleSubmit: (event: FormEvent<Element>) => void = (event: React.FormEvent) => {
		event.preventDefault();

		const enteredText = todoTextInputRef.current!.value;

		if (enteredText.trim().length === 0) {
			// FIXME: Throw an error/handle empty input
			return;
		}
		todosCtx.handleCreateTodo(enteredText);
	};
	return (
		<form onSubmit={handleSubmit}>
			<input className="new-todo" type="text" id="text" ref={todoTextInputRef} placeholder="Learn React"/>
			<button className="btn-submit">Add Todo</button>
		</form>
	);
};

export default NewTodo;