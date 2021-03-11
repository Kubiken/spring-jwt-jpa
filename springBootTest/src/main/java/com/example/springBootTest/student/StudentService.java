package com.example.springBootTest.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

	private final StudentRepository studentRepository;

	@Autowired
	public StudentService(StudentRepository studentRepository){
		this.studentRepository = studentRepository;
	}

	public List<Student> getStudents(){
		return studentRepository.findAll();
	}

	public void addNewStudent(Student student) {
		Optional<Student>studentByEmail =
				studentRepository.findStudentByEmail(student.getEmail());
		if(studentByEmail.isPresent()){
			throw new IllegalStateException("Student already exists");
		}else {studentRepository.saveAndFlush(student);}
		System.out.println(student);
	}

	public void deleteStudent(Long studentId) {
		if(!studentRepository.existsById(studentId)){
			throw new IllegalStateException("Student with "+studentId+" does not exists");
		}
		studentRepository.deleteById(studentId);
	}

	@Transactional
	public void updateStudent(Long studentId, String name, String email){

		Student student = studentRepository.findById(studentId)
				.orElseThrow(()->new IllegalStateException("User with id"+studentId+" doen not exists"));

		if(name != null
				&& !student.getName().equals(name)
				&& name.length()>0) student.setName(name);
		else {throw new IllegalStateException("Corrupted parameter name: "+name);}

		if(email != null
				&& !student.getEmail().equals(email)
				&& email.length()>0) student.setEmail(email);
		else {throw new IllegalStateException("Corrupted parameter email: "+email);}

	}
}
