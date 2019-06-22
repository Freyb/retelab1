package hu.bme.mit.inf.retelab1.todo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

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
		class Tmp {
			public Long id1;
			public String taskname2;
		}
		final Tmp tmp = new Tmp();

		mvc.perform(get("/todo")
        .contentType(MediaType.APPLICATION_JSON))

        // Then
        .andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(2)))
		.andDo((res) -> {
			DocumentContext context = JsonPath.parse(res.getResponse().getContentAsString());

			int id = context.read("$[0].id");
			tmp.id1 = Long.valueOf(id);
			tmp.taskname2 = context.read("$[1].taskname");
		});
		
		// When
		mvc.perform(delete("/todo/" + tmp.id1)
		.contentType(MediaType.APPLICATION_JSON))

		// Then
		.andExpect(status().isOk());

		// When
		mvc.perform(get("/todo")
        .contentType(MediaType.APPLICATION_JSON))

        // Then
        .andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(1)))
		.andExpect(jsonPath("$[0].taskname", is(tmp.taskname2)));
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
