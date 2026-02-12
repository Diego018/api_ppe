package com.diego.api.controller;

import com.diego.api.service.StudentService;
import com.example.demo.entity.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {

        this.studentService = studentService;

    }

    // GET ALL
    @GetMapping
    public List<Student> getAll() {

        return studentService.studentList();

    }

    // GET BY ID
    @GetMapping("/{id}")

    public Student getById(@PathVariable Long id) {
        return studentService.findById(id);

    }

    // CREATE
    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student) {

        Student newStudent = studentService.createStudent(student);

        return ResponseEntity.status(201).body(newStudent);

    }

    // UPDATE
    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student student) {

        return studentService.updateStudent(id, student);

    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        studentService.deleteStudent(id);

    }

}

