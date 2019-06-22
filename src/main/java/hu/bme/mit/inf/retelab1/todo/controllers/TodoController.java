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
import hu.bme.mit.inf.retelab1.todo.repositories.TodoRepository;

@RestController
@RequestMapping("/todo")
public class TodoController {

	@Autowired TodoRepository todoRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public List<TodoItem> todos(){	
		return this.todoRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public TodoItem getTodoById(@PathVariable("id") Long id){
        return todoRepository.findById(id).orElse(null);
	}

	@RequestMapping(method = RequestMethod.POST)
	public void saveTodo(@RequestBody @Valid TodoItem todo){
		todo.setId(null);
		todoRepository.save(todo);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/{id}")
	public void deleteTodo(@PathVariable("id") Long id){
        todoRepository.deleteById(id);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	public void editTodo(@RequestBody @Valid TodoItem editedTodo ,@PathVariable("id") Long id){
		editedTodo.setId(id);
		todoRepository.saveAndFlush(editedTodo);
	}
	
}