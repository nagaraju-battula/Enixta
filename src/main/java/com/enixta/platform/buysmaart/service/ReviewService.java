/**
 * 
 */
package com.enixta.platform.buysmaart.service;

import java.util.List;

import com.enixta.platform.buysmaart.model.SmartPhone;
import com.enixta.platform.buysmaart.model.YoutubeVideoReview;

/**
 * @author nbattula
 *
 */
public interface ReviewService {
	
		public List<SmartPhone> getDeviceDetails();
	
		public List<YoutubeVideoReview> getReviewDetails(String deviceName, long maxResults);
		
		public List<YoutubeVideoReview> retriveReviewDetailsFromYoutube(String reviewString, long maxResults);
		
		public void persistReviewDetails(List<YoutubeVideoReview> reviews);
		
		public boolean deleteReview(String videoId);
		
		public void initSearchableData();
		
		public List<String> getLuceneSearchResults(String query);
}
