package com.nulp.dss.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String fName;
	private String lName;
	private String mName;
	private String regalia;

//	@ManyToMany(mappedBy="persons", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
//	private Set<Commission> commissions = new HashSet<Commission>();
	
//	@LazyCollection(LazyCollectionOption.FALSE)
//	@OneToMany(fetch = FetchType.LAZY, mappedBy="person", cascade=CascadeType.ALL)  
//	private Set<PresentInProtectionDay> presentInProtectionDay = new HashSet<PresentInProtectionDay>();
	
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

	public String getRegalia() {
		return regalia;
	}

	public void setRegalia(String regalia) {
		this.regalia = regalia;
	}

//	public Set<Commission> getCommissions() {
//		return commissions;
//	}
//
//	public void setCommissions(Set<Commission> commissions) {
//		this.commissions = commissions;
//	}

//	public Set<PresentInProtectionDay> getPresentInProtectionDay() {
//		return presentInProtectionDay;
//	}
//
//	public void setPresentInProtectionDay(
//			Set<PresentInProtectionDay> presentInProtectionDay) {
//		this.presentInProtectionDay = presentInProtectionDay;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Person other = (Person) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", fname=" + fName + ", lname=" + lName
				+ ", mname=" + mName + ", regalia=" + regalia + "]";
	}

}
