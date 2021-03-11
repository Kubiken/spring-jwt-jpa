package com.example.springBootTest.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

	private StudentService studentService;

	public StudentController(@Autowired StudentService studentService){
		this.studentService = studentService;
	}

	@GetMapping(path = "get")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
	public List<Student> studentos(){
		return studentService.getStudents();
	}

	@PostMapping(path = "add")
	@PreAuthorize("hasAuthority('student:write')")
	public void registerStudent(@RequestBody Student student){
		studentService.addNewStudent(student);
	}

	@DeleteMapping(path = "delete/{studentId}")
	@PreAuthorize("hasAuthority('student:write')")
	public void deleteStudent(@PathVariable("studentId") Long id){
		studentService.deleteStudent(id);
	}

	@PutMapping(path="update/{studentId}")
	@PreAuthorize("hasAuthority('student:write')")
	public void updateNameAndEmail(@PathVariable("studentId") Long studentId,
								   @RequestParam(required = false) String name,
								   @RequestParam(required = false) String email)
	{
		studentService.updateStudent(studentId, name, email);
	}

}
