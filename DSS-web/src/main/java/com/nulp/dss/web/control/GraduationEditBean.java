package com.nulp.dss.web.control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.servlet.http.HttpSession;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.nulp.dss.dao.DiplomaDao;
import com.nulp.dss.dao.GraduationDao;
import com.nulp.dss.dao.PresentInProtectionDayDao;
import com.nulp.dss.docxmanaging.management.StudentsScheduleFormManager;
import com.nulp.dss.model.Commission;
import com.nulp.dss.model.Diploma;
import com.nulp.dss.model.Graduation;
import com.nulp.dss.model.PresentInProtectionDay;
import com.nulp.dss.model.ProtectionDay;
import com.nulp.dss.model.Student;
import com.nulp.dss.model.enums.QuarterEnum;
import com.nulp.dss.util.Transliterator;

@ManagedBean
@ViewScoped
public class GraduationEditBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GraduationDao graduationDao = new GraduationDao();
	private PresentInProtectionDayDao presentInProtectionDayDao = new PresentInProtectionDayDao();
	private DiplomaDao diplomaDao = new DiplomaDao();

	private String graduationStatistic;
	private Long graduationTotalStudents;
	private Long graduationOccupiedPlasseStudents;
	private String scheduleStatistic;
	private Long scheduleTotalPlasses;
	private Long scheduleOccupiedPlasses;
	
	private String year;
	private String quarter;
	private Map<String, String> years;
	private Map<String, String> quarters;
	
	private Boolean displayDaysList;
	private Boolean displayAllGraduationDay;
	
	private Graduation graduation;
	private Commission commission;
	
	private ProtectionDay protectionDay;
	private String protectionDayId;
	private Map<String, ProtectionDay> protectionDays;
	private Map<String, String> protectionDayIds;
	
//	private Diploma freeDiploma;
//	private String freeDiplomaId;
	private Map<String, Diploma> freeDiplomas;
	private Map<String, String> freeDiplomaIds;
	private Student student;
	private List<Student> students;
	
	private Integer numberOfCurrentlySelectedDiplomas;
	private Integer numberOfMaxPossibleDiploms;
	
	private StreamedContent studentsScheduleFile;
	private BufferedInputStream studentsScheduleFileStream;
	
	@PostConstruct
	public void init() {
		displayAllGraduationDay = false;

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
		if (studentsScheduleFileStream != null){
			try {
				studentsScheduleFileStream.close();
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

	public Graduation getGraduation() {
		return graduation;
	}

	public void setGraduation(Graduation graduation) {
		this.graduation = graduation;
	}

	public Commission getCommission() {
		return commission;
	}

	public void setCommission(Commission commission) {
		this.commission = commission;
	}

	public Boolean getDisplayDaysList() {
		return displayDaysList;
	}

	public void setDisplayDaysList(Boolean displayDaysList) {
		this.displayDaysList = displayDaysList;
	}

	public Boolean getDisplayAllGraduationDay() {
		return displayAllGraduationDay;
	}

	public void setDisplayAllGraduationDay(Boolean displayAllGraduationDay) {
		this.displayAllGraduationDay = displayAllGraduationDay;
	}

	public ProtectionDay getProtectionDay() {
		return protectionDay;
	}

	public void setProtectionDay(ProtectionDay protectionDay) {
		this.protectionDay = protectionDay;
	}

	public String getProtectionDayId() {
		return protectionDayId;
	}

	public void setProtectionDayId(String protectionDayId) {
		this.protectionDayId = protectionDayId;
	}

	public Map<String, ProtectionDay> getProtectionDays() {
		return protectionDays;
	}

	public void setProtectionDays(Map<String, ProtectionDay> protectionDays) {
		this.protectionDays = protectionDays;
	}

	public Map<String, String> getProtectionDayIds() {
		return protectionDayIds;
	}

	public void setProtectionDayIds(Map<String, String> protectionDayIds) {
		this.protectionDayIds = protectionDayIds;
	}

//	public Diploma getFreeDiploma() {
//		return freeDiploma;
//	}
//
//	public void setFreeDiploma(Diploma freeDiploma) {
//		this.freeDiploma = freeDiploma;
//	}
//
//	public String getFreeDiplomaId() {
//		return freeDiplomaId;
//	}
//
//	public void setFreeDiplomaId(String freeDiplomaId) {
//		this.freeDiplomaId = freeDiplomaId;
//	}

	public Map<String, Diploma> getFreeDiplomas() {
		return freeDiplomas;
	}

	public void setFreeDiplomas(Map<String, Diploma> freeDiplomas) {
		this.freeDiplomas = freeDiplomas;
	}

	public Map<String, String> getFreeDiplomaIds() {
		return freeDiplomaIds;
	}

	public void setFreeDiplomaIds(Map<String, String> freeDiplomaIds) {
		this.freeDiplomaIds = freeDiplomaIds;
	}
	
	public Integer getNumberOfCurrentlySelectedDiplomas() {
		return numberOfCurrentlySelectedDiplomas;
	}

	public void setNumberOfCurrentlySelectedDiplomas(
			Integer numberOfCurrentlySelectedDiplomas) {
		this.numberOfCurrentlySelectedDiplomas = numberOfCurrentlySelectedDiplomas;
	}

	public Integer getNumberOfMaxPossibleDiploms() {
		return numberOfMaxPossibleDiploms;
	}

	public void setNumberOfMaxPossibleDiploms(Integer numberOfMaxPossibleDiploms) {
		this.numberOfMaxPossibleDiploms = numberOfMaxPossibleDiploms;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getGraduationStatistic() {
		return graduationStatistic;
	}

	public void setGraduationStatistic(String graduationStatistic) {
		this.graduationStatistic = graduationStatistic;
	}

	public String getScheduleStatistic() {
		return scheduleStatistic;
	}

	public void setScheduleStatistic(String scheduleStatistic) {
		this.scheduleStatistic = scheduleStatistic;
	}

	public StreamedContent getStudentsScheduleFile() {
		return studentsScheduleFile;
	}

	public void onYearChange() {
		quarter = "";
		displayDaysList = false;
		displayAllGraduationDay = false;
	}
	
	
	public void onSeasonChange(){
		if (quarter != null && !quarter.isEmpty() && year != null && !year.isEmpty()){
			List<Graduation> list = graduationDao.getByYear(new Integer(year));
			
			QuarterEnum curentQ = QuarterEnum.valueOf(quarter);
			for (Graduation g: list){
				if (g.getQuarter().equals(curentQ)){
					graduation = g;
					commission = graduation.getCommission();
					graduationDao.loadGraduationAndCommissionAndProtectionDays(graduation);
					
					loadProtectionDayList();
					initFreeDiplomas();
					break;
				}
			}

			graduationTotalStudents = graduationDao.countAllGraduationStudents(graduation.getId());
			graduationOccupiedPlasseStudents = graduationDao.countAllGraduationStudentsWhatHaweDay(graduation.getId());
			scheduleTotalPlasses = calcScheduleTotalPlasses();
			scheduleOccupiedPlasses = graduationOccupiedPlasseStudents;
			countStatistic();
			
			displayDaysList = true;
		}
		else{
			displayDaysList = false;
		}
		
		displayAllGraduationDay = false;
	}
	
	private Long calcScheduleTotalPlasses() {
		long counter = 0;
		for (ProtectionDay pd: graduation.getProtectionDays()){
			if (pd.getStartTime() != null && pd.getEndTime() != null){
				counter += getNumbOfMaxDiplomas(pd);
			}
		}
		return counter;
	}

	private void loadProtectionDayList(){
		String nullString = "-";
		protectionDays = new HashMap<String, ProtectionDay>();
		protectionDayIds = new TreeMap<String, String>(new EnumManagerBean.EnumStringComparator(nullString, false));
		
		protectionDays.put(nullString, null);
		protectionDayIds.put(nullString, nullString);
		
		for (ProtectionDay protectionDay: graduation.getProtectionDays()){
			if (protectionDay.getStartTime() != null && protectionDay.getEndTime() != null){
				protectionDays.put(protectionDay.getId().toString(), protectionDay);
				protectionDayIds.put(protectionDay.getDay().toString(), protectionDay.getId().toString());
			}
		}
	}
	
	public void onProtectionDayChange(){
		protectionDay = protectionDays.get(protectionDayId);
		
		if (protectionDay != null){	
			graduationDao.loadForProtectionDay(graduation, protectionDay);
			initFreeDiplomas();
			countDiplomasNumbers();
			
			displayAllGraduationDay = true;
		}
		else{
			displayAllGraduationDay = false;
		}
	}
	
	private void countDiplomasNumbers(){
		numberOfCurrentlySelectedDiplomas = protectionDay.getDiplomas().size();
		numberOfMaxPossibleDiploms = getNumbOfMaxDiplomas();
	}
	
	private int getNumbOfMaxDiplomas(){
		long time = protectionDay.getEndTime().getTime() - protectionDay.getStartTime().getTime();
		return (int)(time/(1000 * 60 * 30));
	}
	
	private int getNumbOfMaxDiplomas(ProtectionDay localProtectionDay){
		long time = localProtectionDay.getEndTime().getTime() - localProtectionDay.getStartTime().getTime();
		return (int)(time/(1000 * 60 * 30));
	}
	
	public void onPresentPersonEdit(PresentInProtectionDay presentInProtectionDay){
		presentInProtectionDayDao.update(presentInProtectionDay);
		
		FacesMessage msg = new FacesMessage("Статус члена комісіъ змінено.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	private void initFreeDiplomas(){
		String nullString = "-";
		freeDiplomas = new HashMap<String, Diploma>();
		freeDiplomaIds = new TreeMap<String, String>(new EnumManagerBean.EnumStringComparator(nullString, false));
		students = new ArrayList<Student>();
		
		freeDiplomas.put(nullString, null);
		freeDiplomaIds.put(nullString, nullString);
		
		for (Diploma diploma: diplomaDao.getAllFreeByGraduationId(graduation, protectionDays.values())){
			freeDiplomas.put(diploma.getId().toString(), diploma);
			freeDiplomaIds.put(diploma.getStudent().getlName() + " " + diploma.getStudent().getfName() + " " + diploma.getStudent().getmName(), diploma.getId().toString());
			students.add(diploma.getStudent());
		}
		
		setSessionAttributes();
	}
	
	private void setSessionAttributes() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
    	HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
  
    	session.setAttribute("freeDiplomas", freeDiplomas);
    	session.setAttribute("freeDiplomaIds", freeDiplomaIds);
	}
	
	public void deleteDiploma(Diploma diploma){
		if (diploma != null){
			graduationDao.deleteDiplomaFromProtectionDay(diploma, protectionDay);
			
			freeDiplomas.put(diploma.getId().toString(), diploma);
			freeDiplomaIds.put(diploma.getStudent().getlName() + " " + diploma.getStudent().getfName() + " " + diploma.getStudent().getmName(), diploma.getId().toString());
			students.add(diploma.getStudent());
			numberOfCurrentlySelectedDiplomas--;
			
			graduationOccupiedPlasseStudents--;
			scheduleOccupiedPlasses = graduationOccupiedPlasseStudents;
			countStatistic();
			
			setSessionAttributes();
		}
	}
	
	public String addDiploma(){
		
		if ((numberOfCurrentlySelectedDiplomas < numberOfMaxPossibleDiploms) && student != null){
			Diploma diploma = student.getDiploma();
			graduationDao.addDiplomaToProtectionDay(diploma, protectionDay);
			
			freeDiplomas.remove(diploma.getId().toString());
			freeDiplomaIds.remove(diploma.getStudent().getlName() + " " + diploma.getStudent().getfName() + " " + diploma.getStudent().getmName());
			students.remove(student);
			student = null;
			numberOfCurrentlySelectedDiplomas++;
			
			graduationOccupiedPlasseStudents++;
			scheduleOccupiedPlasses = graduationOccupiedPlasseStudents;
			countStatistic();
			
			setSessionAttributes();
		}
		return null;
	}
	
	public List<Student> completeStudents(String query) {
        List<Student> filteredStudents = new ArrayList<Student>();
        query = query.toLowerCase();
        
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            if(student.displayFullName().toLowerCase().startsWith(query)) {
            	filteredStudents.add(student);
            }
        }
         
        return filteredStudents;
    }
	
	private void countStatistic() {
		graduationStatistic = "Студентам призначено день захисту - " + graduationOccupiedPlasseStudents + " з " + graduationTotalStudents;
		scheduleStatistic = "Занято днів захисту - " + scheduleOccupiedPlasses + " з " + scheduleTotalPlasses;
	}

	public void authoGenerateStudentsScedule(){
		Student student;
		while (graduationOccupiedPlasseStudents < graduationTotalStudents && scheduleOccupiedPlasses < scheduleTotalPlasses){

			for (ProtectionDay pd: protectionDays.values()){
				if (pd.getDiplomas().size() < getNumbOfMaxDiplomas(pd)){
					student = students.get(students.size() - 1);
					students.remove(students.size() - 1);
					freeDiplomas.remove(student.getDiploma().getId() + "");
					freeDiplomaIds.remove(student.displayFullName());
					
					pd.getDiplomas().add(student.getDiploma());
					student.getDiploma().setProtectionDay(pd);
					break;
				}
			}
			
			graduationOccupiedPlasseStudents++;
			scheduleOccupiedPlasses++;
		}
		
		graduationDao.update(graduation);
		countStatistic();
		FacesMessage msg = new FacesMessage("Студентів розподілено!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void downloadStudentsScheduleFile(){
		if (graduation != null){
			try {
				if (studentsScheduleFileStream != null){
					studentsScheduleFileStream.close();
					studentsScheduleFileStream = null;
				}
				String filePath = new StudentsScheduleFormManager().generateDocuments(graduation.getId());
				if (filePath == null || !new File(filePath).exists()){
					String message = "Не вдалося сформувати документ!";
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, message, message);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return;
				}
				
				studentsScheduleFileStream = new BufferedInputStream(new FileInputStream(filePath));
				studentsScheduleFile = new DefaultStreamedContent(studentsScheduleFileStream, "docx", getStudentSchedulemReadableFileName());
			} catch (Exception e) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Не вдалося сформувати документ!", "Не вдалося сформувати документ!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				e.printStackTrace();
			}
		}
		else{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Рік та сезон захисту обрано!", "Рік та сезон захисту обрано!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	private String getStudentSchedulemReadableFileName(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(graduation.getYear());
		
		String fileName = "Розклад для студентів за " + graduation.getQuarter() + " " + calendar.get(Calendar.YEAR) + ".docx";
		return Transliterator.transliterate(fileName);
	}
	
}
