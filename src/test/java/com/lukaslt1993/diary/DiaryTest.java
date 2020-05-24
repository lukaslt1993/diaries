package com.lukaslt1993.diary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.lukaslt1993.diary.models.Record;
import com.lukaslt1993.diary.repositories.DiariesRepository;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class DiaryTest {
	
	@Autowired
	DiariesRepository repo;

	@Autowired
	WebApplicationContext webApplicationContext;

	private MockMvc mvc;
	private final String diaries = EndpointNames.DIARIES, register = EndpointNames.REGISTER, email = "aaa@bbb.com";

	@BeforeEach
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(SecurityMockMvcConfigurers.springSecurity()).build();
	}

	@Test
	@Order(1)
	public void register() throws Exception {
		String json = "{\"email\": \"aaa@bbb.com\", \"password\": \"abc\"}";
		mvc.perform(MockMvcRequestBuilders.post(register).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(email)
	@Test
	@Order(2)
	public void createRecord() throws Exception {
		String json = "{\"title\": \"title1\", \"text\": \"text1\"}";
		mvc.perform(MockMvcRequestBuilders.post(diaries).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	
	@WithMockUser(email)
	@Test
	@Order(3)
	public void updateRecord() throws Exception {
		String json = "{\"title\": \"title11\", \"text\": \"text11\"}";
		mvc.perform(MockMvcRequestBuilders.put(diaries + "/" + repo.count()).contentType(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@WithMockUser(email)
	@Test
	@Order(4)
	public void createAnotherRecord() throws Exception {
		String json = "{\"title\": \"title2\", \"text\": \"text2\"}";
		mvc.perform(MockMvcRequestBuilders.post(diaries).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(email)
	@Test
	@Order(5)
	public void getRecords() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(diaries)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(email)
	@Test
	@Order(6)
	public void deleteRecord() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete(diaries + "/" + repo.count()))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@WithMockUser("Donald@J.Trump")
	@Test
	@Order(7)
	public void otherUserCantUpdate() throws Exception {
		String json = "{\"title\": \"title111\", \"text\": \"text111\"}";
		mvc.perform(MockMvcRequestBuilders.put(diaries + "/" + repo.count()).contentType(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}
	
	@WithMockUser("Hacker@anonymous.to")
	@Test
	@Order(8)
	public void otherUserCantSee() throws Exception {
		assertTrue(mvc.perform(MockMvcRequestBuilders.get(diaries))
				.andReturn().getResponse().getContentAsString().equals("[]"));
	}
	
	@WithMockUser("Saulius@Upes.gatve")
	@Test
	@Order(9)
	public void otherUserCantDelete() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete(diaries + "/" + repo.count()))
			.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}
	
	@Test
	@Order(10)
	public void generalTest() {
		List<Record> records = repo.findAll();
		Record rec = records.get(records.size() - 1);
		assertEquals("title11", rec.getTitle());
		assertEquals("text11", rec.getText());
	}

}
