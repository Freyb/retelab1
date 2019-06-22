package hu.bme.mit.inf.retelab1.todo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.bme.mit.inf.retelab1.todo.model.TodoItem;


/**
 * 
 * @author Dimitri Vasiliev
 *
 */

public interface TodoRepository extends JpaRepository<TodoItem, Long> {	
}