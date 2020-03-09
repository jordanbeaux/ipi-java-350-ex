package com.ipiecoles.java.java350.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ipiecoles.java.java350.repository.EmployeRepository;

@SpringBootTest
public class EmployeRepositoryTest {

	@Autowired
	EmployeRepository employeRepo;
	
	@Test
	public void testFindLastMatricule() {
		
		// Given
		Employe e = new Employe();
		e.setMatricule("M12345");
		employeRepo.save(e);
		
		
		// When
		String result = employeRepo.findLastMatricule();
		
		// Then
		Assertions.assertThat(e.getMatricule().substring(1)).isEqualTo(result);
	}
}
