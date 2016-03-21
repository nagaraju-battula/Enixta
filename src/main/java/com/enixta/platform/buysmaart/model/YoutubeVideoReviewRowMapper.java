/**
 * 
 */
package com.enixta.platform.buysmaart.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author nbattula
 *
 */
public class YoutubeVideoReviewRowMapper implements RowMapper{
	YoutubeVideoReview videoReview = null;
	List<YoutubeVideoReview> youtubeVideoReviewList = new ArrayList<YoutubeVideoReview>();
	
	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		do {
			YoutubeVideoReview videoReview = new YoutubeVideoReview();
			videoReview.setVideoId(rs.getString("F_VIDEO_ID"));
			videoReview.setReviewTitle(rs.getString("F_REVIEW_TITLE"));
			videoReview.setReviewDescription(rs.getString("F_REVIEW_DESCRIPTION"));
			videoReview.setChannelTitle(rs.getString("F_CHANNEL_TITLE"));
			//videoReview.setPublishedAt(rs.getDate("F_PUBLISHED_AT"));
			
			videoReview.setThumbnailUrl(rs.getString("F_THUMBNAIL_URL"));
			//videoReview.set(rs.getString("F_CREATION_DATE"));
			//videoReview.set(rs.getString("F_ISENABLED"));
			videoReview.setDeviceName(rs.getString("F_DEVICE_NAME"));
			
			youtubeVideoReviewList.add(videoReview);
			
		}while (rs.next());
		return youtubeVideoReviewList;
	}

}
