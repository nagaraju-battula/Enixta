/**
 * 
 */
package com.enixta.platform.buysmaart.model;

import com.google.api.client.util.DateTime;

/**
 * @author nbattula
 *
 */
public class YoutubeVideoReview {
	
	private String videoId;
	private String reviewTitle;
	private String reviewDescription;
	private String channelTitle;
	private DateTime publishedAt;
	private String thumbnailUrl;
	private String deviceName;
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public String getReviewTitle() {
		return reviewTitle;
	}
	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}
	public String getReviewDescription() {
		return reviewDescription;
	}
	public void setReviewDescription(String reviewDescription) {
		this.reviewDescription = reviewDescription;
	}
	public String getChannelTitle() {
		return channelTitle;
	}
	public void setChannelTitle(String channelTitle) {
		this.channelTitle = channelTitle;
	}
	public DateTime getPublishedAt() {
		return publishedAt;
	}
	public void setPublishedAt(DateTime publishedAt) {
		this.publishedAt = publishedAt;
	}
	
}
