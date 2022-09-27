package com.cst438;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cst438.controller.StudentController;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@ContextConfiguration(classes = {StudentController.class})
@WebMvcTest
public class JunitTestStudent {
	public static final int TEST_STUDENT_ID = 0;
	public static final String TEST_STUDENT_NAME = "test";
	public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";
	public static final String TEST_STATUS = null;
	public static final int TEST_STATUS_CODE = 0;

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	StudentRepository studentRepository;
	
	@Test
	public void addStudent() throws Exception{
		MockHttpServletResponse	response;
		
		StudentDTO testDTO = new StudentDTO();
		testDTO.name = TEST_STUDENT_NAME;
		testDTO.email = TEST_STUDENT_EMAIL;
		response = mvc.perform(
						MockMvcRequestBuilders
					      .post("/student")
					      .content(asJsonString(testDTO))
					      .contentType(MediaType.APPLICATION_JSON)
					      .accept(MediaType.APPLICATION_JSON))
						.andReturn().getResponse();

		assertEquals(200, response.getStatus());
		
		Student dupStudent = new Student();
		dupStudent.setName(TEST_STUDENT_NAME);
		dupStudent.setEmail(TEST_STUDENT_EMAIL);
		
		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(dupStudent);
		verify(studentRepository).save(any(Student.class));
		response = mvc.perform(
				MockMvcRequestBuilders
			      .post("/student")
			      .content(asJsonString(dupStudent))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void holdChange() throws Exception{
		MockHttpServletResponse	response;

		Student student = new Student();
		student.setStudent_id(TEST_STUDENT_ID);
		student.setName(TEST_STUDENT_NAME);
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setStatus(TEST_STATUS);
		student.setStatusCode(TEST_STATUS_CODE);

		given(studentRepository.findById(TEST_STUDENT_ID)).willReturn(Optional.of(student));
		response = mvc.perform(
				MockMvcRequestBuilders
			      .put("/student/0"))
				.andReturn().getResponse();

		assertEquals(200, response.getStatus());
	}
	
	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
