package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.model.Demo;
import com.example.demo.service.DemoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/demos")
public class DemoController {

	private final DemoService service;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Demo creatDemo(@RequestBody Demo demo) {
		return service.createDemo(demo);
	}
	
	@GetMapping
	public List<Demo> findAllDemos() {
		return service.findAllDemos();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Demo> findDemoById(@PathVariable Long id) {
		return service.findDemoById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteDemo(@PathVariable Long id, HttpServletResponse response) {
		return service.findDemoById(id)
				.map(demoObj -> {
					service.deleteDemoById(id);
                    return ResponseEntity.noContent().build();
                })
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<Demo> updateDemo(@PathVariable Long id, @RequestBody Demo demo) {
        return service.findDemoById(id)
                .map(demoObj -> {
                    demo.setId(demoObj.getId());
                    return ResponseEntity.ok(service.updateDemo(demo));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
