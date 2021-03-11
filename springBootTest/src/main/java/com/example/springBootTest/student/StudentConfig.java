package com.example.springBootTest.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class StudentConfig {

	@Bean
	CommandLineRunner commandLineRunner(StudentRepository repository){
		return args -> {
			Student vaskez = new Student(
					"Vaskez",
					"ncpd2252342@pd.nc",
					LocalDate.of(1997, Month.JUNE, 21));

			Student petrova = new Student(
					"Petrova",
					"ncpd33452345@pd.nc",
					LocalDate.of(2002, Month.DECEMBER, 8));

			repository.saveAll(
					List.of(vaskez, petrova)
			);
		};

	}
}
