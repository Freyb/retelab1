package hu.bme.mit.inf.retelab1.todo.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import hu.bme.mit.inf.retelab1.todo.model.Priority;
import hu.bme.mit.inf.retelab1.todo.model.TodoItem;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TodoRepositoryTest {
 
    @Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private TodoRepository todoRepository;
 
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void givenTwoTodos_whenGettingAll_thenAllIsReturned() {
        // Given
        TodoItem item1 = new TodoItem("Task1", new Date(), "Category", Priority.IMPORTANT, false);
        entityManager.persistAndFlush(item1);

        TodoItem item2 = new TodoItem("Task2", new Date(), "Category", Priority.IMPORTANT, false);
        entityManager.persistAndFlush(item2);

        // When
        List<TodoItem> items = todoRepository.findAll();

        // Then
        assertEquals(2, items.size());
    }

    @Test
    void givenTwoTodos_whenGettingOneById_thenCorrectTodoIsReturned() {
        // Given
        TodoItem item1 = new TodoItem("Task1", new Date(), "Category", Priority.IMPORTANT, false);
        entityManager.persistAndFlush(item1);

        TodoItem item2 = new TodoItem("Task2", new Date(), "Category", Priority.IMPORTANT, false);
        entityManager.persistAndFlush(item2);

        // When
        Long id = entityManager.getId(item1, Long.class);
        TodoItem itemReturned = todoRepository.findById(id).orElse(null);

        // Then
        assertNotNull(itemReturned);
        assertEquals(item1.getTaskname(), itemReturned.getTaskname());
    }

    @Test
    void givenAnEmptyRepository_whenANewTodoIsAdded_thenPersistsCorrectly() {
        // Given

        // When
        TodoItem item1 = new TodoItem("Task1", new Date(), "Category", Priority.IMPORTANT, false);
        todoRepository.saveAndFlush(item1);

        // And
        List<TodoItem> items = todoRepository.findAll();

        // Then
        assertEquals(1, items.size());
        assertEquals(item1.getTaskname(), items.get(0).getTaskname());
    }

    @Test
    void givenATodo_whenThatTodoIsUpdated_thenPersistsCorrectly() {
        // Given
        TodoItem item1 = new TodoItem("Task1", new Date(), "Category1", Priority.IMPORTANT, false);
        entityManager.persistAndFlush(item1);
        Long id = entityManager.getId(item1, Long.class);

        // When
        TodoItem item2 = new TodoItem("Task2", new Date(), "Category2", Priority.HIGH, true);
        item2.setId(id);
        todoRepository.saveAndFlush(item2);

        // And
        TodoItem itemReturned = todoRepository.findById(id).orElse(null);

        // Then
        assertNotNull(itemReturned);
        assertEquals(item2.getTaskname(), itemReturned.getTaskname());
        assertEquals(item2.getPerformdate(), itemReturned.getPerformdate());
        assertEquals(item2.getCategory(), itemReturned.getCategory());
        assertEquals(item2.getPriority(), itemReturned.getPriority());
        assertEquals(item2.getIsdone(), itemReturned.getIsdone());
    }

    @Test
    void givenATodo_whenItIsDeleted_thenItIsRemoved() {
        // Given
        TodoItem item1 = new TodoItem("Task1", new Date(), "Category", Priority.IMPORTANT, false);
        entityManager.persistAndFlush(item1);
        Long id = entityManager.getId(item1, Long.class);

        // Then
        assertEquals(1, todoRepository.findAll().size());

        // When
        todoRepository.deleteById(id);

        // Then
        assertEquals(0, todoRepository.findAll().size());
    }
 
}