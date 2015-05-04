package com.nulp.dss.web.control;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.nulp.dss.dao.DiplomaDao;
import com.nulp.dss.dao.GraduationDao;
import com.nulp.dss.dao.PresentInProtectionDayDao;
import com.nulp.dss.model.Commission;
import com.nulp.dss.model.Diploma;
import com.nulp.dss.model.Graduation;
import com.nulp.dss.model.PresentInProtectionDay;
import com.nulp.dss.model.ProtectionDay;
import com.nulp.dss.model.enums.QuarterEnum;

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
	
	private Diploma freeDiploma;
	private String freeDiplomaId;
	private Map<String, Diploma> freeDiplomas;
	private Map<String, String> freeDiplomaIds;
	
	private Integer numberOfCurrentlySelectedDiplomas;
	private Integer numberOfMaxPossibleDiploms;
	
	
	@PostConstruct
	public void init() {
		displayDaysList = false;
		displayAllGraduationDay = false;

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

	public Diploma getFreeDiploma() {
		return freeDiploma;
	}

	public void setFreeDiploma(Diploma freeDiploma) {
		this.freeDiploma = freeDiploma;
	}

	public String getFreeDiplomaId() {
		return freeDiplomaId;
	}

	public void setFreeDiplomaId(String freeDiplomaId) {
		this.freeDiplomaId = freeDiplomaId;
	}

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
					break;
				}
			}
			
			displayDaysList = true;
		}
		else{
			displayDaysList = false;
		}
		
		displayAllGraduationDay = false;
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
	
	public void onPresentPersonEdit(PresentInProtectionDay presentInProtectionDay){
		presentInProtectionDayDao.update(presentInProtectionDay);
		
		FacesMessage msg = new FacesMessage("Статус члена комісіъ змінено.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	private void initFreeDiplomas(){
		String nullString = "-";
		freeDiplomas = new HashMap<String, Diploma>();
		freeDiplomaIds = new TreeMap<String, String>(new EnumManagerBean.EnumStringComparator(nullString, false));
		
		freeDiplomas.put(nullString, null);
		freeDiplomaIds.put(nullString, nullString);
		
		for (Diploma diploma: diplomaDao.getAllFreeByGraduationId(graduation.getId(), protectionDay)){
			freeDiplomas.put(diploma.getId().toString(), diploma);
			freeDiplomaIds.put(diploma.getStudent().getlName() + " " + diploma.getStudent().getfName() + " " + diploma.getStudent().getmName(), diploma.getId().toString());
		}
	}
	
	public void deleteDiploma(Diploma diploma){
		if (diploma != null){
			graduationDao.deleteDiplomaFromProtectionDay(diploma, protectionDay);
			
			freeDiplomas.put(diploma.getId().toString(), diploma);
			freeDiplomaIds.put(diploma.getStudent().getlName() + " " + diploma.getStudent().getfName() + " " + diploma.getStudent().getmName(), diploma.getId().toString());
			numberOfCurrentlySelectedDiplomas--;
		}
	}
	
	public String addDiploma(){
		
		freeDiploma = freeDiplomas.get(freeDiplomaId);
		if ((numberOfCurrentlySelectedDiplomas < numberOfMaxPossibleDiploms) && freeDiploma != null){
			graduationDao.addDiplomaToProtectionDay(freeDiploma, protectionDay);
			
			freeDiplomas.remove(freeDiploma.getId().toString());
			freeDiplomaIds.remove(freeDiploma.getStudent().getlName() + " " + freeDiploma.getStudent().getfName() + " " + freeDiploma.getStudent().getmName());
			numberOfCurrentlySelectedDiplomas++;
		}
		return null;
	}
	
}
