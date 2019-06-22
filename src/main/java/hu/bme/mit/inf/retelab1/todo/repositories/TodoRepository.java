package hu.bme.mit.inf.retelab1.todo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.bme.mit.inf.retelab1.todo.model.TodoItem;


/**
 * 
 * @author Dimitri Vasiliev
 *
 */

public interface TodoRepository extends JpaRepository<TodoItem, Long> {

    List<TodoItem> findAll();
    
	List<TodoItem> findBytaskname(String taskname);
	List<TodoItem> findBytasknameStartsWithIgnoreCase(String taskname);
	
}