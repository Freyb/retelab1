package hu.bme.mit.inf.retelab1.todo.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hu.bme.mit.inf.retelab1.todo.model.TodoItem;
import hu.bme.mit.inf.retelab1.todo.services.TodoService;

@RestController
@RequestMapping("/todo")
public class TodoController {

	@Autowired 
	private TodoService todoService;
	
	@RequestMapping(method = RequestMethod.GET)
	public List<TodoItem> todos() {	
		return todoService.getTodos();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public TodoItem getTodoById(@PathVariable("id") final Long id) {
        return todoService.getTodoById(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	public void saveTodo(@RequestBody @Valid final TodoItem todo) {
		todoService.addTodo(todo);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void deleteTodo(@PathVariable("id") final Long id) {
        todoService.deleteTodo(id);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public void editTodo(@RequestBody @Valid final TodoItem editedTodo, @PathVariable("id") final Long id) {
		todoService.updateTodo(id, editedTodo);
	}
	
}