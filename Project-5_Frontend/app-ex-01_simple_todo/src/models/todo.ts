class Todo {
	public id: string;
	public text: string;

	constructor(text: string) {
		this.text = text;
		this.id = new Date().toISOString();
	}
}

export default Todo;