package com.nulp.dss.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.nulp.dss.dao.GraduationDao;
import com.nulp.dss.dao.ReviewDao;
import com.nulp.dss.dao.ReviewerDao;
import com.nulp.dss.model.Graduation;
import com.nulp.dss.model.Group;
import com.nulp.dss.model.Review;
import com.nulp.dss.model.Reviewer;
import com.nulp.dss.model.Student;
import com.nulp.dss.model.enums.QuarterEnum;
import com.nulp.dss.util.HibernateUtil;

@ManagedBean
@ViewScoped
public class ReviewsEditBean {

	private static String overlappingMessage = "“акий час рецензуванн€ вже зан€тий ≥ншою реценз≥Їю";
	
	private GraduationDao graduationDao = new GraduationDao();
	private ReviewerDao reviewerDao = new ReviewerDao();
	private ReviewDao reviewDao = new ReviewDao();
	
	private String year;
	private String quarter;
	private String groupName;
	private Map<String, String> years;
	private Map<String, String> quarters;
	private Map<String, String> groups;
	private Set<Review> reviews;
	private List<Review> reviewsList;

	private Student freeStudentForNewReview;
	private String freeStudentForNewReviewId;
	private Map<String, Student> freeStudentsForNewReviews;
	private Map<String, String> freeStudentsForNewReviewsWithId;
	
	private Reviewer reviewer;
	private String reviewerId;
	private Map<String, Reviewer> reviewers;
	private Map<String, String> reviewersWithId;
	
	
	private Boolean displayGroups;
	private Boolean displayReviews;
	private Boolean displayAddReview;
	private Boolean displayReviewsList;
	
	private Group groupObj;
	private Graduation graduation;
	
	@PostConstruct
	public void init() {
		displayGroups = false;
		displayReviews = false;
		
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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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

	public Map<String, String> getGroups() {
		return groups;
	}

	public void setGroups(Map<String, String> groups) {
		this.groups = groups;
	}

	public Boolean getDisplayGroups() {
		return displayGroups;
	}

	public void setDisplayGroups(Boolean displayGroups) {
		this.displayGroups = displayGroups;
	}

	public Boolean getDisplayReviews() {
		return displayReviews;
	}

	public void setDisplayReviews(Boolean displayReviews) {
		this.displayReviews = displayReviews;
	}

	public Group getGroupObj() {
		return groupObj;
	}

	public void setGroupObj(Group groupObj) {
		this.groupObj = groupObj;
	}

	public Graduation getGraduation() {
		return graduation;
	}

	public void setGraduation(Graduation graduation) {
		this.graduation = graduation;
	}

	public Map<String, Reviewer> getReviewers() {
		return reviewers;
	}

	public void setReviewers(Map<String, Reviewer> reviewers) {
		this.reviewers = reviewers;
	}

	public Reviewer getReviewer() {
		return reviewer;
	}

	public void setReviewer(Reviewer reviewer) {
		this.reviewer = reviewer;
	}

	public Boolean getDisplayAddReview() {
		return displayAddReview;
	}

	public void setDisplayAddReview(Boolean displayAddReview) {
		this.displayAddReview = displayAddReview;
	}

	public Boolean getDisplayReviewsList() {
		return displayReviewsList;
	}

	public void setDisplayReviewsList(Boolean displayReviewsList) {
		this.displayReviewsList = displayReviewsList;
	}
	
	public Set<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	public Student getFreeStudentForNewReview() {
		return freeStudentForNewReview;
	}

	public void setFreeStudentForNewReview(Student freeStudentForNewReview) {
		this.freeStudentForNewReview = freeStudentForNewReview;
	}

	public Map<String, Student> getFreeStudentsForNewReviews() {
		return freeStudentsForNewReviews;
	}

	public void setFreeStudentsForNewReviews(
			Map<String, Student> freeStudentsForNewReviews) {
		this.freeStudentsForNewReviews = freeStudentsForNewReviews;
	}

	public String getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(String reviewerId) {
		this.reviewerId = reviewerId;
	}

	public Map<String, String> getReviewersWithId() {
		return reviewersWithId;
	}

	public void setReviewersWithId(Map<String, String> reviewersWithId) {
		this.reviewersWithId = reviewersWithId;
	}

	public String getFreeStudentForNewReviewId() {
		return freeStudentForNewReviewId;
	}

	public void setFreeStudentForNewReviewId(String freeStudentForNewReviewId) {
		this.freeStudentForNewReviewId = freeStudentForNewReviewId;
	}

	public Map<String, String> getFreeStudentsForNewReviewsWithId() {
		return freeStudentsForNewReviewsWithId;
	}

	public void setFreeStudentsForNewReviewsWithId(
			Map<String, String> freeStudentsForNewReviewsWithId) {
		this.freeStudentsForNewReviewsWithId = freeStudentsForNewReviewsWithId;
	}

	public List<Review> getReviewsList() {
		return reviewsList;
	}

	public void setReviewsList(List<Review> reviewsList) {
		this.reviewsList = reviewsList;
	}

	public void onYearChange() {
		quarter = "";
		groupName = "";
		displayGroups = false;
		displayReviews = false;
		displayAddReview = false;
		displayReviewsList = false;
	}
	
	
	public void onReviewerChange(){
		reviewer = reviewers.get(reviewerId);
		
		if (reviewer != null){
			displayReviewsList = true;
			displayAddReview = true;

			HibernateUtil.lazyInitialize(groupObj, groupObj.getStudents());
			reviews = getReviewsByReviewer();
			reviewsList = new ArrayList<Review>(reviews);
			initFreeStudentsForNewReviewsByGroup();
		}
		else{
			displayReviewsList = false;
			displayAddReview = false;
		}
	}
	
	private void initFreeStudentsForNewReviewsByGroup(){
		freeStudentsForNewReviews = new HashMap<String, Student>();
		freeStudentsForNewReviewsWithId = new HashMap<String, String>();
		
		Reviewer localReviewer;
		for (Student student: groupObj.getStudents()){
			localReviewer = student.getDiploma().getReview().getReviewer();
			if (localReviewer == null){
				freeStudentsForNewReviewsWithId.put(student.getlName() + " " + student.getfName() + " " + student.getmName(), student.getId().toString());
				freeStudentsForNewReviews.put(student.getId().toString(), student);
			}
		}
	}
	
	private Set<Review> getReviewsByReviewer(){
		Set<Review> set = new HashSet<Review>();
		
		Reviewer localReviewer;
		for (Student student: groupObj.getStudents()){
			localReviewer = student.getDiploma().getReview().getReviewer();
			if (localReviewer != null && localReviewer.getId().equals(reviewer.getId())){
				set.add(student.getDiploma().getReview());
			}
		}
		
		return set;
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
		displayReviews = false;
		displayAddReview = false;
		displayReviewsList = false;
	}
	
	private void loadGroups(){
		Map<String, String> map = new HashMap<String, String>();
		
		for (Group g: graduation.getGroups()){
			map.put(g.getName(), g.getId().toString());
		}
		
		groups =  map;
	}
	
	public void onGroupChange(){
		if (groupName == null || groupName.isEmpty()){
			displayReviewsList = false;
			displayAddReview = false;
			displayReviews = false;
			return;
		}
		
		groupObj = getSelectedGroup();
		HibernateUtil.lazyInitialize(groupObj, groupObj.getStudents());

		setReviewersMaps();

		if (reviewer != null){
			onReviewerChange();
			displayReviewsList = true;
			displayAddReview = true;
		}
		else{
			displayReviewsList = false;
			displayAddReview = false;
		}
		
		displayReviews = true;
	}
	
	private void setReviewersMaps(){
		String nullString = "-";
		reviewers = new HashMap<String, Reviewer>();
		reviewersWithId = new TreeMap<String, String>(new EnumManagerBean.EnumStringComparator(nullString, false));
		
		reviewers.put(nullString, null);
		reviewersWithId.put(nullString, nullString);
		for (Reviewer reviewer: reviewerDao.getAllWithGroup(groupObj)){
			reviewersWithId.put(reviewer.getlName() + " " + reviewer.getfName() + " " + reviewer.getmName(), reviewer.getId().toString());
			reviewers.put(reviewer.getId().toString(), reviewer);
		}
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
	
	public void deleteReview(Review review){
		review.setDate(null);
		review.setTimeEnd(null);
		review.setTimeStart(null);
		review.setReviewer(null);
		reviewDao.update(review);
		
		reviews.remove(review);
		reviewsList = new ArrayList<Review>(reviews);
		
		Student student = review.getDiploma().getStudent();
		freeStudentsForNewReviews.put(student.getId().toString(), student);
		freeStudentsForNewReviewsWithId.put(student.getlName() + " " + student.getfName() + " " + student.getmName(), student.getId().toString());
		
		FacesMessage msg = new FacesMessage("–еценз≥ю видалено.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public String addNewReview(){
		
		freeStudentForNewReview = freeStudentsForNewReviews.get(freeStudentForNewReviewId);
		
		if (freeStudentForNewReview != null){
			Review review = freeStudentForNewReview.getDiploma().getReview();
			
			review.setReviewer(reviewer);

			reviewDao.update(review);
			
			reviews.add(review);
			reviewsList = new ArrayList<Review>(reviews);
			
			Student student = review.getDiploma().getStudent();
			freeStudentsForNewReviews.remove(freeStudentForNewReviewId);
			freeStudentsForNewReviewsWithId.remove(student.getlName() + " " + student.getfName() + " " + student.getmName());
		}
		
		FacesMessage msg = new FacesMessage("–еценз≥ю додано.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		return null;
	}
	
	public void onReviewDateUpdate(Review review){
		FacesMessage msg;
		
		if (checkTimeOverlapping(review)){
			reviewDao.update(review);
			msg = new FacesMessage("«м≥нено дату рецензуванн€.");
		}
		else{
			HibernateUtil.refresh(review);
			msg = new FacesMessage(FacesMessage.SEVERITY_WARN, overlappingMessage, overlappingMessage);
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void onReviewStartTimeUpdate(Review review){
		FacesMessage msg;
		
		if (review.getTimeStart() == null){
			reviewDao.update(review);
			msg = new FacesMessage("„ас початку очищено.");
		}
		else if (review.getTimeEnd() == null || review.getTimeEnd().compareTo(review.getTimeStart()) > 0){
			if (checkTimeOverlapping(review)){
				reviewDao.update(review);
				msg = new FacesMessage("Ќовий час початку всановлено.");
			}
			else{
				HibernateUtil.refresh(review);
				msg = new FacesMessage(FacesMessage.SEVERITY_WARN, overlappingMessage, overlappingMessage);
			}
		}
		else{
			HibernateUtil.refresh(review);
			msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "„ас початку п≥зн≥ше н≥ж час к≥нц€!", "„ас початку п≥зн≥ше н≥ж час к≥нц€!");
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void onReviewEndTimeUpdate(Review review){
		FacesMessage msg;
		
		if (review.getTimeEnd() == null){
			reviewDao.update(review);
			msg = new FacesMessage("„ас к≥нц€ очищено.");
		}
		else if (review.getTimeStart() == null || review.getTimeEnd().compareTo(review.getTimeStart()) > 0){
			if (checkTimeOverlapping(review)){
				msg = new FacesMessage("Ќовий час к≥нц€ всановлено.");
				reviewDao.update(review);
			}
			else{
				HibernateUtil.refresh(review);
				msg = new FacesMessage(FacesMessage.SEVERITY_WARN, overlappingMessage, overlappingMessage);
			}
		}
		else{
			HibernateUtil.refresh(review);
			msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "„ас початку п≥зн≥ше н≥ж час к≥нц€!", "„ас початку п≥зн≥ше н≥ж час к≥нц€!");
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	private boolean checkTimeOverlapping(Review review){
		if (review.getDate() == null){
			return true;
		}
		else{
			return checkStartEndTimeOverlapping(review);
		}
	}
	
	private boolean checkStartEndTimeOverlapping(Review review){
		if (review.getTimeStart() != null && review.getTimeEnd() != null){
			return checkIfStartAndEndTimeOverlapping(review);
		}
		else if (review.getTimeStart() != null){
			return checkIfStartTimeOverlapping(review);
		}
		else if (review.getTimeEnd() != null){
			return checkIfEndTimeOverlapping(review);
		}
		else{
			return true;
		}
	}
	
	private boolean checkIfStartTimeOverlapping(Review review){
		for (Review loopReview: reviews){
			if (loopReview != review && loopReview.getDate().equals(review.getDate()) && loopReview.getTimeStart() != null && loopReview.getTimeEnd() != null){
				if (review.getTimeStart().compareTo(loopReview.getTimeStart()) >= 0 && review.getTimeStart().compareTo(loopReview.getTimeEnd()) < 0){
					return false;
				}
			}
		}
		
		return true;
	}
	
	private boolean checkIfEndTimeOverlapping(Review review){
		for (Review loopReview: reviews){
			if (loopReview != review && loopReview.getDate().equals(review.getDate()) && loopReview.getTimeStart() != null && loopReview.getTimeEnd() != null){
				if (review.getTimeEnd().compareTo(loopReview.getTimeStart()) > 0 && review.getTimeEnd().compareTo(loopReview.getTimeEnd()) <= 0){
					return false;
				}
			}
		}
		
		return true;
	}
	
	private boolean checkIfStartAndEndTimeOverlapping(Review review){
		for (Review loopReview: reviews){
			if (loopReview != review && loopReview.getDate().equals(review.getDate()) && loopReview.getTimeStart() != null && loopReview.getTimeEnd() != null){
				if (review.getTimeStart().compareTo(loopReview.getTimeStart()) >= 0 && review.getTimeStart().compareTo(loopReview.getTimeEnd()) < 0){
					return false;
				}
				if (review.getTimeEnd().compareTo(loopReview.getTimeStart()) > 0 && review.getTimeEnd().compareTo(loopReview.getTimeEnd()) <= 0){
					return false;
				}
				if (review.getTimeStart().compareTo(loopReview.getTimeStart()) >= 0 && review.getTimeEnd().compareTo(loopReview.getTimeEnd()) <= 0){
					return false;
				}
				if (loopReview.getTimeStart().compareTo(review.getTimeStart()) >= 0 && loopReview.getTimeEnd().compareTo(review.getTimeEnd()) <= 0){
					return false;
				}
			}
		}
		
		return true;
	}
	
}
