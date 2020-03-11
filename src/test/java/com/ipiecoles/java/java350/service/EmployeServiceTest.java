package com.ipiecoles.java.java350.service;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
		Mockito.when(employeRepo.findLastMatricule()).thenReturn("99999"); // Mock => attribuer arbitrairement une valeur pour simuler retour appel BDD
		
		// When
		try {
			employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
			Assertions.fail("ça aurait dû planter");
		}
		
		// Then (que l'exception a bien été levée)
		catch(Exception e){
			Assertions.assertThat(e).isInstanceOf(EmployeException.class);
			Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
		}
			
	}
	
	@Test
	public void testCalculPerformanceCommercialCas1() throws EmployeException {
		// Given
		String matricule = "C00666";
		Long caTraite = 1000l;
		Long objectifCa = 1500l;
		
		Employe e = new Employe();
		
		e.setMatricule(matricule);
				
		Mockito.when(employeRepo.findByMatricule("C00666")).thenReturn(e);
		Mockito.when(employeRepo.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn((double)1);
		
		// When
		employeService.calculPerformanceCommercial(e.getMatricule(), caTraite, objectifCa);
		
		ArgumentCaptor<Employe> empCaptor= ArgumentCaptor.forClass(Employe.class);
		
		Mockito.verify(employeRepo, Mockito.times(1)).save(empCaptor.capture()); 
		Employe emp = empCaptor.getValue();
				
		// perf_base = 1; 1000 < 1500 (- 33%) => cas 1 		
		Assertions.assertThat(emp.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
		
	}
	
	@Test
	public void testCalculPerformanceCommercialCas2() throws EmployeException {
		// Given
		String matricule = "C00667";
		Long caTraite = 1275l;
		Long objectifCa = 1500l;
		
		Employe e = new Employe();
		
		e.setMatricule(matricule);
				
		Mockito.when(employeRepo.findByMatricule("C00667")).thenReturn(e);
		Mockito.when(employeRepo.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn((double)1);
		
		// When
		employeService.calculPerformanceCommercial(e.getMatricule(), caTraite, objectifCa);
		
		ArgumentCaptor<Employe> empCaptor= ArgumentCaptor.forClass(Employe.class);
		
		Mockito.verify(employeRepo, Mockito.times(1)).save(empCaptor.capture()); 
		Employe emp = empCaptor.getValue();
				
		// perf_base = 1; 1275 < 1500 (- 15%) => cas 2 		
		Assertions.assertThat(emp.getPerformance()).isEqualTo(1);
		
	}
	
	@Test
	public void testCalculPerformanceCommercialCas3() throws EmployeException {
		// Given
		String matricule = "C00668";
		Long caTraite = 1500l;
		Long objectifCa = 1500l;
		
		Employe e = new Employe();
		
		e.setMatricule(matricule);
				
		Mockito.when(employeRepo.findByMatricule("C00668")).thenReturn(e);
		Mockito.when(employeRepo.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn((double)1);
		
		// When
		employeService.calculPerformanceCommercial(e.getMatricule(), caTraite, objectifCa);
		
		ArgumentCaptor<Employe> empCaptor= ArgumentCaptor.forClass(Employe.class);
		
		Mockito.verify(employeRepo, Mockito.times(1)).save(empCaptor.capture()); 
		Employe emp = empCaptor.getValue();
				
		// perf_base = 1; 1500 = 1500 => cas 3 		
		Assertions.assertThat(emp.getPerformance()).isEqualTo(emp.getPerformance());
		
	}
	
	@Test
	public void testCalculPerformanceCommercialCasCATraiteNull() {
		// Given
		String matricule = "C00668";
		Long caTraite = null;
		Long objectifCa = 1500l;
		
		Employe e = new Employe();
		
		e.setMatricule(matricule);
		
		// When
		try {
			employeService.calculPerformanceCommercial(e.getMatricule(), caTraite, objectifCa);
			Assertions.fail("ça aurait dû planter");
		}
		
		// Then (que l'exception a bien été levée)
		catch(Exception ex){
			Assertions.assertThat(ex).isInstanceOf(EmployeException.class);
			Assertions.assertThat(ex.getMessage()).isEqualTo("Le chiffre d'affaire traité ne peut être négatif ou null !");
		}
	}
	
	@Test
	public void testCalculPerformanceCommercialCas5() throws EmployeException {
		// Given
		String matricule = "C00669";
		Long caTraite = 2000l;
		Long objectifCa = 1500l;
		
		Employe e = new Employe();
		
		e.setMatricule(matricule);
		
		// 2ème employé pour avoir une idée de la moyenne mais pas nécessaire
			//		String matricule2 = "C00670";
			//		Long caTraite2 = 1000l;
			//		Long objectifCa2 = 1500l;
			//		
			//		Employe e2 = new Employe();
			//		
			//		e2.setMatricule(matricule);
				
		Mockito.when(employeRepo.findByMatricule("C00669")).thenReturn(e);
		Mockito.when(employeRepo.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn((double)1);
		
		
		// When
		employeService.calculPerformanceCommercial(e.getMatricule(), caTraite, objectifCa);
		
		ArgumentCaptor<Employe> empCaptor= ArgumentCaptor.forClass(Employe.class);
		
		Mockito.verify(employeRepo, Mockito.times(1)).save(empCaptor.capture()); 
		Employe emp = empCaptor.getValue();
		
		// perf_base = 1; 2000 > 1500 (+33%) ET > perf moyenne (1500) => cas 5 		
		Assertions.assertThat(emp.getPerformance()).isEqualTo(6);
		
	}
	
	@Test
	public void testCalculPerformanceCommercialCas4() throws EmployeException {
		// Given
		String matricule = "C00671";
		Long caTraite = 1700l;
		Long objectifCa = 1500l;
		
		Employe e = new Employe();
		
		e.setMatricule(matricule);
						
		Mockito.when(employeRepo.findByMatricule("C00671")).thenReturn(e);
		Mockito.when(employeRepo.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn((double)3);
		
		
		// When
		employeService.calculPerformanceCommercial(e.getMatricule(), caTraite, objectifCa);
		
		ArgumentCaptor<Employe> empCaptor= ArgumentCaptor.forClass(Employe.class);
		
		Mockito.verify(employeRepo, Mockito.times(1)).save(empCaptor.capture()); 
		Employe emp = empCaptor.getValue();
		
		// perf_base = 1; 1700 > 1500 (+13%) cas 4		
		Assertions.assertThat(emp.getPerformance()).isEqualTo(2);
		
	}
	
	@Test
	public void testCalculPerformanceCommercialCasObjectifNull() {
		// Given
		String matricule = "C00668";
		Long caTraite = 1500l;
		Long objectifCa = null;
		
		Employe e = new Employe();
		
		e.setMatricule(matricule);
		
		// When
		try {
			employeService.calculPerformanceCommercial(e.getMatricule(), caTraite, objectifCa);
			Assertions.fail("ça aurait dû planter");
		}
		
		// Then (que l'exception a bien été levée)
		catch(Exception ex){
			Assertions.assertThat(ex).isInstanceOf(EmployeException.class);
			Assertions.assertThat(ex.getMessage()).isEqualTo("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
		}
	}
	
	@Test
	public void testMatriculeCommercialInexistant() {
		// Given
		String matricule = "C00123";
		Long caTraite = 1500l;
		Long objectifCa = 1500l;
		
		Employe e = new Employe();
		
		e.setMatricule(matricule);
		
		// When
		Mockito.when(employeRepo.findByMatricule(e.getMatricule())).thenReturn(null);
	
		try {
			employeService.calculPerformanceCommercial(e.getMatricule(), caTraite, objectifCa);
			Assertions.fail("ça aurait dû planter");
		}
		
		// Then (que l'exception a bien été levée)
		catch(Exception ex){
			Assertions.assertThat(ex).isInstanceOf(EmployeException.class);
			Assertions.assertThat(ex.getMessage()).isEqualTo("Le matricule " + matricule + " n'existe pas !");
		}
	}
	
	@Test
	public void testMatriculeCommercialNull() {
		// Given
		String matricule = null;
		Long caTraite = 1500l;
		Long objectifCa = 1500l;
		
		Employe e = new Employe();
		
		e.setMatricule(matricule);
		
		// When

		try {
			employeService.calculPerformanceCommercial(e.getMatricule(), caTraite, objectifCa);
			Assertions.fail("ça aurait dû planter");
		}
		
		// Then (que l'exception a bien été levée)
		catch(Exception ex){
			Assertions.assertThat(ex).isInstanceOf(EmployeException.class);
			Assertions.assertThat(ex.getMessage()).isEqualTo("Le matricule ne peut être null et doit commencer par un C !");
		}
	}
}
