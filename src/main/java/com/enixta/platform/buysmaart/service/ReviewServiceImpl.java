/**
 * 
 */
package com.enixta.platform.buysmaart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.enixta.platform.buysmaart.dao.ReviewDao;
import com.enixta.platform.buysmaart.model.SmartPhone;
import com.enixta.platform.buysmaart.model.YoutubeVideoReview;

/**
 * @author nbattula
 *
 */
public class ReviewServiceImpl implements ReviewService{

	public ReviewDao reviewDao;
	
	@Override
	public List<YoutubeVideoReview> getReviewDetails(String deviceName,
			long maxResults) {
		return reviewDao.getReviewDetails(deviceName, maxResults);
	}

	@Override
	public List<YoutubeVideoReview> retriveReviewDetailsFromYoutube(
			String reviewString, long maxResults) {
		return reviewDao.retriveReviewDetailsFromYoutube(reviewString, maxResults);
	}

	@Override
	public void persistReviewDetails(List<YoutubeVideoReview> reviews) {
		reviewDao.persistReviewDetails(reviews);
	}

	public ReviewDao getReviewDao() {
		return reviewDao;
	}

	public void setReviewDao(ReviewDao reviewDao) {
		this.reviewDao = reviewDao;
	}

	@Override
	public List<SmartPhone> getDeviceDetails() {
		return reviewDao.getDeviceDetails();
	}

	@Override
	public boolean deleteReview(String videoId) {
		return reviewDao.deleteReview(videoId);
	}

}
