package hu.bme.mit.inf.retelab1.todo.model;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TodoItemTest {
 
    @Test
    void givenCustomCreatedTodoItem_thenPersistsCorrectly_1() {
        // Given
        String taskname = "Taskname1";
        Date performdate = new Date();
        String category = "Category1";
        Priority priority = Priority.IMPORTANT;
        boolean isdone = false;

        TodoItem item = new TodoItem(taskname, performdate, category, priority, isdone);

        // Then
        assertEquals(taskname, item.getTaskname());
        assertEquals(performdate, item.getPerformdate());
        assertEquals(category, item.getCategory());
        assertEquals(priority, item.getPriority());
        assertEquals(isdone, item.getIsdone());
    }

    @Test
    void givenCustomCreatedTodoItem_thenPersistsCorrectly_2() {
        // Given
        String taskname = "Taskname2";
        Date performdate = new Date();
        String category = "Category2";
        Priority priority = Priority.HIGH;
        boolean isdone = true;

        TodoItem item = new TodoItem(taskname, performdate, category, priority, isdone);

        // Then
        assertEquals(taskname, item.getTaskname());
        assertEquals(performdate, item.getPerformdate());
        assertEquals(category, item.getCategory());
        assertEquals(priority, item.getPriority());
        assertEquals(isdone, item.getIsdone());
    }

    @Test
    void givenDefaultCreatedTodoItem_whenIdIsSet_thenPersistsCorrectly() {
        // Given
        TodoItem item = new TodoItem();

        // When
        item.setId(4L);

        // Then
        assertEquals(4, item.getId());
    }
 
}