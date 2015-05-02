package com.nulp.dss.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.nulp.dss.model.enums.DiplomaTypeEnum;
import com.nulp.dss.model.enums.FormEnum;
import com.nulp.dss.model.enums.RatingEnum;

@Entity
public class Diploma {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String theme;
	
	@Column(name = "sequence_number_of_passing_by_day")
	private Integer sequenceNumberOfPassingByDay;
	
	@Enumerated(EnumType.STRING)
	private RatingEnum rating;
	
	@Column(name = "head_rating")
	@Enumerated(EnumType.STRING)
	private RatingEnum headRating;
	
	@Column(name = "reviewer_rating")
	@Enumerated(EnumType.STRING)
	private RatingEnum reviewerRating;
	
	@Column(name = "number_of_explanatory_pages")
	private Integer numberOfExplanatoryPages;
	
	@Column(name = "number_of_drawings_pages")
	private Integer numberOfDrawingsPages;
	
	@Column(name = "diploma_type")
	@Enumerated(EnumType.STRING)
	private DiplomaTypeEnum diplomaType;
	
	@Enumerated(EnumType.STRING)
	private FormEnum form;
	
//	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy="diploma", cascade=CascadeType.ALL)  
	private Set<Question> questions = new HashSet<Question>();

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "head_id")
	private Head head;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "protection_day_id")
	private ProtectionDay protectionDay;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "diploma")
	private DiplomaInfo diplomaInfo;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "diploma")
	private Review review;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "diploma")
	private Student student;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public RatingEnum getRating() {
		return rating;
	}

	public void setRating(RatingEnum rating) {
		this.rating = rating;
	}

	public Integer getSequenceNumberOfPassingByDay() {
		return sequenceNumberOfPassingByDay;
	}

	public void setSequenceNumberOfPassingByDay(Integer sequenceNumberOfPassingByDay) {
		this.sequenceNumberOfPassingByDay = sequenceNumberOfPassingByDay;
	}

	public RatingEnum getHeadRating() {
		return headRating;
	}

	public void setHeadRating(RatingEnum headRating) {
		this.headRating = headRating;
	}

	public RatingEnum getReviewerRating() {
		return reviewerRating;
	}

	public void setReviewerRating(RatingEnum reviewerRating) {
		this.reviewerRating = reviewerRating;
	}

	public Integer getNumberOfExplanatoryPages() {
		return numberOfExplanatoryPages;
	}

	public void setNumberOfExplanatoryPages(Integer numberOfExplanatoryPages) {
		this.numberOfExplanatoryPages = numberOfExplanatoryPages;
	}

	public Integer getNumberOfDrawingsPages() {
		return numberOfDrawingsPages;
	}

	public void setNumberOfDrawingsPages(Integer numberOfDrawingsPages) {
		this.numberOfDrawingsPages = numberOfDrawingsPages;
	}

	public DiplomaTypeEnum getDiplomaType() {
		return diplomaType;
	}

	public void setDiplomaType(DiplomaTypeEnum diplomaType) {
		this.diplomaType = diplomaType;
	}

	public FormEnum getForm() {
		return form;
	}

	public void setForm(FormEnum form) {
		this.form = form;
	}

	public DiplomaInfo getDiplomaInfo() {
		return diplomaInfo;
	}

	public void setDiplomaInfo(DiplomaInfo diplomaInfo) {
		this.diplomaInfo = diplomaInfo;
	}

	public Set<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}

	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	public ProtectionDay getProtectionDay() {
		return protectionDay;
	}

	public void setProtectionDay(ProtectionDay protectionDay) {
		this.protectionDay = protectionDay;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Diploma other = (Diploma) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Diploma [id=" + id + ", theme=" + theme + ", rating=" + rating
				+ ", sequenceNumberOfPassingByDay="
				+ sequenceNumberOfPassingByDay + ", headRating=" + headRating
				+ ", reviewerRating=" + reviewerRating
				+ ", numberOfExplanatoryPages=" + numberOfExplanatoryPages
				+ ", numberOfDrawingsPages=" + numberOfDrawingsPages
				+ ", diplomaType=" + diplomaType + ", form=" + form + "]";
	}

}
