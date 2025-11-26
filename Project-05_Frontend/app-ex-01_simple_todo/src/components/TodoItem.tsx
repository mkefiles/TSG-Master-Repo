import {type JSX} from "react";

type TodoItemProps = {
	text: string;
	onRemoveTodo(): void;
};

const TodoItem = (props: TodoItemProps): JSX.Element => {
	return (
		<li>
			<span>{props.text}</span>
			<span className={"trash-can"} onClick={props.onRemoveTodo}>&#x1F5D1;</span>
		</li>
	);
};

export default TodoItem;