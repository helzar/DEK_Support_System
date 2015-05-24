package com.nulp.dss.web.control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.nulp.dss.dao.DiplomaDao;
import com.nulp.dss.dao.GraduationDao;
import com.nulp.dss.dao.GroupDao;
import com.nulp.dss.dao.StudentDao;
import com.nulp.dss.docxmanaging.reader.StudentFileReader;
import com.nulp.dss.excelmanaging.management.DEKResultTableManager;
import com.nulp.dss.model.Commission;
import com.nulp.dss.model.Diploma;
import com.nulp.dss.model.DiplomaInfo;
import com.nulp.dss.model.Graduation;
import com.nulp.dss.model.Group;
import com.nulp.dss.model.Review;
import com.nulp.dss.model.Student;
import com.nulp.dss.model.enums.QuarterEnum;
import com.nulp.dss.util.HibernateUtil;
import com.nulp.dss.util.Transliterator;

@ManagedBean
@ViewScoped
public class DiplomasEditBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TEMP_STUDENT_INFO_FILE_NAME = "temp_studs_file";
	private static final String TEMP_STUDENT_INFO_FILE_EXTENSION = "doc";
	
	private GraduationDao graduationDao = new GraduationDao();
	private GroupDao groupDao = new GroupDao();
	private StudentDao studentDao = new StudentDao();
	private DiplomaDao diplomaDao = new DiplomaDao();
	
	private String year;
	private String quarter;
	private String groupName;
	private Map<String, String> years;
	private Map<String, String> quarters;
	private Map<String, String> groups;
//	private Map<String, Reviewer> reviewers;

	private String newGraduationYear;
	private String newGroupName;
	private Boolean displayGroups;
	private Boolean displayStudents;
	
	private Group groupObj;
	private Graduation graduation;
	
	private Student newStudent;
	private Diploma newDiploma;
	private DiplomaInfo newDiplomaInfo;
	private Review newReview;

	private Integer selectedRow;
	private String studentsDocName;
	private Part diplomasFile;
	
	private StreamedContent statisticFile;
	private BufferedInputStream statisticFileStream;
	

	@PostConstruct
	public void init() {
		displayGroups = false;
		displayStudents = false;
		
		newStudent = new Student();
		newDiploma = new Diploma();
		newDiplomaInfo = new DiplomaInfo();
		newReview = new Review();
		
		quarters = getQuartersMap(); 
		years = getYearsMap(graduationDao.getYearsList());
	}
	
	@PreDestroy
	public void destroy() {  
		if (statisticFileStream != null){
			try {
				statisticFileStream.close();
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
	
	
	
	public String getNewGraduationYear() {
		return newGraduationYear;
	}

	public void setNewGraduationYear(String newGraduationYear) {
		this.newGraduationYear = newGraduationYear;
	}

	public Boolean getDisplayGroups() {
		return displayGroups;
	}

	public void setDisplayGroups(Boolean displayGroups) {
		this.displayGroups = displayGroups;
	}

	public Boolean getDisplayStudents() {
		return displayStudents;
	}

	public void setDisplayStudents(Boolean displayStudents) {
		this.displayStudents = displayStudents;
	}

	public Graduation getGraduation() {
		return graduation;
	}

	public void setGraduation(Graduation graduation) {
		this.graduation = graduation;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Map<String, String> getGroups() {
		return groups;
	}

	public void setGroups(Map<String, String> groups) {
		this.groups = groups;
	}

	public Group getGroupObj() {
		return groupObj;
	}

	public void setGroupObj(Group groupObj) {
		this.groupObj = groupObj;
	}

	public String getNewGroupName() {
		return newGroupName;
	}

	public void setNewGroupName(String newGroupName) {
		this.newGroupName = newGroupName;
	}

	public Integer getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(Integer selectedRow) {
		this.selectedRow = selectedRow;
	}

	public Student getNewStudent() {
		return newStudent;
	}

	public void setNewStudent(Student newStudent) {
		this.newStudent = newStudent;
	}

	public Diploma getNewdDiploma() {
		return newDiploma;
	}

	public void setNewdDiploma(Diploma newDiploma) {
		this.newDiploma = newDiploma;
	}

	public DiplomaInfo getNewDiplomaInfo() {
		return newDiplomaInfo;
	}

	public void setNewDiplomaInfo(DiplomaInfo newDiplomaInfo) {
		this.newDiplomaInfo = newDiplomaInfo;
	}

	public Review getNewReview() {
		return newReview;
	}

	public void setNewReview(Review newReview) {
		this.newReview = newReview;
	}

	public String getStudentsDocName() {
		return studentsDocName;
	}

	public void setStudentsDocName(String studentsDocName) {
		this.studentsDocName = studentsDocName;
	}

	public Part getDiplomasFile() {
		return diplomasFile;
	}

	public void setDiplomasFile(Part diplomasFile) {
		this.diplomasFile = diplomasFile;
	}

	public StreamedContent getStatisticFile() {
		return statisticFile;
	}
	

	public void onYearChange() {
		quarter = "";
		groupName = "";
		displayGroups = false;
		displayStudents = false;
	}
	
	public void onSeasonChange(){
		if (quarter != null && !quarter.isEmpty() && year != null && !year.isEmpty()){
			List<Graduation> list = graduationDao.getByYear(new Integer(year));
			
			QuarterEnum curentQ = QuarterEnum.valueOf(quarter);
			for (Graduation g: list){
				if (g.getQuarter().equals(curentQ)){
					graduation = g;
					HibernateUtil.lazyInitialize(graduation, graduation.getGroups());
					break;
				}
			}
			
			displayGroups = true;
			loadGroups();
		}
		else{
			displayGroups = false;
		}
		
		groupName = "";
		displayStudents = false;
	}
	
	public void onGroupChange(){
		if (groupName == null || groupName.isEmpty()){
			displayStudents = false;
			return;
		}
		
		groupObj = getSelectedGroup();
		HibernateUtil.lazyInitialize(groupObj, groupObj.getStudents());

		displayStudents = true;
	}

	private Group getSelectedGroup(){
		Integer id = new Integer(groupName);
		for(Group g: graduation.getGroups()){
			 if (g.getId().equals(id)){
				 return g;
			 }
		}
		return null;
	}
	
	private boolean newGraduationYearValid() {
		
		boolean isValid;
		FacesMessage msg;
		
		if (newGraduationYear.matches("[12][019][0-9][0-9]")){
			Integer year = new Integer(newGraduationYear);
			
			if ((year > 1970) && (year < 2100)){
				if (graduationDao.getByYear(year).size() == 0){
					msg = new FacesMessage("Рік додано.");
					isValid = true;
				}
				else{
					msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Такий рік вже існує.", "Такий рік вже існує.");
					isValid = false;
				}
			}
			else{
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Допустимий діапазон - від 1970 до 2100.",
						"Допустимий діапазон - від 1970 до 2100.");
				isValid = false;
			}
		}
		else{
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Введіть вірний формат.",
					"Введіть вірний формат.");
			isValid = false;
		}
		
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return isValid;
	}
	
	public void addGraduation(){
		
		if (newGraduationYearValid()){
			Graduation graduation = new Graduation();
			Commission commission = new Commission();
			Calendar cld = Calendar.getInstance();
			cld.set(Calendar.YEAR, new Integer(newGraduationYear));
			cld.set(Calendar.WEEK_OF_YEAR, 6);
			Date date = cld.getTime();
		
			graduation.setYear(date);
//			graduation.setQuarter(QuarterEnum.autumn);
//			commission = new Commission();
//			graduation.setCommission(commission);
//			commission.setGraduation(graduation);
//			graduationDao.insert(graduation);
	
			graduation.setQuarter(QuarterEnum.summer);
			commission = new Commission();
			graduation.setCommission(commission);
			commission.setGraduation(graduation);
			graduationDao.insert(graduation);
	
			graduation.setQuarter(QuarterEnum.winter);
			commission = new Commission();
			graduation.setCommission(commission);
			commission.setGraduation(graduation);
			graduationDao.insert(graduation);
			
			years = getYearsMap(graduationDao.getYearsList());
			newGraduationYear = "";
		}
	}
	
	private void loadGroups(){
		Map<String, String> map = new HashMap<String, String>();
		
		for (Group g: graduation.getGroups()){
			map.put(g.getName(), g.getId().toString());
		}
		
		groups =  map;
	}
	
	private boolean newGroupValid() {
		
		boolean isValid = true;
		FacesMessage msg = new FacesMessage("Групу додано.");
		
		for (Group g: graduation.getGroups()){
			if (g.getName().equals(newGroupName)){
				isValid = false;
				msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Група з такою назвою вже існує.", "Група з такою назвою вже існує.");
				break;
			}
		}
		
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return isValid;
	}
	
	public void addGroup(){
		
		if (newGroupValid()){
			Group group = new Group();
			group.setName(newGroupName);
			groupDao.insert(group);
			
			graduation.getGroups().add(group);
			group.setGraduation(graduation);
			
			graduationDao.update(graduation);
			
			loadGroups();
			newGroupName = "";
		}
	}
	
	public void addStudent(){

		newDiploma.setDiplomaInfo(newDiplomaInfo);
		newDiploma.setStudent(newStudent);
		newDiploma.setReview(newReview);
		
		newDiplomaInfo.setDiploma(newDiploma);
		newStudent.setDiploma(newDiploma);
		newReview.setDiploma(newDiploma);

		diplomaDao.save(newDiploma);
		
		newStudent.setGroup(groupObj);
		groupObj.getStudents().add(newStudent);
		groupDao.update(groupObj);
		
		newStudent = new Student();
		newDiploma = new Diploma();
		newDiplomaInfo = new DiplomaInfo();
		newReview = new Review();
		
		FacesMessage msg = new FacesMessage("Студента додано.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}


	public String updateAllStudentOnEdit(){
		groupDao.update(groupObj);
		
		FacesMessage msg = new FacesMessage("Дані про всіх студентів збережено.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return null;
	}
	
	
	public String clearAllStudentChangesOnEdit() {
		
		for (Student student: groupObj.getStudents()){
			HibernateUtil.refresh(student);
			HibernateUtil.lazyInitialize(groupObj, groupObj.getStudents());
			break;
		}
		HibernateUtil.lazyInitialize(groupObj, groupObj.getStudents());
		
		FacesMessage msg = new FacesMessage("Зміни даних про всіх студентів відмінено.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return null;
    }

	
	public void deleteStudent(Student studentToDelete) {
		groupObj.getStudents().remove(studentToDelete);

		studentDao.delete(studentToDelete);
		
		FacesMessage msg = new FacesMessage("Студента видалено.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void deleteGroup(){
		graduation.getGroups().remove(groupObj);
		loadGroups();
		
		groupDao.delete(groupObj);
		
		groupObj = null;
		displayStudents = false;
		
		FacesMessage msg = new FacesMessage("Групу видалено.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void uploadFile() {
		if (diplomasFile != null){

			File file = new File(TEMP_STUDENT_INFO_FILE_NAME + "." + TEMP_STUDENT_INFO_FILE_EXTENSION);

			if (file.exists()) {
				file.delete();
			}

			try (InputStream input = diplomasFile.getInputStream()) {
				Files.copy(input, file.toPath());
				new StudentFileReader().readFile(file.getName(), graduation);
				
				FacesMessage msg = new FacesMessage("Дані з документа завантажено.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				loadGroups();
				
				String[] updateElementsId = {":form1:msgs", ":form2", ":form25", ":form3", ":form4", ":form5"};
				for (String elementId: updateElementsId){
					RequestContext.getCurrentInstance().update(elementId);
				}
			} catch (IOException e) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Помилка при завантаженні.", "Помилка при завантаженні.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				e.printStackTrace();
			}

			diplomasFile = null;
		}
		else{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Документ не обрано.", "Документ не обрано.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	/*
	public void parsStudentsDoc() throws IOException{
		groupObj = null;
		displayStudents = false;
		
		if(studentsDocFile != null) {
			
			// puts in to jBoss8.0/bin
			File file = new File(TEMP_STUDENT_INFO_FILE_NAME + "." + TEMP_STUDENT_INFO_FILE_EXTENSION);
			
//			File folder = new File("\\");
//			String filename = FilenameUtils.getBaseName(studentsDocFile.getFileName()); 
//			String extension = FilenameUtils.getExtension(studentsDocFile.getFileName());
//			File file = File.createTempFile(filename + "-", "." + extension, folder);
			
			if (!file.exists()){
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Помилка при завантаженні.", "Помилка при завантаженні.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				studentsDocFile = null;
				return;
			}
			
			new StudentFileReader().readFile(file.getName(), graduation);
			
			file.delete();
			studentsDocFile = null;
			
            FacesMessage msg = new FacesMessage("Дані з документа завантажено.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
		else{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Документ не обрано.", "Документ не обрано.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void loadStudentsDoc(FileUploadEvent event) throws IOException{

		studentsDocFile = null;
		if(event.getFile() != null) {
			
			// puts in to jBoss8.0/bin
			File file = new File(TEMP_STUDENT_INFO_FILE_NAME + "." + TEMP_STUDENT_INFO_FILE_EXTENSION);
			
//			File folder = new File("\\");
//			String filename = FilenameUtils.getBaseName(studentsDocFile.getFileName()); 
//			String extension = FilenameUtils.getExtension(studentsDocFile.getFileName());
//			File file = File.createTempFile(filename + "-", "." + extension, folder);
			
			if (file.exists()){
				file.delete();
			}
			
			try (InputStream input = event.getFile().getInputstream()) {
			    Files.copy(input, file.toPath());
			}
			
			studentsDocFile = event.getFile();
			
            FacesMessage msg = new FacesMessage("Документ завантажено.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
//            System.out.println("-----------------------------------------------------------------------------");
//            System.out.println("UTF-8 - " + new String(studentsDocFile.getFileName().getBytes("ISO-8859-1"), "UTF-8"));
//            System.out.println("UTF-8 - " + new String(studentsDocFile.getFileName().getBytes("Cp1251"), "UTF-8"));
//            System.out.println("ISO-8859-1 - " + new String(studentsDocFile.getFileName().getBytes("UTF-8"), "ISO-8859-1"));
//            System.out.println("ISO-8859-1 - " + new String(studentsDocFile.getFileName().getBytes("Cp1251"), "ISO-8859-1"));
//            System.out.println("Cp1251 - " + new String(studentsDocFile.getFileName().getBytes("UTF-8"), "Cp1251"));
//            System.out.println("Cp1251 - " + new String(studentsDocFile.getFileName().getBytes("ISO-8859-1"), "Cp1251"));
//            System.out.println("-----------------------------------------------------------------------------");
        }
		else{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Не вдалося завантажити документ.", "Не вдалося завантажити документ.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	//*/
	
	public void onStudentCheckboxEdit() {
		groupDao.update(groupObj);
    }
	
	public void downloadStatistic(){
		if (graduation != null){
			try {
				if (statisticFileStream != null){
					statisticFileStream.close();
					statisticFileStream = null;
				}
				String filePath = new DEKResultTableManager().generate(graduation.getId(), graduation.getQuarter());
				if (filePath == null || !new File(filePath).exists()){
					String message = "Не вдалося сформувати документ!";
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, message, message);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return;
				}
				
				statisticFileStream = new BufferedInputStream(new FileInputStream(filePath));
				statisticFile = new DefaultStreamedContent(statisticFileStream, "docx", getPaymentFormReadableFileName());
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
	
	private String getPaymentFormReadableFileName(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(graduation.getYear());
		
		String fileName = "Статистика за " + graduation.getQuarter() + " " + calendar.get(Calendar.YEAR) + ".xls";
		return Transliterator.transliterate(fileName);
	}
	
}
