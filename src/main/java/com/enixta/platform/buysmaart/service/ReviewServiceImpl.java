/**
 * 
 */
package com.enixta.platform.buysmaart.service;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;

import com.enixta.platform.buysmaart.dao.ReviewDao;
import com.enixta.platform.buysmaart.model.SmartPhone;
import com.enixta.platform.buysmaart.model.YoutubeVideoReview;
import com.enixta.platform.buysmaart.utils.LuceneSearch;

/**
 * @author nbattula
 *
 */
public class ReviewServiceImpl implements ReviewService {

	public ReviewDao reviewDao;

	@Override
	public List<YoutubeVideoReview> getReviewDetails(String deviceName,
			long maxResults) {
		return reviewDao.getReviewDetails(deviceName, maxResults);
	}

	@Override
	public List<YoutubeVideoReview> retriveReviewDetailsFromYoutube(
			String reviewString, long maxResults) {
		return reviewDao.retriveReviewDetailsFromYoutube(reviewString,
				maxResults);
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

	@Override
	public void initSearchableData() {
		List<YoutubeVideoReview> youtubeReviewList = reviewDao
				.getReviewDetails(null, Integer.MAX_VALUE);
		LuceneSearch.init(youtubeReviewList);
	}

	@Override
	public List<String> getLuceneSearchResults(String query) {
		try {
			List<YoutubeVideoReview> youtubeReviewList = reviewDao
					.getReviewDetails(null, Integer.MAX_VALUE);
			return LuceneSearch.search(query, youtubeReviewList);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
