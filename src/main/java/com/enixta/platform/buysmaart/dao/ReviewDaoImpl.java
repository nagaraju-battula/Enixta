/**
 * 
 */
package com.enixta.platform.buysmaart.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.enixta.platform.buysmaart.model.SmartPhone;
import com.enixta.platform.buysmaart.model.SmartPhoneRowMapper;
import com.enixta.platform.buysmaart.model.YoutubeVideoReview;
import com.enixta.platform.buysmaart.model.YoutubeVideoReviewRowMapper;
import com.enixta.platform.buysmaart.youtubeapi.YoutubeReviewSearchUtils;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

/**
 * @author nbattula
 *
 */
public class ReviewDaoImpl implements ReviewDao {

	public JdbcTemplate jdbcTemplate;

	@Override
	public List<YoutubeVideoReview> getReviewDetails(String deviceName,
			long maxResults) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append("DTL.F_VIDEO_ID, ");
		sb.append("DTL.F_REVIEW_TITLE, ");
		sb.append("DTL.F_REVIEW_DESCRIPTION, ");
		sb.append("DTL.F_CHANNEL_TITLE, ");
		sb.append("DTL.F_PUBLISHED_AT, ");
		sb.append("DTL.F_THUMBNAIL_URL, ");
		sb.append("DTL.F_CREATION_DATE, ");
		sb.append("DTL.F_ISENABLED, ");
		sb.append("DTL.F_DEVICE_NAME ");
		sb.append("FROM ");
		sb.append("T_DEVICE_REVIEWS DTL ");
		sb.append("WHERE ");
		sb.append("DTL.F_ISENABLED = ? ");
		if(deviceName != null){
			sb.append("AND DTL.F_DEVICE_NAME = ? ");
		}
		
		String query = sb.toString();
		Object[] args = null;
		
		if(deviceName != null){
			args = new Object[] { "NO", deviceName };
		}else{
			args = new Object[] { "NO"};
		}
		
		try {

			@SuppressWarnings("unchecked")
			List<YoutubeVideoReview> youtubeVideoReviewList = (List<YoutubeVideoReview>) jdbcTemplate
					.queryForObject(query, args, new YoutubeVideoReviewRowMapper());
			return youtubeVideoReviewList;
		}catch(EmptyResultDataAccessException er){
			//Ignore
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<YoutubeVideoReview> retriveReviewDetailsFromYoutube(
			String reviewString, long maxResults) {
		List<YoutubeVideoReview> youtubeReviewList = new ArrayList<YoutubeVideoReview>();
		List<SearchResult> searchResultList = YoutubeReviewSearchUtils
				.getVideoMetadata(reviewString + " review", maxResults);
		Iterator<SearchResult> iteratorSearchResults = searchResultList
				.iterator();

		if (!iteratorSearchResults.hasNext()) {
			return null;
		}

		YoutubeVideoReview youtubeVideoReview = null;
		while (iteratorSearchResults.hasNext()) {

			SearchResult singleVideo = iteratorSearchResults.next();
			ResourceId rId = singleVideo.getId();
			if (rId.getKind().equals("youtube#video")) {
				Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails()
						.getDefault();

				youtubeVideoReview = new YoutubeVideoReview();

				youtubeVideoReview.setVideoId(rId.getVideoId());
				youtubeVideoReview.setChannelTitle(singleVideo.getSnippet()
						.getChannelTitle());
				youtubeVideoReview.setReviewTitle(singleVideo.getSnippet()
						.getTitle());
				youtubeVideoReview.setReviewDescription(singleVideo
						.getSnippet().getDescription());
				youtubeVideoReview.setPublishedAt(singleVideo.getSnippet()
						.getPublishedAt());
				youtubeVideoReview.setThumbnailUrl(thumbnail.getUrl());
				youtubeVideoReview.setDeviceName(reviewString);

				youtubeReviewList.add(youtubeVideoReview);
			}
		}

		return youtubeReviewList;
	}

	@Override
	public void persistReviewDetails(List<YoutubeVideoReview> reviews) {
		int status;
		for (YoutubeVideoReview youtubeVideoReview : reviews) {
			status = insertReviewRecordToDB(youtubeVideoReview);
		}

	}

	private int insertReviewRecordToDB(YoutubeVideoReview review) {
		String queryDtl = "INSERT INTO "
				+ "T_DEVICE_REVIEWS"
				+ " (F_VIDEO_ID, F_DEVICE_NAME, F_REVIEW_TITLE, F_REVIEW_DESCRIPTION, F_CHANNEL_TITLE, F_PUBLISHED_AT, F_THUMBNAIL_URL, F_CREATION_DATE, F_ISENABLED)"
				+ " VALUES (?,?,?,?,?,?,?,SYSDATE(),?)"
				+ " ON DUPLICATE KEY UPDATE" + " F_REVIEW_TITLE = ?,"
				+ " F_REVIEW_DESCRIPTION = ?," + " F_CHANNEL_TITLE = ?,"
				+ " F_PUBLISHED_AT = ?," + " F_THUMBNAIL_URL = ?,"
				+ " F_CREATION_DATE = SYSDATE()," + "F_ISENABLED = F_ISENABLED";

		Object[] argsDtl = new Object[] { review.getVideoId(),
				review.getDeviceName(), review.getReviewTitle(),
				review.getReviewDescription(), review.getChannelTitle(),
				review.getPublishedAt(), review.getThumbnailUrl(), "NO",
				review.getReviewTitle(), review.getReviewDescription(),
				review.getChannelTitle(), review.getPublishedAt(),
				review.getThumbnailUrl() };

		int outDtl = jdbcTemplate.update(queryDtl, argsDtl);

		return outDtl;
	}
	
	@Override
	public boolean deleteReview(String videoId){
		String query = "UPDATE " + "T_DEVICE_REVIEWS"
				+ " SET F_ISENABLED = 'YES' WHERE F_VIDEO_ID = ?";
		Object[] args = new Object[] {videoId};
		int out = jdbcTemplate.update(query, args);

		if (out != 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<SmartPhone> getDeviceDetails() {

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append("DTL.F_NAME, ");
		sb.append("DTL.F_DEVICE_ID, ");
		sb.append("DTL.F_DISPLAYNAME, ");
		sb.append("DTL.F_PRICE, ");
		sb.append("DTL.F_ISENABLED, ");
		sb.append("DTL.F_COMPANY ");
		sb.append("FROM ");
		sb.append("T_DEVICE_DETAILS DTL ");
		sb.append("WHERE ");
		sb.append("DTL.F_ISENABLED = ? ");

		String query = sb.toString();

		Object[] args = new Object[] { "YES" };

		try {

			@SuppressWarnings("unchecked")
			List<SmartPhone> smartPhoneList = (List<SmartPhone>) jdbcTemplate
					.queryForObject(query, args, new SmartPhoneRowMapper());
			return smartPhoneList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
