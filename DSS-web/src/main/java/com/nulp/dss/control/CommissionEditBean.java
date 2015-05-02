package com.nulp.dss.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.nulp.dss.dao.CommissionDao;
import com.nulp.dss.dao.GraduationDao;
import com.nulp.dss.dao.PersonDao;
import com.nulp.dss.dao.ProtectionDayDao;
import com.nulp.dss.model.Commission;
import com.nulp.dss.model.Graduation;
import com.nulp.dss.model.Person;
import com.nulp.dss.model.ProtectionDay;
import com.nulp.dss.model.enums.QuarterEnum;
import com.nulp.dss.util.HibernateUtil;

@ManagedBean
@ViewScoped
public class CommissionEditBean {
	
	private GraduationDao graduationDao = new GraduationDao();
	private PersonDao personDao = new PersonDao();
	private CommissionDao commissionDao = new CommissionDao();
	private ProtectionDayDao protectionDayDao = new ProtectionDayDao();
	
	
	private String year;
	private String quarter;
	private Map<String, String> years;
	private Map<String, String> quarters;
	
	private Boolean displayCommissions;
	
	private Person freePerson;
	private String freePersonId;
	private Map<String, Person> freePersons;
	private Map<String, String> freePersonIds;
	
	private Graduation graduation;
	private Commission commission;
	
	private String newHeadId;
	private String newSecretaryId;
	private String newPersonId;

	private List<ProtectionDay> protectionDayList;
	
	@PostConstruct
	public void init() {
		displayCommissions = false;
		protectionDayList = new ArrayList<ProtectionDay>(20);
		
		quarters = getQuartersMap(); 
		years = getYearsMap(graduationDao.getYearsList());
	}
	
	private Map<String, String> getYearsMap(List<Integer> yearsList) {
		Map<String, String> map = new HashMap<String, String>();
		
		for (Integer i: yearsList){
			map.put(i.toString(), i.toString());
		}
		
		return map;
	}

	private Map<String, String> getQuartersMap() {
		Map<String, String> map = new HashMap<String, String>();
		
		for (QuarterEnum qe: QuarterEnum.values()){
			map.put(qe.toString(), qe.name());
		}
		
		return map;
	}
	
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public Map<String, String> getYears() {
		return years;
	}

	public void setYears(Map<String, String> years) {
		this.years = years;
	}

	public Map<String, String> getQuarters() {
		return quarters;
	}

	public void setQuarters(Map<String, String> quarters) {
		this.quarters = quarters;
	}

	public Boolean getDisplayCommissions() {
		return displayCommissions;
	}

	public void setDisplayCommissions(Boolean displayCommissions) {
		this.displayCommissions = displayCommissions;
	}

	public Person getFreePerson() {
		return freePerson;
	}

	public void setFreePerson(Person freePerson) {
		this.freePerson = freePerson;
	}

	public String getFreePersonId() {
		return freePersonId;
	}

	public void setFreePersonId(String freePersonId) {
		this.freePersonId = freePersonId;
	}

	public Map<String, Person> getFreePersons() {
		return freePersons;
	}

	public void setFreePersons(Map<String, Person> freePersons) {
		this.freePersons = freePersons;
	}

	public Map<String, String> getFreePersonIds() {
		return freePersonIds;
	}

	public void setFreePersonIds(Map<String, String> freePersonIds) {
		this.freePersonIds = freePersonIds;
	}

	public Commission getCommission() {
		return commission;
	}

	public void setCommission(Commission commission) {
		this.commission = commission;
	}

	public String getNewHeadId() {
		return newHeadId;
	}

	public void setNewHeadId(String newHeadId) {
		this.newHeadId = newHeadId;
	}

	public String getNewSecretaryId() {
		return newSecretaryId;
	}

	public void setNewSecretaryId(String newSecretaryId) {
		this.newSecretaryId = newSecretaryId;
	}

	public String getNewPersonId() {
		return newPersonId;
	}

	public void setNewPersonId(String newPersonId) {
		this.newPersonId = newPersonId;
	}
	
	public void onYearChange() {
		quarter = "";
		displayCommissions = false;
	}
	
	public List<ProtectionDay> getProtectionDayList() {
		return protectionDayList;
	}

	public void setProtectionDayList(List<ProtectionDay> protectionDayList) {
		this.protectionDayList = protectionDayList;
	}
	

	public void onSeasonChange(){
		if (quarter != null && !quarter.isEmpty() && year != null && !year.isEmpty()){
			List<Graduation> list = graduationDao.getByYear(new Integer(year));
			
			QuarterEnum curentQ = QuarterEnum.valueOf(quarter);
			for (Graduation g: list){
				if (g.getQuarter().equals(curentQ)){
					graduation = g;
					commission = graduation.getCommission();
					HibernateUtil.lazyInitialize(commission , commission.getPersons());
					HibernateUtil.lazyInitialize(graduation, graduation.getProtectionDays());
//					loadSortedProtectionDays();
					loadProtectionDayList();
					break;
				}
			}
			
			newHeadId = "";
			newSecretaryId = "";
			newPersonId = "";
			displayCommissions = true;
			initFreePersons();
		}
		else{
			displayCommissions = false;
		}
	}

	private void loadProtectionDayList(){
		protectionDayList.clear();
		for (ProtectionDay protectionDay: graduation.getProtectionDays()){
			protectionDayList.add(protectionDay);
		}
	}
	
	
	private void initFreePersons(){
		String nullString = "-";
		freePersons = new HashMap<String, Person>();
		freePersonIds = new TreeMap<String, String>(new EnumManagerBean.EnumStringComparator(nullString, false));
		
		freePersons.put(nullString, null);
		freePersonIds.put(nullString, nullString);
		
		for (Person person: personDao.getAll()){
			if (!commission.getPersons().contains(person)){
				freePersons.put(person.getId().toString(), person);
				freePersonIds.put(person.getlName() + " " + person.getfName() + " " + person.getmName(), person.getId().toString());
			}
		}
		
		if (commission.getHead() != null){
			freePersons.remove(commission.getHead().getId());
			freePersonIds.remove(commission.getHead().getlName() + " " + commission.getHead().getfName() + " " + commission.getHead().getmName());
		}
		if (commission.getSecretary() != null){
			freePersons.remove(commission.getSecretary().getId());
			freePersonIds.remove(commission.getSecretary().getlName() + " " + commission.getSecretary().getfName() + " " + commission.getSecretary().getmName());
		}
	}
	
	
	public void deletePerson(Person person){
		commission.getPersons().remove(person);
		commissionDao.update(commission);
		
		freePersons.put(person.getId().toString(), person);
		freePersonIds.put(person.getlName() + " " + person.getfName() + " " + person.getmName(), person.getId().toString());
		
		newHeadId = "";
		newSecretaryId = "";
		newPersonId = "";
	}
	
	public String onCommissionHeadChange(){
		
		
		Person oldHead = commission.getHead();
		
		if (oldHead != null){
			freePersons.put(oldHead.getId().toString(), oldHead);
			freePersonIds.put(oldHead.getlName() + " " + oldHead.getfName() + " " + oldHead.getmName(), oldHead.getId().toString());
		}
		
		Person newHead = null;
		if (newHeadId != null && !newHeadId.isEmpty()){
			newHead = freePersons.get(newHeadId);
			
			freePersons.remove(newHead.getId().toString());
			freePersonIds.remove(newHead.getlName() + " " + newHead.getfName() + " " + newHead.getmName());
		}
		
		commission.setHead(newHead);
		commissionDao.update(commission);
		
		FacesMessage msg = new FacesMessage("Голову змінено.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		newHeadId = "";
		newSecretaryId = "";
		newPersonId = "";
		return null;
	}
	
	
	public String onCommissionSecretaryChange(){
		
		Person oldSecretary = commission.getSecretary();
		
		if (oldSecretary != null){
			freePersons.put(oldSecretary.getId().toString(), oldSecretary);
			freePersonIds.put(oldSecretary.getlName() + " " + oldSecretary.getfName() + " " + oldSecretary.getmName(), oldSecretary.getId().toString());
		}
		
		Person newSecretary = null;
		if (newSecretaryId != null && !newSecretaryId.isEmpty()){
			newSecretary = freePersons.get(newSecretaryId);
			
			freePersons.remove(newSecretary.getId().toString());
			freePersonIds.remove(newSecretary.getlName() + " " + newSecretary.getfName() + " " + newSecretary.getmName());
		}
		
		commission.setSecretary(newSecretary);
		commissionDao.update(commission);

		FacesMessage msg = new FacesMessage("Секретаря змінено.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		newHeadId = "";
		newSecretaryId = "";
		newPersonId = "";
		return null;
	}

	public String onCommissionPersonChange(){
		
		if (newPersonId != null && !newPersonId.isEmpty()){
			Person newPerson = freePersons.get(newPersonId);
			
			if (newPerson != null){
				commission.getPersons().add(newPerson);
				commissionDao.update(commission);
				
				freePersons.remove(newPerson.getId().toString());
				freePersonIds.remove(newPerson.getlName() + " " + newPerson.getfName() + " " + newPerson.getmName());
				
				FacesMessage msg = new FacesMessage("Члена комісії додано.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
		
		newHeadId = "";
		newSecretaryId = "";
		newPersonId = "";
		return null;
	}
	
	public void onProtectionDayDayUpdate(ProtectionDay day){
		if (day.getDay() != null){
			if (!HibernateUtil.isLazyInitialize(graduation.getProtectionDays())){
				HibernateUtil.lazyInitialize(graduation, graduation.getProtectionDays());
			}
			for (ProtectionDay protectionDay: graduation.getProtectionDays()){
				if (protectionDay != day && protectionDay.getDay() != null && protectionDay.getDay().equals(day.getDay())){
					HibernateUtil.refresh(day);
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "День захисту з такою датою вже існує!", "День захисту з такою датою вже існує!");
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return;
				}
			}
		}

		graduationDao.update(graduation);
		loadProtectionDayList();
		
		FacesMessage msg = new FacesMessage("День захисту змінено.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void onProtectionDayStartTimeUpdate(ProtectionDay day){
		chackStartAndEndTime(day, "Час початку захисту змінено.");
	}

	public void onProtectionDayEndTimeUpdate(ProtectionDay day){
		chackStartAndEndTime(day, "Час кінця захисту змінено.");
	}
	
	private void chackStartAndEndTime(ProtectionDay day, String successMessage){
		if (day.getStartTime() != null && day.getEndTime() != null && day.getStartTime().compareTo(day.getEndTime()) >= 0){
			HibernateUtil.refresh(day);
			FacesMessage msg = new FacesMessage("Час початку захисту пізніше ніж час кінця!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		else{
			graduationDao.update(graduation);
			FacesMessage msg = new FacesMessage(successMessage);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void deleteProtectionDay(ProtectionDay day){
		graduation.getProtectionDays().remove(day);

		graduationDao.update(graduation);
		
		protectionDayDao.delete(day);
		loadProtectionDayList();
		
		FacesMessage msg = new FacesMessage("День захисту видалено!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void addNewProtectionDay(){
		ProtectionDay newProtectionDay = new ProtectionDay();
		graduationDao.insertObj(newProtectionDay);
		HibernateUtil.refresh(newProtectionDay);
		
		graduation.getProtectionDays().add(newProtectionDay);
		graduationDao.update(graduation);

		loadProtectionDayList();
		
		FacesMessage msg = new FacesMessage("Новий день захисту додано!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

}



