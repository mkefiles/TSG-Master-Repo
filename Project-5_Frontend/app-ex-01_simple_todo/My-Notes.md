# My Notes

## General Notes
This examples wraps the majority of the components with `useContext()` in lieu of "prop-drilling". This is, mainly, because the same *props* are used throughout.

- Use *Props* (and "Prop Drilling") for local, explicit and direct data-flow between closely related components
- Use *Context* for global, application-wide data, avoiding prop-drilling in deeply nested components and managing cross-cutting concerns

## Component-Specific Notes
### Todos.tsx
In this component, the instructor introduced a minor curveball in using the `.bind()` method. The following examples both accomplish the identical task, yet one uses `.bind()` and the other uses an Arrow Function:

**bind()**
```tsx
<TodoItem onRemoveTodo={todosCtx.handleRemoveTodo.bind(null, item.id)} />
```

**Arrow Function**
```tsx
<TodoItem onRemoveTodo={() => { todosCtx.handleRemoveTodo(item.id) }} />
```

Based on research, the problem comes down to the `this` keyword and what it refers to. Generally speaking, `this` determines which Object is the focus of the function that immediately succeeds/follows it and the *scope* also affects it as well...

- IF `this` is called within the global scope, and does not have a succeeding function, then it is referring to the `window` Object (that is the "global" object)
- IF `this` is called within a functions scope, and does not have a succeeding function, then it is referring to the function itself as an Object

With that said, when we are *passing* a function we **do not** want to *call* it, however we need to provide it with the `item.id` parameter (for this example that is). The solution is either to use an Arrow Function that *calls* the desired function with the necessary parameter (because the Arrow Function does not immediately execute) **or** to call `.bind()`, which gives you access to `this`, allows you to pass a parameter **and** does not have the function immediately execute.

### NewTodo.tsx
#### How User Input was retrieved:
To get User Input, you can either:

- Listen to every key-stroke with `useState()`, or...
- Implement `useRef()` to get the submitted input
  - `useRef()` enables you to access the D.O.M. element directly

This component uses the `useRef()` approach.

Also, please note the following:

- `HTMLInputElement` must be passed as a Generic to let `useRef()` know what data-type will be expected
  - This is because it does not know what the `<input>` field will be providing
  - It is initialized to `null` because input has not yet been rec'd at run-time
- The `enteredText` variable uses a *Non-Null Assertion* because we know that they value will not be a `null` value
  - The handling of an empty string would need to be handled properly

## StrictMode
Wrapping the `<App />` with `<StrictMode>` simply provides better React Dev. Tooling.

## Event-Handler Props
It is best practice to declare them w/i the component AND to use a naming convention that starts with `handle`.

## Creating a Component (React.FC vs Alternative)
Based on research, `React.FC` is now deprecated and is only to be used in older React apps (anything pre-dating React 18 and TypeScript 5.1).

**Extract the Props**
```tsx
type MyComponent = {
	name: string;
	age: number;
};
```

**Pre-React 18 & TypeScript 5.1 (DEPRECATED)**
```tsx
const MyComponent: React.FC<MyComponentProps> = ({name, age}) => {
	return ({/* TSX goes here... */});
}
```

**Preferred Approach**
```tsx
const MyComponent = (props: MyComponentProps): JSX.Element => {
	return ({/* TSX goes here... */});
}
```

**A "More JavaScript" Approach**
```tsx
function MyComponent (props: MyComponentProps): JSX.Element {
	return ({/* TSX goes here... */});
}
```

**Note:**
- Pulling the *props* out to their own `type` is optional, however this appears to be preferred/best-practice (also helps make the code cleaner)
- The `React.FC` automatically passes any *children* (i.e., nested HTML/JSX) whereas the *preferred approach* does not