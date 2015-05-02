package com.nulp.dss.control;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.CellEditEvent;

import com.nulp.dss.dao.PersonDao;
import com.nulp.dss.model.Person;

@ManagedBean
@ViewScoped
public class PersonEditBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PersonDao personDao = new PersonDao(); 
	private List<Person> persons;
	private Person newPerson;
	
	@PostConstruct
	public void init() {
		persons = personDao.getAll();
		newPerson = new Person();
	}

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public Person getNewPerson() {
		return newPerson;
	}

	public void setNewPerson(Person newPerson) {
		this.newPerson = newPerson;
	}
	
	public void addPerson(){

		personDao.insert(newPerson);
		persons.add(newPerson);
		newPerson = new Person();
		
		FacesMessage msg = new FacesMessage("Члена комісії додано.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void deletePerson(Person personToDelete) {
		
		persons.remove(personToDelete);
		personDao.delete(personToDelete);
		
		FacesMessage msg = new FacesMessage("Члена комісії видалено.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void onPersonCellEdit(CellEditEvent event) {
		
		updateAllPersons();
		
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
         
        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Значення змінено. З " + oldValue + " у " + newValue, "Змінено");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
	
	private void updateAllPersons(){
		for (Person p: persons){
			personDao.update(p);
		}
	}
}
