package com.nulp.dss.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "diploma_info")
public class DiplomaInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "diploma_id")
	@GenericGenerator(name = "gen", strategy = "foreign", parameters = @Parameter(name = "property", value = "diploma"))
	@GeneratedValue(generator = "gen")
	private Integer id;
	
	@Column(name = "allowed")
	private Boolean allowed;
	
	@Column(name = "protected")
	private Boolean isProtected;
	
	@Column(name = "software_used")
	private Boolean softwareUsed;
	
	@Column(name = "research_type")
	private Boolean researchType;
	
	@Column(name = "with_real_technological_developments")
	private Boolean withRealTechnologicalDevelopments;
	
	@Column(name = "recommended_to_implementation")
	private Boolean recommendedToImplementation;
	
	@Column(name = "by_enterprise_order")
	private Boolean byEnterpriseOrder;
	
	@Column(name = "protected_on_enterprise")
	private Boolean protectedOnEnterprise;
	
	@Column(name = "complex_work_interuniversity")
	private Boolean complexWorkInteruniversity;
	
	@Column(name = "complex_work_intercathedral")
	private Boolean complexWorkIntercathedral;
	
	@Column(name = "complex_work_cathedral")
	private Boolean complexWorkCathedral;
	
	@Column(name = "recommended_to_print")
	private Boolean recommendedToPrint;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn
	private Diploma diploma;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getAllowed() {
		return allowed;
	}

	public void setAllowed(Boolean allowed) {
		this.allowed = allowed;
	}

	public Boolean getIsProtected() {
		return isProtected;
	}

	public void setIsProtected(Boolean isprotected) {
		this.isProtected = isprotected;
	}

	public Boolean getSoftwareUsed() {
		return softwareUsed;
	}

	public void setSoftwareUsed(Boolean softwareUsed) {
		this.softwareUsed = softwareUsed;
	}

	public Boolean getResearchType() {
		return researchType;
	}

	public void setResearchType(Boolean researchType) {
		this.researchType = researchType;
	}

	public Boolean getWithRealTechnologicalDevelopments() {
		return withRealTechnologicalDevelopments;
	}

	public void setWithRealTechnologicalDevelopments(
			Boolean withRealTechnologicalDevelopments) {
		this.withRealTechnologicalDevelopments = withRealTechnologicalDevelopments;
	}

	public Boolean getRecommendedToImplementation() {
		return recommendedToImplementation;
	}

	public void setRecommendedToImplementation(Boolean recommendedToImplementation) {
		this.recommendedToImplementation = recommendedToImplementation;
	}

	public Boolean getByEnterpriseOrder() {
		return byEnterpriseOrder;
	}

	public void setByEnterpriseOrder(Boolean byEnterpriseOrder) {
		this.byEnterpriseOrder = byEnterpriseOrder;
	}

	public Boolean getProtectedOnEnterprise() {
		return protectedOnEnterprise;
	}

	public void setProtectedOnEnterprise(Boolean protectedOnEnterprise) {
		this.protectedOnEnterprise = protectedOnEnterprise;
	}

	public Boolean getComplexWorkInteruniversity() {
		return complexWorkInteruniversity;
	}

	public void setComplexWorkInteruniversity(Boolean complexWorkInteruniversity) {
		this.complexWorkInteruniversity = complexWorkInteruniversity;
	}

	public Boolean getComplexWorkIntercathedral() {
		return complexWorkIntercathedral;
	}

	public void setComplexWorkIntercathedral(Boolean complexWorkIntercathedral) {
		this.complexWorkIntercathedral = complexWorkIntercathedral;
	}

	public Boolean getComplexWorkCathedral() {
		return complexWorkCathedral;
	}

	public void setComplexWorkCathedral(Boolean complexWorkCathedral) {
		this.complexWorkCathedral = complexWorkCathedral;
	}

	public Boolean getRecommendedToPrint() {
		return recommendedToPrint;
	}

	public void setRecommendedToPrint(Boolean recommendedToPrint) {
		this.recommendedToPrint = recommendedToPrint;
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
		DiplomaInfo other = (DiplomaInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DiplomaInfo [id=" + id + ", allowed=" + allowed
				+ ", isprotected=" + isProtected + ", softwareUsed="
				+ softwareUsed + ", researchType=" + researchType
				+ ", withRealTechnologicalDevelopments="
				+ withRealTechnologicalDevelopments
				+ ", recommendedToImplementation="
				+ recommendedToImplementation + ", byEnterpriseOrder="
				+ byEnterpriseOrder + ", protectedOnEnterprise="
				+ protectedOnEnterprise + ", complexWorkInteruniversity="
				+ complexWorkInteruniversity + ", complexWorkIntercathedral="
				+ complexWorkIntercathedral + ", complexWorkCathedral="
				+ complexWorkCathedral + ", recommendedToPrint="
				+ recommendedToPrint + "]";
	}
	

}
