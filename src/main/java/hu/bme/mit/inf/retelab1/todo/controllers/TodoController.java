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

	@Autowired TodoService todoService;
	
	@RequestMapping(method = RequestMethod.GET)
	public List<TodoItem> todos(){	
		return todoService.getTodos();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public TodoItem getTodoById(@PathVariable("id") Long id){
        return todoService.getTodoById(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	public void saveTodo(@RequestBody @Valid TodoItem todo){
		todoService.addTodo(todo);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	public void deleteTodo(@PathVariable("id") Long id){
        todoService.deleteTodo(id);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	public void editTodo(@RequestBody @Valid TodoItem editedTodo, @PathVariable("id") Long id){
		todoService.updateTodo(id, editedTodo);
	}
	
}