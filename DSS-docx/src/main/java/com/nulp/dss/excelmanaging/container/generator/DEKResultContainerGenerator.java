package com.nulp.dss.excelmanaging.container.generator;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.nulp.dss.dao.GraduationDao;
import com.nulp.dss.excelmanaging.container.DEKResultContainer;
import com.nulp.dss.excelmanaging.container.DEKResultFullStatisticContainer;
import com.nulp.dss.excelmanaging.container.DEKWinterResultFullStatisticContainer;
import com.nulp.dss.model.DiplomaInfo;
import com.nulp.dss.model.Graduation;
import com.nulp.dss.model.Group;
import com.nulp.dss.model.Student;
import com.nulp.dss.model.enums.QuarterEnum;
import com.nulp.dss.model.enums.RatingEnum;
import com.nulp.dss.util.GroupNameManager;
import com.nulp.dss.util.HibernateUtil;
import com.nulp.dss.util.enums.DiplomaTypeEnum;

public class DEKResultContainerGenerator {
	private static final Logger LOG = Logger.getLogger(DEKResultContainerGenerator.class);

	public List<DEKResultContainer> generate(int graduationId) {
		Graduation graduation = new GraduationDao().getById(graduationId);

		if (graduation == null){
			return null;
		}
		
		HibernateUtil.lazyInitialize(graduation, graduation.getGroups());
		for (Group g: graduation.getGroups()){
			HibernateUtil.lazyInitialize(g, g.getStudents());
		}
		
		return generate(graduation);
	}
	
	public List<DEKResultContainer> generate(Graduation graduation) {
		List<DEKResultContainer> list = new ArrayList<DEKResultContainer>();
		
		if (graduation.getQuarter().equals(QuarterEnum.summer)){
			LOG.warn("SUMMER STATISTIC IS NOT SUPPORTED!!!");
		}
		else{
			DEKWinterResultFullStatisticContainer container = getWinterContainer(graduation);
			formateListFromWinterContainer(list, container.getSpecialists(), 10);
			formateListFromWinterContainer(list, container.getSpecialistsAbsentia(), 11);
			formateListFromWinterContainer(list, container.getMasters(), 12);
			formateListFromWinterContainer(list, container.getMastersAbsentia(), 13);
		}
		
		return list;
	}

	private void formateListFromWinterContainer(List<DEKResultContainer> list,
			DEKResultFullStatisticContainer container, int rowNumber) {
		list.add(new DEKResultContainer("B" + rowNumber, container.getAllowed()));
		list.add(new DEKResultContainer("C" + rowNumber, container.getIsProtected()));
		
		list.add(new DEKResultContainer("D" + rowNumber, container.getEvaluationExcellent()));
		list.add(new DEKResultContainer("F" + rowNumber, container.getEvaluationGod()));
		list.add(new DEKResultContainer("H" + rowNumber, container.getEvaluationSatisfactorily()));
		
		list.add(new DEKResultContainer("J" + rowNumber, container.getHasHonorsDiplom()));
		list.add(new DEKResultContainer("L" + rowNumber, container.getIsRecommendedToPostgraduate()));
		
		list.add(new DEKResultContainer("R" + rowNumber, container.getResearchType()));
		list.add(new DEKResultContainer("T" + rowNumber, container.getWithRealTechnologicalDevelopments()));
		list.add(new DEKResultContainer("V" + rowNumber, container.getByEnterpriseOrder()));
		list.add(new DEKResultContainer("X" + rowNumber, container.getRecommendedToImplementation()));
		list.add(new DEKResultContainer("Z" + rowNumber, container.getProtectedOnEnterprise()));
		
		list.add(new DEKResultContainer("AB" + rowNumber, container.getComplexWorkCathedral()));
		list.add(new DEKResultContainer("AE" + rowNumber, container.getRecommendedToPrint()));
	}

	private DEKWinterResultFullStatisticContainer getWinterContainer(
			Graduation graduation) {
		DEKWinterResultFullStatisticContainer container = new DEKWinterResultFullStatisticContainer();
		
		for (Group g: graduation.getGroups()){
			if (GroupNameManager.getGroupDiplomasType(g.getName()).equals(DiplomaTypeEnum.MASTER)){
				if (GroupNameManager.isAbsentia(g.getName())){
					addStatistic(container.getMastersAbsentia(), g);
				} else{
					addStatistic(container.getMasters(), g);
				}
			}else if (GroupNameManager.getGroupDiplomasType(g.getName()).equals(DiplomaTypeEnum.SPECIALIST)){
				if (GroupNameManager.isAbsentia(g.getName())){
					addStatistic(container.getSpecialistsAbsentia(), g);
				} else{
					addStatistic(container.getSpecialists(), g);
				}
			}
		}

		return container;
	}

	private void addStatistic(DEKResultFullStatisticContainer container,
			Group g) {
		DiplomaInfo diplomaInfo;
		for (Student s: g.getStudents()){
			diplomaInfo = s.getDiploma().getDiplomaInfo();
			
			if (diplomaInfo.getIsProtected() != null && diplomaInfo.getIsProtected()){
				container.setIsProtected(container.getIsProtected() + 1);
			} else{
				continue;
			}
			
			if (diplomaInfo.getAllowed() != null && diplomaInfo.getAllowed()){
				container.setAllowed(container.getAllowed() + 1);
			}
			
			
			RatingEnum rating = diplomaInfo.getDiploma().getRating();
			if (rating != null){
				if (rating.equals(RatingEnum.five)){
					container.setEvaluationExcellent(container.getEvaluationExcellent() + 1);
				}
				else if (rating.equals(RatingEnum.four)){
					container.setEvaluationGod(container.getEvaluationGod() + 1);
				}
				else if (rating.equals(RatingEnum.three)){
					container.setEvaluationSatisfactorily(container.getEvaluationSatisfactorily() + 1);
				}
			}
			
			if (s.getHasHonorsDiplom() != null && s.getHasHonorsDiplom()){
				container.setHasHonorsDiplom(container.getHasHonorsDiplom() + 1);
			}
			if (s.getIsRecommendedToPostgraduate() != null && s.getIsRecommendedToPostgraduate()){
				container.setIsRecommendedToPostgraduate(container.getIsRecommendedToPostgraduate() + 1);
			}
			
			if (diplomaInfo.getResearchType() != null && diplomaInfo.getResearchType()){
				container.setResearchType(container.getResearchType() + 1);
			}
			if (diplomaInfo.getWithRealTechnologicalDevelopments() != null && diplomaInfo.getWithRealTechnologicalDevelopments()){
				container.setWithRealTechnologicalDevelopments(container.getWithRealTechnologicalDevelopments() + 1);
			}
			if (diplomaInfo.getByEnterpriseOrder() != null && diplomaInfo.getByEnterpriseOrder()){
				container.setByEnterpriseOrder(container.getByEnterpriseOrder() + 1);
			}
			if (diplomaInfo.getRecommendedToImplementation() != null && diplomaInfo.getRecommendedToImplementation()){
				container.setRecommendedToImplementation(container.getRecommendedToImplementation() + 1);
			}
			if (diplomaInfo.getProtectedOnEnterprise() != null && diplomaInfo.getProtectedOnEnterprise()){
				container.setProtectedOnEnterprise(container.getProtectedOnEnterprise() + 1);
			}
			
			if (diplomaInfo.getComplexWorkCathedral() != null && diplomaInfo.getComplexWorkCathedral()){
				container.setComplexWorkCathedral(container.getComplexWorkCathedral() + 1);
			}
			if (diplomaInfo.getRecommendedToPrint() != null && diplomaInfo.getRecommendedToPrint()){
				container.setRecommendedToPrint(container.getRecommendedToPrint() + 1);
			}
		}
	}
}
