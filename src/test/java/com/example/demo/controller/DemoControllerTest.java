package com.example.demo.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controller.model.Demo;
import com.example.demo.service.DemoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = DemoController.class)
public class DemoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private DemoService service;

	@Test
	public void shouldFindAllDemos() throws Exception {
		List<Demo> list = Arrays.asList(Demo.builder().id(1L).name("Demo 1").description("Demo1 Desc").build(),
				Demo.builder().id(1L).name("Demo 2").description("Demo2 Desc").build());
		BDDMockito.given(service.findAllDemos()).willReturn(list);
		mockMvc.perform(get("/demos")).andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(list.size())));
	}

	@Test
	public void shouldFindDemoById() throws Exception {
		BDDMockito.given(service.findDemoById(1L))
				.willReturn(Optional.of(Demo.builder().id(1L).name("Demo1").description("Demo Description").build()));
		mockMvc.perform(get("/demos/{id}", 1L)).andExpect(status().isOk()).andExpect(jsonPath("$.name", is("Demo1")));
	}

	@Test
	void shouldReturn404WhenFindDemoById() throws Exception {
		final Long demoId = 1L;
		BDDMockito.given(service.findDemoById(demoId)).willReturn(Optional.empty());

		this.mockMvc.perform(get("/demos/{id}", demoId)).andExpect(status().isNotFound());
	}

	@Test
	public void shouldCreateDemo() throws Exception {
		Demo newDemo = Demo.builder().name("Demo1").description("Demo Description").build();
		BDDMockito.given(service.createDemo(newDemo)).willAnswer(invocation -> invocation.getArgument(0));
		this.mockMvc
				.perform(post("/demos").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newDemo)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.name", is(newDemo.getName())))
				.andExpect(jsonPath("$.description", is(newDemo.getDescription())));
	}

	@Test
	public void shouldUpdateDemo() throws Exception {
		Long demoId = 1L;
		Demo demo = Demo.builder().id(demoId).name("Demo1 updated").description("Demo Description").build();
		BDDMockito.given(service.findDemoById(demoId)).willReturn(Optional.of(demo));
		BDDMockito.given(service.updateDemo(demo)).willAnswer(invocation -> invocation.getArgument(0));
		this.mockMvc
				.perform(put("/demos/{id}", demoId).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(demo)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name", is(demo.getName())))
				.andExpect(jsonPath("$.description", is(demo.getDescription())));
	}

	@Test
	void shouldReturn404WhenUpdatingNonExistingDemo() throws Exception {
		Long userId = 1L;
		Demo demoToUpdate = Demo.builder().id(10L).name("Demo to delete").description("demo description").build();
		BDDMockito.given(service.findDemoById(userId)).willReturn(Optional.empty());

		this.mockMvc.perform(put("/demos/{id}", userId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(demoToUpdate))).andExpect(status().isNotFound());
	}

	@Test
	public void shouldDeleteDemo() throws Exception {
		Long demoId = 1L;
		Demo demo = Demo.builder().id(demoId).name("Demo1 to delete").description("Demo Description").build();
		BDDMockito.given(service.findDemoById(demoId)).willReturn(Optional.of(demo));
		BDDMockito.doNothing().when(service).deleteDemoById(demoId);
		this.mockMvc.perform(delete("/demos/{id}", demoId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	void shouldReturn404WhenDeletingNonExistingDemo() throws Exception {
		Long demoId = 1L;
		BDDMockito.given(service.findDemoById(demoId)).willReturn(Optional.empty());
		this.mockMvc.perform(delete("/demos/{id}", demoId)).andExpect(status().isNotFound());
	}
}
