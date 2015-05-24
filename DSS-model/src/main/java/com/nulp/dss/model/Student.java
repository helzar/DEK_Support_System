package com.nulp.dss.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
public class Student implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "diploma_id")
	@GenericGenerator(name = "gen", strategy = "foreign", parameters = @Parameter(name = "property", value = "diploma"))
	@GeneratedValue(generator = "gen")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "stud_group_id")
	private Group group;
	
	private String fName;
	private String lName;
	private String mName;
	
	@Column(name = "telephone_number")
	private String telephoneNumber;
	private String email;
	
	@Column(name = "has_honors_diplom")
	private Boolean hasHonorsDiplom;
	
	@Column(name = "is_recommended_to_postgraduate")
	private Boolean isRecommendedToPostgraduate;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn
	private Diploma diploma;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
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

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getHasHonorsDiplom() {
		return hasHonorsDiplom;
	}

	public void setHasHonorsDiplom(Boolean hasHonorsDiplom) {
		this.hasHonorsDiplom = hasHonorsDiplom;
	}

	public Boolean getIsRecommendedToPostgraduate() {
		return isRecommendedToPostgraduate;
	}

	public void setIsRecommendedToPostgraduate(
			Boolean isRecommendedToPostgraduate) {
		this.isRecommendedToPostgraduate = isRecommendedToPostgraduate;
	}

	public Diploma getDiploma() {
		return diploma;
	}

	public void setDiploma(Diploma diploma) {
		this.diploma = diploma;
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
		Student other = (Student) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", fName=" + fName + ", lName=" + lName
				+ ", mName=" + mName + ", telephoneNumber=" + telephoneNumber
				+ ", email=" + email + ", hasHonorsDiplom=" + hasHonorsDiplom
				+ ", isRecommendedToPostgraduate="
				+ isRecommendedToPostgraduate + "]";
	}

}
