package hu.bme.mit.inf.retelab1.todo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import hu.bme.mit.inf.retelab1.todo.model.Priority;
import hu.bme.mit.inf.retelab1.todo.model.TodoItem;
import hu.bme.mit.inf.retelab1.todo.services.TodoService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private TodoService todoService;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    void givenTwoTodos_whenGettingAll_thenAllIsReturned() throws Exception {
        // Given
        TodoItem item1 = new TodoItem("Task1", new Date(), "Category", Priority.IMPORTANT, false);

        TodoItem item2 = new TodoItem("Task2", new Date(), "Category", Priority.IMPORTANT, false);

        Mockito.when(todoService.getTodos()).thenReturn(Arrays.asList(item1, item2));

        // When
        mvc.perform(get("/todo")
        .contentType(MediaType.APPLICATION_JSON))

        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void givenATodoWithId_whenGettingThatById_thenItIsReturned() throws Exception {
        // Given
        TodoItem item1 = new TodoItem("Task1", new Date(), "Category", Priority.IMPORTANT, false);
        item1.setId(1L);

        Mockito.when(todoService.getTodoById(1L)).thenReturn(item1);

        // When
        mvc.perform(get("/todo/1")
        .contentType(MediaType.APPLICATION_JSON))

        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.taskname", is(item1.getTaskname())));
    }

    @Test
    void givenAnEmptyRepository_whenANewTodoIsAdded_thenPersistsCorrectly() throws Exception {
        // Given

        // When
        mvc.perform(post("/todo")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(new TodoItem("Task1", new Date(), "Category", Priority.IMPORTANT, false))))

        // Then
        .andExpect(status().isOk());

        // And
        ArgumentCaptor<TodoItem> item = ArgumentCaptor.forClass(TodoItem.class);
        verify(todoService, times(1)).addTodo(item.capture());
        assertEquals("Task1", item.getValue().getTaskname());

        verifyNoMoreInteractions(todoService);
    }

    @Test
    void givenATodo_whenThatTodoIsUpdated_thenPersistsCorrectly() throws Exception {
        // Given

        // When
        mvc.perform(put("/todo/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(new TodoItem("Task1", new Date(), "Category", Priority.IMPORTANT, false))))

        // Then
        .andExpect(status().isOk());

        // And
        ArgumentCaptor<Long> id = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TodoItem> item = ArgumentCaptor.forClass(TodoItem.class);
        verify(todoService, times(1)).updateTodo(id.capture(), item.capture());
        assertEquals(1L, id.getValue());
        assertEquals("Task1", item.getValue().getTaskname());

        verifyNoMoreInteractions(todoService);
    }

    @Test
    void givenATodo_whenItIsDeleted_thenItIsRemoved() throws Exception {
        // Given

        // When
        mvc.perform(delete("/todo/1")
        .contentType(MediaType.APPLICATION_JSON))

        // Then
        .andExpect(status().isOk());

        // And
        verify(todoService, times(1)).deleteTodo(1L);
        verifyNoMoreInteractions(todoService);
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }  
}