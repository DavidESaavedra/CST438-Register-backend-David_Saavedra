package com.cst438.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.cst438.domain.StudentDTO;

@RestController
public class StudentController {
	@Autowired
	StudentRepository studentRepository;

	@PostMapping("/student")
	@Transactional
	public Student addStudent(@RequestBody StudentDTO studentDTO){
		Student emailCheck = studentRepository.findByEmail(studentDTO.email);
		
		if(studentDTO != null && emailCheck == null){
			Student student = new Student();
			student.setName(studentDTO.name);
			student.setEmail(studentDTO.email);
			Student saveStudent = studentRepository.save(student);
			return saveStudent;
		}else{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A student with that email already exist");
		}
	}
	
	@PutMapping("/student/{id}")
	@Transactional
	public Student changeHold(@PathVariable int id){
		String placeHolder ="Reason for being placed on hold";
		Student student = studentRepository.findById(id).get();

		if(student != null){
			if(student.getStatusCode() == 0){
				student.setStatus(placeHolder);
				student.setStatusCode(1);
				Student saveStudent = studentRepository.save(student);
				return saveStudent;
			}else{
				student.setStatus(null);
				student.setStatusCode(0);
				Student saveStudent = studentRepository.save(student);
				return saveStudent;
			}
		}else{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student was not found");
		}
	}
}
