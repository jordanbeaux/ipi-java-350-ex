package com.ipiecoles.java.java350.model;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class EmployeTest {
	
	// Scénario de test
		//Employe dateEmbauche avec date passée : 2 ans avant aujourd'hui => 2 ans ancienneté
	
	@Test
	public void testGetAnneeAnciennetePassee() {
		//Given = Initialisation des données d'entrée
			Employe employe = new Employe();
			employe.setDateEmbauche(LocalDate.now().minusYears(2));

		//When = Exécution de la méthode à tester
			Integer nbAnnees = employe.getNombreAnneeAnciennete();
		
		//Then = Vérifications de ce qu'a fait la méthode
			Assertions.assertThat(nbAnnees).isEqualTo(2);

	}
	
	// Scénario de test

			// Date dans le futur => 0 
		
	@Test
	public void testGetAnneeAncienneteFutur() {
		//Given = Initialisation des données d'entrée
			Employe employe = new Employe();
			employe.setDateEmbauche(LocalDate.now().plusYears(2));

		//When = Exécution de la méthode à tester
			Integer nbAnnees = employe.getNombreAnneeAnciennete();
		
		//Then = Vérifications de ce qu'a fait la méthode
			Assertions.assertThat(nbAnnees).isEqualTo(0);

	}
	
	// Scénario de test

			// Date actuelle => 0 
			
	@Test
	public void testGetAnneeAncienneteNow() {
		//Given = Initialisation des données d'entrée
			Employe employe = new Employe();
			employe.setDateEmbauche(LocalDate.now());

		//When = Exécution de la méthode à tester
			Integer nbAnnees = employe.getNombreAnneeAnciennete();
		
		//Then = Vérifications de ce qu'a fait la méthode
			Assertions.assertThat(nbAnnees).isEqualTo(0);

	}
			
	// Scénario de test

		// Date indéfinie => 0 
	
	@Test
	public void testGetAnneeAncienneteNull() {
		//Given = Initialisation des données d'entrée
			Employe employe = new Employe();
			employe.setDateEmbauche(null);

		//When = Exécution de la méthode à tester
			Integer nbAnnees = employe.getNombreAnneeAnciennete();
		
		//Then = Vérifications de ce qu'a fait la méthode
			Assertions.assertThat(nbAnnees).isEqualTo(0);

	}
	
	
	@ParameterizedTest(name = "matricile : {0}, performance {1} , nb anciennete {2} , temps partiel {3} , prime {4}")
	@CsvSource({ // Scénario de test
	        "'T12344', 1 , 0 , 1 , 1000.0", // Reprendre les spec FONCTIONNIELLES et faire coller le cas à notre scénario
	        "'T12344', 1 , 0 , 0.5 ,500.0",
	        "'M12344', 1 , 0 , 1 ,1700.0",
	        "'T12344', 2 , 0 , 1 ,2300.0",
	})
	
	// L'ordre dans le CsvSource doit être le même que dans les parametres de la fonction de test
	public void testPrimeAnnuelle(String matricule,  Integer performance, Integer nbAnneeAnciennete, Double tempsPartiel, Double prime) {
		
		//Given 
		Employe employe = new Employe();
		employe.setMatricule(matricule);
		employe.setPerformance(performance);
		employe.setDateEmbauche(LocalDate.now().minusYears(nbAnneeAnciennete));
		employe.setTempsPartiel(tempsPartiel);
		
		// When 
		Double primeCalculee = employe.getPrimeAnnuelle();
		
		// Then
	    Assertions.assertThat(primeCalculee).isEqualTo(prime);
	}
}
