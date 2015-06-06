package com.nulp.dss.util.generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.nulp.dss.dao.GroupDao;
import com.nulp.dss.model.Group;
import com.nulp.dss.model.ProtectionDay;
import com.nulp.dss.model.Review;
import com.nulp.dss.util.GroupNameManager;
import com.nulp.dss.util.enums.DiplomaTypeEnum;

public class ReviewsTimeAutoGenerator {
	private static final Logger LOG = Logger.getLogger(ReviewsTimeAutoGenerator.class);
	private static final long ONE_MINUTE_IN_MILLIS = 60000;
	private static final long DEFAULT_START_TIME = ONE_MINUTE_IN_MILLIS * 60 * 16;
	
	private GroupDao groupDao = new GroupDao();
	
	
	public void generate(List<Review> reviewsList){
		TreeMap<Date, List<Review>> sortedByDateReviews = new TreeMap<Date, List<Review>>();
		List<Review> reviewsEmptyDateList = new ArrayList<Review>(reviewsList.size());
		
		divReviews(reviewsList, sortedByDateReviews, reviewsEmptyDateList);
		fillTimeForReviewsWithDate(sortedByDateReviews);
		fillTimeWithoutDate(sortedByDateReviews, reviewsEmptyDateList);
	}
	
	private void fillTimeWithoutDate(TreeMap<Date, List<Review>> sortedByDateReviews, List<Review> reviewsEmptyDateList) {
		long reviwingTime;
		for (Review review: reviewsEmptyDateList){
			ProtectionDay protectionDay = review.getDiploma().getProtectionDay();
			
			if (protectionDay == null || protectionDay.getDay() == null){
				LOG.warn("Diploma have no protection day, review - " + review + ", protection day - " + protectionDay);
			} else{
				Date reviewingDay = new Date(protectionDay.getDay().getTime() - ONE_MINUTE_IN_MILLIS * 60 * 24);
				
				if (sortedByDateReviews.containsKey(reviewingDay)){
					fillTimeWithoutDate(sortedByDateReviews, review, reviewingDay);
				} else{
					reviwingTime = getReviwingTime(review);
					review.setDate(reviewingDay);
					review.setTimeStart(new Date(DEFAULT_START_TIME));
					review.setTimeEnd(new Date(DEFAULT_START_TIME + reviwingTime));
					sortedByDateReviews.put(reviewingDay, new ArrayList<Review>(Arrays.asList(review)));
				}
			}
		}
	}

	private void fillTimeWithoutDate(TreeMap<Date, List<Review>> sortedByDateReviews, Review review, Date reviewingDay) {
		long reviwingTime = getReviwingTime(review);
		int maxReviewsInDay = getMaxReviewsInDay(review);
		Date maxEndTime; 

		while(sortedByDateReviews.get(reviewingDay).size() >= maxReviewsInDay){
			reviewingDay = new Date(reviewingDay.getTime() - ONE_MINUTE_IN_MILLIS * 60 * 24);
		}
		
		maxEndTime = getMaxEndTime(sortedByDateReviews.get(reviewingDay));
		review.setDate(reviewingDay);
		review.setTimeStart(maxEndTime);
		review.setTimeEnd(new Date(maxEndTime.getTime() + reviwingTime));
		sortedByDateReviews.get(reviewingDay).add(review);
	}

	private int getMaxReviewsInDay(Review review) {
		Group g = groupDao.getByStudentId(review.getDiploma().getStudent().getId());
		DiplomaTypeEnum dTypeEnum = GroupNameManager.getGroupDiplomasType(g.getName());
		if (dTypeEnum.equals(DiplomaTypeEnum.MASTER)){
			return 2;
		} else if (dTypeEnum.equals(DiplomaTypeEnum.SPECIALIST)){
			return 2;
		} else if (dTypeEnum.equals(DiplomaTypeEnum.BACHELOR)){
			return 3;
		} else if (dTypeEnum.equals(DiplomaTypeEnum.NCK)){
			return 2;
		} else{
			LOG.warn("Unknown group, name - " + g.getName());
			return 0;
		}
	}

	private void fillTimeForReviewsWithDate(TreeMap<Date, List<Review>> sortedByDateReviews) {
		long reviwingTime;
		Date maxEndTime;
		
		for (List<Review> reviews: sortedByDateReviews.values()){
			for (Review r: reviews){
				if (r.getTimeStart() != null && r.getTimeEnd() == null){
					reviwingTime = getReviwingTime(r);
					r.setTimeEnd(new Date(r.getTimeStart().getTime() + reviwingTime));
				} else if (r.getTimeStart() == null && r.getTimeEnd() != null){
					reviwingTime = getReviwingTime(r);
					r.setTimeStart(new Date(r.getTimeEnd().getTime() - reviwingTime));
				}
			}
			for (Review r: reviews){
				if (r.getTimeStart() == null && r.getTimeEnd() == null){
					maxEndTime = getMaxEndTime(reviews);
					if (maxEndTime == null){
						maxEndTime = new Date(DEFAULT_START_TIME);
					}
					reviwingTime = getReviwingTime(r);
					r.setTimeStart(maxEndTime);
					r.setTimeEnd(new Date(maxEndTime.getTime() + reviwingTime));
				}
			}
		}
	}

	private Date getMaxEndTime(List<Review> reviews) {
		Date maxEndTime = null;
		for (Review r: reviews){
			if (r.getTimeStart() != null && r.getTimeEnd() != null){
				if (maxEndTime == null){
					maxEndTime = r.getTimeEnd();
				} else{
					if (r.getTimeEnd().after(maxEndTime)){
						maxEndTime = r.getTimeEnd();
					}
				}
			}
		}
		return maxEndTime;
	}

	private long getReviwingTime(Review review) {
		Group g = groupDao.getByStudentId(review.getDiploma().getStudent().getId());
		DiplomaTypeEnum dTypeEnum = GroupNameManager.getGroupDiplomasType(g.getName());
		if (dTypeEnum.equals(DiplomaTypeEnum.MASTER)){
			return ONE_MINUTE_IN_MILLIS * 60 * 4;
		} else if (dTypeEnum.equals(DiplomaTypeEnum.SPECIALIST)){
			return ONE_MINUTE_IN_MILLIS * 60 * 3;
		} else if (dTypeEnum.equals(DiplomaTypeEnum.BACHELOR)){
			return ONE_MINUTE_IN_MILLIS * 60 * 2;
		} else if (dTypeEnum.equals(DiplomaTypeEnum.NCK)){
			return ONE_MINUTE_IN_MILLIS * 60 * 3;
		} else{
			LOG.warn("Unknown group, name - " + g.getName());
			return 0;
		}
	}

	private void divReviews(List<Review> reviewsList, TreeMap<Date, List<Review>> sortedByDateReviews, List<Review> reviewsEmptyDateList){
		Date date;
		for (Review r: reviewsList){
			date = r.getDate();
			if (date != null){
				if(sortedByDateReviews.containsKey(date)){
					sortedByDateReviews.get(date).add(r);
				} else{
					sortedByDateReviews.put(date, new ArrayList<Review>(Arrays.asList(r)));
				}
			} else{
				reviewsEmptyDateList.add(r);
			}
		}
	}
	
}
