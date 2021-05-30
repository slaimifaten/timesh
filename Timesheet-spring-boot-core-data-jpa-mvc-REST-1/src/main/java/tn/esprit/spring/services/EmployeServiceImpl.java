package tn.esprit.spring.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.TimesheetRepository;

@Service
public class EmployeServiceImpl implements IEmployeService {

	@Autowired
	EmployeRepository employeRepository;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	ContratRepository contratRepoistory;
	@Autowired
	TimesheetRepository timesheetRepository;

	public int ajouterEmploye(Employe employe) {
		employeRepository.save(employe);
		return employe.getId();
	}


	public void mettreAjourEmailByEmployeId(String email, int employeId) {
		Optional<Employe> employeOptional = employeRepository.findById(employeId);
		
		if(employeOptional.isPresent()){
			Employe employe = employeOptional.get();
			employe.setEmail(email);
			employeRepository.save(employe);
		}

	}

	@Transactional	
	public void affecterEmployeADepartement(int employeId, int depId)  {
		
		Optional<Departement> departementOptional = deptRepoistory.findById(depId); 
		Optional<Employe> employeOptional = employeRepository.findById(employeId);
		
		if(departementOptional.isPresent() && employeOptional.isPresent()){
			Departement departement = departementOptional.get();
			Employe employe = employeOptional.get();
			
			if(departement.getEmployes().isEmpty()||departement.getEmployes() == null){
				List<Employe> employes = new ArrayList<>();
				employes.add(employe);
				departement.setEmployes(employes);
			}else{
				departement.getEmployes().add(employe);
			}
		}
	}
	@Transactional
	public void desaffecterEmployeDuDepartement(int employeId, int depId)
	{
		
		Optional<Departement> departementOptional = deptRepoistory.findById(depId);
		
		if (departementOptional.isPresent()){
			Departement departement = departementOptional.get();
			int employeNb = departement.getEmployes().size();
			for(int index = 0; index < employeNb; index++){
				if(departement.getEmployes().get(index).getId() == employeId){
					departement.getEmployes().remove(index);
					break;//a revoir
				}
			}
		}

		
	}

	public int ajouterContrat(Contrat contrat) {
		contratRepoistory.save(contrat);
		return contrat.getReference();
	}

	public void affecterContratAEmploye(int contratId, int employeId) {
		
		Optional<Contrat> contratOptional = contratRepoistory.findById(contratId);
		Optional<Employe> employeOptional = employeRepository.findById(employeId);
		if(contratOptional.isPresent() && employeOptional.isPresent()){
			Contrat contrat = contratOptional.get();
			Employe employe = employeOptional.get();
			
			contrat.setEmploye(employe);
			contratRepoistory.save(contrat);
		}

		
	}

	public String getEmployePrenomById(int employeId) {
		
		Optional<Employe> employeOptional = employeRepository.findById(employeId);
		if(employeOptional.isPresent()){
			Employe employe = employeOptional.get();
			return employe.getPrenom();
		}
		return null;
	}
	
	public void deleteEmployeById(int employeId)
	{

		Optional<Employe> employeOptional = employeRepository.findById(employeId);
		if(employeOptional.isPresent()){
			Employe employe = employeOptional.get();
			//Desaffecter l'employe de tous les departements
			//c'est le bout master qui permet de mettre a jour
			//la table d'association
			for(Departement dep : employe.getDepartements()){
				dep.getEmployes().remove(employe);
			}

			employeRepository.delete(employe);
		}

		
	}

	public void deleteContratById(int contratId) {
		Optional<Contrat> contratOptional = contratRepoistory.findById(contratId);
		if(contratOptional.isPresent()){
			Contrat contrat = contratOptional.get();
			contratRepoistory.delete(contrat);
		}	
	}

	public int getNombreEmployeJPQL() {
		return employeRepository.countemp();
	}
	
	public List<String> getAllEmployeNamesJPQL() {
		return employeRepository.employeNames();

	}
	
	public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise) {
		return employeRepository.getAllEmployeByEntreprisec(entreprise);
	}

	public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId) {
		employeRepository.mettreAjourEmailByEmployeIdJPQL(email, employeId);

	}
	public void deleteAllContratJPQL() {
         employeRepository.deleteAllContratJPQL();
	}
	
	public float getSalaireByEmployeIdJPQL(int employeId) {
		return employeRepository.getSalaireByEmployeIdJPQL(employeId);
	}

	public Double getSalaireMoyenByDepartementId(int departementId) {
		return employeRepository.getSalaireMoyenByDepartementId(departementId);
	}
	
	public List<Timesheet> getTimesheetsByMissionAndDate(Employe employe, Mission mission, Date dateDebut,
			Date dateFin) {
		return timesheetRepository.getTimesheetsByMissionAndDate(employe, mission, dateDebut, dateFin);
	}

	public List<Employe> getAllEmployes() {
				return (List<Employe>) employeRepository.findAll();
	}

}
