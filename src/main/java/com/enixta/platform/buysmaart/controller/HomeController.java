package com.enixta.platform.buysmaart.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.enixta.platform.buysmaart.model.SearchInput;
import com.enixta.platform.buysmaart.model.SmartPhone;
import com.enixta.platform.buysmaart.model.YoutubeVideoReview;
import com.enixta.platform.buysmaart.service.ReviewService;

@Controller
public class HomeController {

	@Autowired
	public ReviewService reviewService;

	@RequestMapping(value = "/home.action")
	public ModelAndView test(HttpServletResponse response) throws IOException {
		reviewService.initSearchableData();
		return new ModelAndView("home");
	}
	
	@RequestMapping(value = "/searchhome.action")
	public ModelAndView searchHome(HttpServletResponse response) throws IOException {
		return new ModelAndView("searchhome");
	}

	@RequestMapping(value = "/showphonereviews.action")
	public String phoneReviewsHome(HttpServletResponse response, ModelMap model)
			throws IOException {

		List<SmartPhone> smartPhoneList = reviewService.getDeviceDetails();

		model.put("smartPhoneList", smartPhoneList);
		return "phonereviewshome";
	}

	@RequestMapping(value = "/phonereviews.action")
	public ModelAndView getPhoneReviews(@RequestBody SmartPhone smartPhone,
			HttpServletResponse response, ModelMap model) throws IOException {

		List<YoutubeVideoReview> reviews = reviewService.getReviewDetails(
				smartPhone.getDisplayName(), 10);

		model.put("reviews", reviews);

		return new ModelAndView("phonereviewresults");
	}

	@RequestMapping(value = "/syncdata.action")
	public ModelAndView syncdata(HttpServletResponse response, ModelMap model)
			throws IOException {

		List<SmartPhone> smartPhoneList = reviewService.getDeviceDetails();
		List<YoutubeVideoReview> finalReviews = new ArrayList<>();
		List<YoutubeVideoReview> reviews = null;

		for (SmartPhone device : smartPhoneList) {
			reviews = reviewService.retriveReviewDetailsFromYoutube(
					device.getDisplayName(), 50);

			if (reviews != null) {
				finalReviews.addAll(reviews);
			}
		}

		reviewService.persistReviewDetails(finalReviews);

		return new ModelAndView("syncresult");
	}

	@RequestMapping(value = "/deletereview.action")
	public ModelAndView deletereview(@RequestBody YoutubeVideoReview review,
			HttpServletResponse response, ModelMap model) throws IOException {

		boolean result = reviewService.deleteReview(review.getVideoId());

		if (result) {
			List<YoutubeVideoReview> reviews = reviewService.getReviewDetails(
					review.getDeviceName(), 10);

			model.put("reviews", reviews);
		}

		return new ModelAndView("phonereviewresults");
	}

	@RequestMapping(value = "/lucenesearch.action")
	public ModelAndView lucenesearch(@RequestBody SearchInput searchInput,
			HttpServletResponse response, ModelMap model) throws IOException {

		searchInput.setQuery(searchInput.getQuery().toLowerCase());
		List<String> results = reviewService.getLuceneSearchResults(searchInput.getQuery());

		model.put("results", results);

		return new ModelAndView("luceneresults");
	}

	public ReviewService getReviewService() {
		return reviewService;
	}

	public void setReviewService(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

}
