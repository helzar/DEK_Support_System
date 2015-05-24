package com.nulp.dss.excelmanaging;

import java.util.Random;

import com.nulp.dss.dao.GraduationDao;
import com.nulp.dss.excelmanaging.container.DEKResultContainer;
import com.nulp.dss.excelmanaging.management.DEKResultTableManager;
import com.nulp.dss.model.DiplomaInfo;
import com.nulp.dss.model.Graduation;
import com.nulp.dss.model.Group;
import com.nulp.dss.model.Student;
import com.nulp.dss.model.enums.QuarterEnum;
import com.nulp.dss.model.enums.RatingEnum;
import com.nulp.dss.util.HibernateUtil;

public class MainClass {

	public static void main(String[] args) throws Exception {
		System.out.println("Start");
		
		fillGiplomaInfoWithTestData(30);
		fillGiplomaInfoWithTestData(33);
		
		// 2015 -33   2014 -30
//		new DEKResultTableManager().generate(30, QuarterEnum.winter);
		
		System.out.println("END");
	}

	public static void fillGiplomaInfoWithTestData(int graduationId) {
		Graduation graduation = new GraduationDao().getById(graduationId);

		if (graduation == null){
			return;
		}
		
		HibernateUtil.lazyInitialize(graduation, graduation.getGroups());
		for (Group g: graduation.getGroups()){
			HibernateUtil.lazyInitialize(g, g.getStudents());
			for (Student s: g.getStudents()){
				Random random = new Random();
				DiplomaInfo dInfo = s.getDiploma().getDiplomaInfo();
				
				s.setHasHonorsDiplom(random.nextBoolean());
				s.setIsRecommendedToPostgraduate(random.nextBoolean());
				
				dInfo.setAllowed(random.nextBoolean());
				dInfo.setIsProtected(random.nextBoolean());
				
				
				RatingEnum rating;
				int randValue = random.nextInt(10);
				if (randValue <= 4){  // [0-4]
					rating = RatingEnum.five;
				}
				else if (randValue <= 7){ // [5-7]
					rating = RatingEnum.four;
				}
				else{// [8-9]
					rating = RatingEnum.three;
				}
				dInfo.getDiploma().setRating(rating);

				dInfo.setResearchType(random.nextBoolean());
				dInfo.setWithRealTechnologicalDevelopments(random.nextBoolean());
				dInfo.setByEnterpriseOrder(random.nextBoolean());
				dInfo.setRecommendedToImplementation(random.nextBoolean());
				dInfo.setProtectedOnEnterprise(random.nextBoolean());
				
				dInfo.setComplexWorkCathedral(random.nextBoolean());
				dInfo.setRecommendedToPrint(random.nextBoolean());
			}
		}
		
		new GraduationDao().update(graduation);
	}

	

}
