package com.ipiecoles.java.java350.service;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;

@ExtendWith(MockitoExtension.class) 
public class EmployeServiceTest {

	@InjectMocks
	private EmployeService employeService;
	
	@Mock
	private EmployeRepository employeRepo;
	
	@Test
	public void testEmbaucheEmploye() throws EmployeException{
		
		// Given
		String nom = "Wayne";
		String prenom = "Bruce";
		Poste poste = Poste.COMMERCIAL;
		NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
		Double tempsPartiel = 1.0;
		
		//findLastMatricule => 00345 / null
		Mockito.when(employeRepo.findLastMatricule()).thenReturn("00345");
		//findByMatricule => null
		Mockito.when(employeRepo.findByMatricule("C00346")).thenReturn(null);
		
		// When
		employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
		
		// Then (BDD si emmploye est bien créé (avec param)
		// Initialisation capteur d'argument
		ArgumentCaptor<Employe> employeCaptor= ArgumentCaptor.forClass(Employe.class);
		
		Mockito.verify(employeRepo, Mockito.times(1)).save(employeCaptor.capture()); // Mockiton.never() => vérifie qu'on n'est pas passé par une branche de l'app
		Employe e = employeCaptor.getValue();
		Assertions.assertThat(e.getNom()).isEqualTo(nom);	
		Assertions.assertThat(e.getPrenom()).isEqualTo(prenom);	
		Assertions.assertThat(e.getMatricule()).isEqualTo("C00346");
		Assertions.assertThat(e.getDateEmbauche()).isEqualTo(LocalDate.now());	
		Assertions.assertThat(e.getTempsPartiel()).isEqualTo(tempsPartiel);	
		Assertions.assertThat(e.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
		// 1521.22 * 1.2 * 1.0 => 1825.46
		Assertions.assertThat(e.getSalaire()).isEqualTo(1825.46);	
	}
	
	@Test
	public void testLimiteMatricule() throws EmployeException{
		
		// Given
		String nom = "Wayne";
		String prenom = "Bruce";
		Poste poste = Poste.COMMERCIAL;
		NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
		Double tempsPartiel = 1.0;
		
		//findLastMatricule => 00345 / null
		Mockito.when(employeRepo.findLastMatricule()).thenReturn("99999");
		
		// When
		try {
			employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
			Assertions.fail("ça aurait dû planter");
		}
		catch(Exception e){
			Assertions.assertThat(e).isInstanceOf(EmployeException.class);
			Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
		}
		
		
		
		// Then (que l'exception a bien été levée)
	}
	
}
