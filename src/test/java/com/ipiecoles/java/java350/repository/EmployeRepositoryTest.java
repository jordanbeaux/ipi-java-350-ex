package com.ipiecoles.java.java350.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.repository.EmployeRepository;

@SpringBootTest
public class EmployeRepositoryTest {

	@Autowired
	EmployeRepository employeRepo;
	
	@BeforeEach //Fonction va s'excuter avant chaque test
	@AfterAll //Fonction va s'excuter après chaque test
	public void setup() {
		employeRepo.deleteAll(); // Vider la base pour une meilleure étanchéite (ne pas avoir de données parasites)
	}
	
	@Test
	public void testFindLastMatriculeIsNull() {
		
		// Given
		
		// When
		String lastMatricule = employeRepo.findLastMatricule();
		
		// Then
		Assertions.assertThat(lastMatricule).isNull();
	}
	
	@Test
	public void testFindLastMatricule2Employe() {
		
		// Given
		Employe e1 = new Employe();
		e1.setMatricule("M12345");
		employeRepo.save(e1);
		
		Employe e2 = new Employe();
		e2.setMatricule("T02345");
		employeRepo.save(e2);
		
		// When
		String result = employeRepo.findLastMatricule();

		
		// Then
		Assertions.assertThat(result).isEqualTo("12345");

	}
}
