import { type JSX, useContext } from "react";
import Todo from "../models/todo";
import TodoItem from "./TodoItem";
import { TodosContext } from "../utils/todos-context.tsx";

const Todos = (): JSX.Element => {
    const todosCtx = useContext(TodosContext);
    return (
        <ul>
            {todosCtx.items.map(
                (item: Todo): JSX.Element => (
                    <TodoItem
                        key={item.id}
                        text={item.text}
                        // Method One: .bind()
                        // onRemoveTodo={todosCtx.handleRemoveTodo.bind(null, item.id)}
                        // Method Two: Arrow Function
                        onRemoveTodo={() => {
                            todosCtx.handleRemoveTodo(item.id);
                        }}
                    />
                ),
            )}
        </ul>
    );
};

export default Todos;
