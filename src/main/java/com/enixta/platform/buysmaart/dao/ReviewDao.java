/**
 * 
 */
package com.enixta.platform.buysmaart.dao;

import java.util.List;

import com.enixta.platform.buysmaart.model.SmartPhone;
import com.enixta.platform.buysmaart.model.YoutubeVideoReview;

/**
 * @author nbattula
 *
 */
public interface ReviewDao {
	
	public List<YoutubeVideoReview> getReviewDetails(String deviceName, long maxResults);
	
	public List<YoutubeVideoReview> retriveReviewDetailsFromYoutube(String reviewString, long maxResults);
	
	public void persistReviewDetails(List<YoutubeVideoReview> reviews);

	public List<SmartPhone> getDeviceDetails();
	
	public boolean deleteReview(String videoId);
}
