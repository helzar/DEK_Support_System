package com.nulp.dss.web.control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.CellEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.nulp.dss.dao.CommissionDao;
import com.nulp.dss.dao.GraduationDao;
import com.nulp.dss.dao.PersonDao;
import com.nulp.dss.dao.ProtectionDayDao;
import com.nulp.dss.docxmanaging.management.ScheduleFormManager;
import com.nulp.dss.model.Commission;
import com.nulp.dss.model.Graduation;
import com.nulp.dss.model.Person;
import com.nulp.dss.model.ProtectionDay;
import com.nulp.dss.model.enums.QuarterEnum;
import com.nulp.dss.util.HibernateUtil;
import com.nulp.dss.util.Transliterator;

@ManagedBean
@ViewScoped
public class CommissionEditBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final long ONE_MINUTE_IN_MILLIS=60000;
	
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
	private String auditoriumToCopy;

	private List<ProtectionDay> protectionDayList;
	
	private StreamedContent scheduleFormFile;
	private BufferedInputStream scheduleFormStream;
	
	
	@PostConstruct
	public void init() {
		displayCommissions = false;
		protectionDayList = new ArrayList<ProtectionDay>(20);
		
		quarters = getQuartersMap(); 
		years = getYearsMap(graduationDao.getYearsList());
		
		setCurrentYearAndQuarter();
		onSeasonChange();
	}
	
	private void setCurrentYearAndQuarter() {
		LocalDate date = LocalDate.now();
		year = date.getYear() + "";
		
		int month = date.getMonth().getValue();
		if (month >= 8 || month <= 3){
			quarter = QuarterEnum.winter.name();
		} else{
			quarter = QuarterEnum.summer.name();
		}
	}
	
	@PreDestroy
	public void destroy() {  
		if (scheduleFormStream != null){
			try {
				scheduleFormStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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

	public StreamedContent getScheduleFormFile() {
		return scheduleFormFile;
	}

	public String getAuditoriumToCopy() {
		return auditoriumToCopy;
	}

	public void setAuditoriumToCopy(String auditoriumToCopy) {
		this.auditoriumToCopy = auditoriumToCopy;
	}
	

	public void onSeasonChange(){
		if (quarter != null && !quarter.isEmpty() && year != null && !year.isEmpty()){
			List<Graduation> list = graduationDao.getByYear(new Integer(year));
			
			QuarterEnum curentQ = QuarterEnum.valueOf(quarter);
			for (Graduation g: list){
				if (g.getQuarter().equals(curentQ)){
					graduation = g;
					commission = graduation.getCommission();
					HibernateUtil.lazyInitialize(commission, commission.getPersons());
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
			protectionDay.setNumberOfStudents(getNumberOfStudentsFromDate(protectionDay));
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
		day.setNumberOfStudents(getNumberOfStudentsFromDate(day));
	}

	public void onProtectionDayEndTimeUpdate(ProtectionDay day){
		chackStartAndEndTime(day, "Час кінця захисту змінено.");
		day.setNumberOfStudents(getNumberOfStudentsFromDate(day));
	}
	
	private Integer getNumberOfStudentsFromDate(ProtectionDay day){
		if (day.getStartTime() != null && day.getEndTime() != null){
			Calendar calendar = Calendar.getInstance();

			calendar.setTime(day.getStartTime());
			int startHour = calendar.get(Calendar.HOUR_OF_DAY);
			int startMinute = calendar.get(Calendar.MINUTE);
				
			calendar.setTime(day.getEndTime());
			int endHour = calendar.get(Calendar.HOUR_OF_DAY);
			int endMinute = calendar.get(Calendar.MINUTE);

			int totalStudents = (endHour - startHour) * 2;
			if (startMinute != 0){
				totalStudents--;
			}
			if (endMinute != 0){
				totalStudents++;
			}
			
			return totalStudents;
		}
		else{
			return null;
		}
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
	
	public void downloadScheduleForm(){
		if (graduation != null){
			try {
				if (scheduleFormStream != null){
					scheduleFormStream.close();
					scheduleFormStream = null;
				}
				String filePath = new ScheduleFormManager().generateDocuments(graduation.getId());
				if (filePath == null || !new File(filePath).exists()){
					String message = "Не вдалося сформувати документ, захист не обрано!";
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, message, message);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return;
				}
				
				scheduleFormStream = new BufferedInputStream(new FileInputStream(filePath));
				scheduleFormFile = new DefaultStreamedContent(scheduleFormStream, "docx", getScheduleFormReadableFileName());
			} catch (Exception e) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Не вдалося сформувати документ!", "Не вдалося сформувати документ!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				e.printStackTrace();
			}
		}
		else{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Рецензента не обрано!", "Рецензента не обрано!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	private String getScheduleFormReadableFileName(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(graduation.getYear());
		
		String fileName = "Розклад роботи ДЕК за " + graduation.getQuarter().toString() + " " + calendar.get(Calendar.YEAR) + ".docx";
		return Transliterator.transliterate(fileName);
	}
	
	
	public void onSheduleCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Значення змінено. З " + oldValue + " у " + newValue, "Змінено");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

		updateAllSheduleDays();
    }

	public void onSheduleCellEdit(ProtectionDay day) {
    	onStudenNumberChange(day);
		updateAllSheduleDays();
    }
	
	private void updateAllSheduleDays(){
		for (ProtectionDay pd: protectionDayList){
			protectionDayDao.update(pd);
		}
	}
	
	public void copyAuditorium(){
		if (auditoriumToCopy != null && !auditoriumToCopy.isEmpty()){
			for (ProtectionDay pd: protectionDayList){
				pd.setAuditorium(auditoriumToCopy);
				protectionDayDao.update(pd);
			}
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Аудиторія для всіх днів змінена на " + auditoriumToCopy, "Змінено");
            FacesContext.getCurrentInstance().addMessage(null, msg);
		} else{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Аудиторію не можливо змінити, стрічка пуста!", "Аудиторію не можливо змінити, стрічка пуста!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
	}

	private void onStudenNumberChange(ProtectionDay day){
		if (day.getStartTime() == null){
			day.setNumberOfStudents(0);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Час початку не встановлено!", "Час початку не встановлено!");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		} else if (day.getNumberOfStudents() <= 0){
			day.setNumberOfStudents(0);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Кількість студентів має бути більшою нуля!", "Кількість студентів має бути більшою нуля!");
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		} else{
			day.setEndTime(getEndDate(day));
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Новий час кінця та кількість студентів встановлено", "Змінено");
            FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	private Date getEndDate(ProtectionDay day) {
		long time = day.getStartTime().getTime();
		return new Date(time + (day.getNumberOfStudents() * 30 * ONE_MINUTE_IN_MILLIS));
	}
	
}



