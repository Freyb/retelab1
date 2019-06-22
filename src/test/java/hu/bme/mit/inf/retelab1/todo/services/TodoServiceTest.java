package hu.bme.mit.inf.retelab1.todo.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import hu.bme.mit.inf.retelab1.todo.model.Priority;
import hu.bme.mit.inf.retelab1.todo.model.TodoItem;
import hu.bme.mit.inf.retelab1.todo.repositories.TodoRepository;

@SpringBootTest
public class TodoServiceTest {

    @Autowired
    private TodoService todoService;

    @MockBean
    private TodoRepository todoRepository;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void givenTwoTodos_whenGettingAll_thenAllIsReturned() {
        // Given
        TodoItem item1 = new TodoItem("Task1", new Date(), "Category", Priority.IMPORTANT, false);

        TodoItem item2 = new TodoItem("Task2", new Date(), "Category", Priority.IMPORTANT, false);

        Mockito.when(todoRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        // When
        List<TodoItem> items = todoService.getTodos();

        // Then
        assertEquals(2, items.size());
    }

    @Test
    void givenATodoWithId_whenGettingThatById_thenItIsReturned() {
        // Given
        TodoItem item1 = new TodoItem("Task1", new Date(), "Category", Priority.IMPORTANT, false);
        item1.setId(1L);

        Mockito.when(todoRepository.findById(1L)).thenReturn(Optional.of(item1));

        // When
        TodoItem itemReturned = todoService.getTodoById(1L);

        // Then
        assertNotNull(itemReturned);
        assertEquals(item1.getTaskname(), itemReturned.getTaskname());
    }

    @Test
    void givenAnEmptyRepository_whenANewTodoIsAdded_thenPersistsCorrectly() {
        // Given

        // When
        TodoItem item1 = new TodoItem("Task1", new Date(), "Category", Priority.IMPORTANT, false);
        item1.setId(1L);

        // And
        todoService.addTodo(item1);

        // Then
        assertNull(item1.getId());
        verify(todoRepository, times(1)).save(item1);
        verifyNoMoreInteractions(todoRepository);
    }

    @Test
    void givenATodo_whenThatTodoIsUpdated_thenPersistsCorrectly() {
        // Given
        TodoItem item1 = new TodoItem("Task1", new Date(), "Category1", Priority.IMPORTANT, false);

        // When
        todoService.updateTodo(1L, item1);

        // Then
        assertNotNull(item1.getId());
        verify(todoRepository, times(1)).saveAndFlush(item1);
        verifyNoMoreInteractions(todoRepository);
    }

    @Test
    void givenATodo_whenItIsDeleted_thenItIsRemoved() {
        // Given

        // When
        todoService.deleteTodo(1L);

        // Then
        verify(todoRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(todoRepository);
    }
}