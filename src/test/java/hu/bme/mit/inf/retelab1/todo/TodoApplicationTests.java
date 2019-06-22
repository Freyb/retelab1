package hu.bme.mit.inf.retelab1.todo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import hu.bme.mit.inf.retelab1.todo.model.Priority;
import hu.bme.mit.inf.retelab1.todo.model.TodoItem;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class TodoApplicationTests {

    @Autowired
    private MockMvc mvc;

	@Test
	void givenAFreshApplication_whenTwoTodosAreAdded_thenTheyPersist_whenOneIsDeleted_thenOnlyOneRemains() throws Exception {
		// Given

		// When
		mvc.perform(post("/todo")
        .contentType(MediaType.APPLICATION_JSON)
		.content(asJsonString(new TodoItem("Task1", new Date(), "Category", Priority.IMPORTANT, false))))
		
		// Then
		.andExpect(status().isOk());

		// When
		mvc.perform(post("/todo")
        .contentType(MediaType.APPLICATION_JSON)
		.content(asJsonString(new TodoItem("Task2", new Date(), "Category", Priority.IMPORTANT, false))))
		
		// Then
		.andExpect(status().isOk());

		// When 

		MvcResult result = mvc.perform(get("/todo")
		.contentType(MediaType.APPLICATION_JSON))

        // Then
        .andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(2)))
		.andReturn();

		DocumentContext context = JsonPath.parse(result.getResponse().getContentAsString());
		long id1 = Long.valueOf((int) context.read("$[0].id"));
		String taskname2 = context.read("$[1].taskname");
		
		// When
		mvc.perform(delete("/todo/" + id1)
		.contentType(MediaType.APPLICATION_JSON))

		// Then
		.andExpect(status().isOk());

		// When
		mvc.perform(get("/todo")
        .contentType(MediaType.APPLICATION_JSON))

        // Then
        .andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(1)))
		.andExpect(jsonPath("$[0].taskname", is(taskname2)));
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
