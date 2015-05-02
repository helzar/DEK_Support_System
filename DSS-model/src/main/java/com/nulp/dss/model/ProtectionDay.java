package com.nulp.dss.model;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "protection_day")
public class ProtectionDay {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Temporal(value=TemporalType.DATE)
	private Date day;
	
	@Temporal(value=TemporalType.TIME)
	@Column(name = "start_time")
	private Date startTime;
	
	@Temporal(value=TemporalType.TIME)
	@Column(name = "end_time")
	private Date endTime;

	private String auditorium;
	
	@Column(name = "reserve_day")
	private Boolean reserveDay;
	
//	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	@JoinColumn(name = "graduation_id")
//	private Graduation graduation;
	
//	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy="protectionDay", cascade=CascadeType.ALL)  
	private Set<PresentInProtectionDay> presentInProtectionDaySet = new HashSet<PresentInProtectionDay>();

//	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy="protectionDay", cascade=CascadeType.ALL)  
	private Set<Diploma> diplomas = new HashSet<Diploma>();
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getAuditorium() {
		return auditorium;
	}

	public void setAuditorium(String auditorium) {
		this.auditorium = auditorium;
	}

	public Boolean getReserveDay() {
		return reserveDay;
	}

	public void setReserveDay(Boolean reserveDay) {
		this.reserveDay = reserveDay;
	}

//	public Graduation getGraduation() {
//		return graduation;
//	}
//
//	public void setGraduation(Graduation graduation) {
//		this.graduation = graduation;
//	}

	public Set<PresentInProtectionDay> getPresentInProtectionDaySet() {
		return presentInProtectionDaySet;
	}

	public void setPresentInProtectionDaySet(
			Set<PresentInProtectionDay> presentInProtectionDaySet) {
		this.presentInProtectionDaySet = presentInProtectionDaySet;
	}

	public Set<Diploma> getDiplomas() {
		return diplomas;
	}

	public void setDiplomas(Set<Diploma> diplomas) {
		this.diplomas = diplomas;
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
		ProtectionDay other = (ProtectionDay) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProtectionDay [id=" + id + ", day=" + day + ", startTime="
				+ startTime + ", endTime=" + endTime + ", auditorium="
				+ auditorium + ", reserveDay=" + reserveDay + "]";
	}

}
