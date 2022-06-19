package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.controller.model.Demo;
import com.example.demo.repository.DemoRepository;

@ExtendWith(MockitoExtension.class)
public class DemoServiceTest {

	@Mock
	private DemoRepository repo;

	@InjectMocks
	private DemoService service;

	@Test
	void shouldCreateDemo() {
		Demo demo = Demo.builder().name("Demo1").description("Demo description").build();
		given(repo.save(demo)).willAnswer(invocation -> invocation.getArgument(0));
		Demo savedDemo = service.createDemo(demo);
		assertThat(savedDemo).isNotNull();
		verify(repo).save(any(Demo.class));
	}

	@Test
	void shouldFindAllDemos() {
		List<Demo> list = Arrays.asList(Demo.builder().id(1L).name("Demo 1").description("Demo1 Desc").build(),
				Demo.builder().id(1L).name("Demo 2").description("Demo2 Desc").build());
		given(repo.findAll()).willReturn(list);
		assertEquals(service.findAllDemos(), list);
	}

	@Test
	void shouldFindDemoById() {
		Long demoId = 1L;
		Demo demo = Demo.builder().id(demoId).name("Demo1").description("Demo description").build();
		given(repo.findById(demoId)).willReturn(Optional.of(demo));
		Optional<Demo> expected = service.findDemoById(demoId);
		assertThat(expected).isNotNull();
	}

	@Test
	void shouldUpdateDemo() {
		Demo demo = Demo.builder().id(1L).name("Demo1").description("Demo description").build();
		given(repo.save(demo)).willReturn(demo);
		Demo expected = service.updateDemo(demo);
		assertThat(expected).isNotNull();
		verify(repo).save(any(Demo.class));
	}

	@Test
	void shouldDeleteDemoById() {
		Long demoId = 1L;
		service.deleteDemoById(demoId);
		service.deleteDemoById(demoId);
		verify(repo, times(2)).deleteById(demoId);
	}
}
