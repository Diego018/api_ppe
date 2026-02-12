package com.diego.api.service;


import com.diego.api.repository.IStudentRepository;
import com.example.demo.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final IStudentRepository iStudentRepository;

    public StudentService(IStudentRepository iStudentRepository) {

        this.iStudentRepository = iStudentRepository;

    }

    //Method for create student

    public Student createStudent(Student estudiante){

        return iStudentRepository.save(estudiante);

    }

    //List all students

    public List<Student> studentList () {

        return iStudentRepository.findAll();
    }

    // Get all students by ID

    public Student findById(Long id) {

        return iStudentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

    }

    //I update the student

    public Student updateStudent(Long id, Student nuevo) {

        Student existing = findById(id);

        existing.setName(nuevo.getName());
        existing.setCareer(nuevo.getCareer());

        return iStudentRepository.save(existing);


    }

    //Method for remove Student

    public void deleteStudent(Long id) {

        iStudentRepository.deleteById(id);

    }


}

