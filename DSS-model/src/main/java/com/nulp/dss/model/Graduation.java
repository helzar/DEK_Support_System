package com.nulp.dss.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.nulp.dss.model.enums.QuarterEnum;

@Entity
public class Graduation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Temporal(value=TemporalType.DATE)
	private Date year;
	
	@Enumerated(EnumType.STRING)
	private QuarterEnum quarter;
	
//	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy="graduation", cascade=CascadeType.ALL)  
	private Set<Group> groups = new HashSet<Group>();
	
//	@LazyCollection(LazyCollectionOption.FALSE)
//	@OneToMany(fetch = FetchType.LAZY, mappedBy="graduation", cascade=CascadeType.ALL)  
//	private Set<ProtectionDay> protectionDays = new HashSet<ProtectionDay>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)  
	@JoinColumn(name="graduation_id")
	private Set<ProtectionDay> protectionDays = new HashSet<ProtectionDay>();
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "graduation")
	private Commission commission;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getYear() {
		return year;
	}

	public void setYear(Date year) {
		this.year = year;
	}

	public QuarterEnum getQuarter() {
		return quarter;
	}

	public void setQuarter(QuarterEnum quarter) {
		this.quarter = quarter;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	public Set<ProtectionDay> getProtectionDays() {
		return protectionDays;
	}

	public void setProtectionDays(Set<ProtectionDay> protectionDays) {
		this.protectionDays = protectionDays;
	}
	
	public Commission getCommission() {
		return commission;
	}

	public void setCommission(Commission commission) {
		this.commission = commission;
	}

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
		Graduation other = (Graduation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Graduation [id=" + id + ", year=" + year + ", quarter="
				+ quarter + "]";
	}

}
