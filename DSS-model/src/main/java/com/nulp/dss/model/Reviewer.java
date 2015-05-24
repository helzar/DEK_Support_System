package com.nulp.dss.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Reviewer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String fName;
	private String lName;
	private String mName;
	
	@Temporal(value=TemporalType.DATE)
	private Date birthday;
	
	@Column(name = "number_of_childrens")
	private Integer numberOfChildrens;
	
	@Column(name = "academic_title")
	private String academicTitle;
	
	private String degree;
	
	@Column(name = "place_of_permanent_job")
	private String placeOfPermanentJob;
	
	@Column(name = "home_address")
	private String homeAddress;
	
	@Column(name = "passport_series")
	private String passportSeries;
	
	@Column(name = "passport_number")
	private String passportNumber;
	
	@Column(name = "passport_issued")
	private String passportIssued;
	
//	@LazyCollection(LazyCollectionOption.FALSE)
//	@OneToMany(fetch = FetchType.LAZY, mappedBy="reviewer", cascade=CascadeType.ALL)  
//	private Set<Review> reviews = new HashSet<Review>();
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getNumberOfChildrens() {
		return numberOfChildrens;
	}

	public void setNumberOfChildrens(Integer numberOfChildrens) {
		this.numberOfChildrens = numberOfChildrens;
	}

	public String getAcademicTitle() {
		return academicTitle;
	}

	public void setAcademicTitle(String academicTitle) {
		this.academicTitle = academicTitle;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getPlaceOfPermanentJob() {
		return placeOfPermanentJob;
	}

	public void setPlaceOfPermanentJob(String placeOfPermanentJob) {
		this.placeOfPermanentJob = placeOfPermanentJob;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getPassportSeries() {
		return passportSeries;
	}

	public void setPassportSeries(String passportSeries) {
		this.passportSeries = passportSeries;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getPassportIssued() {
		return passportIssued;
	}

	public void setPassportIssued(String passportIssued) {
		this.passportIssued = passportIssued;
	}

//	public Set<Review> getReviews() {
//		return reviews;
//	}
//
//	public void setReviews(Set<Review> reviews) {
//		this.reviews = reviews;
//	}

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
		Reviewer other = (Reviewer) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Reviewer [id=" + id + ", fName=" + fName + ", lName=" + lName
				+ ", mName=" + mName + ", birthday=" + birthday
				+ ", numberOfChildrens=" + numberOfChildrens
				+ ", academicTitle=" + academicTitle + ", degree=" + degree
				+ ", placeOfPermanentJob=" + placeOfPermanentJob
				+ ", homeAddress=" + homeAddress + ", passportSeries="
				+ passportSeries + ", passportNumber=" + passportNumber
				+ ", passportIssued=" + passportIssued + "]";
	}

}
