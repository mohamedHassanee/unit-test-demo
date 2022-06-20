package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.controller.model.Demo;
import com.example.demo.repository.DemoRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class DemoService {

	private final DemoRepository repo;

	public Demo createDemo(Demo demo) {
		return repo.save(demo);
	}

	public Demo updateDemo(Demo demo) {
		return repo.save(demo);
	}

	public List<Demo> findAllDemos() {
		return repo.findAll();
	}

	public Optional<Demo> findDemoById(Long id) {
		return repo.findById(id);
	}

	public void deleteDemoById(Long id) {
		repo.deleteById(id);
	}
}
