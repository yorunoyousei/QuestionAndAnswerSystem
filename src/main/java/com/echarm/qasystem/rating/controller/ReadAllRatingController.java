package com.echarm.qasystem.rating.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.echarm.qasystem.rating.error.CategoryNotExistErrorBody;
import com.echarm.qasystem.rating.error.EmptyParameterErrorBody;
import com.echarm.qasystem.rating.error.InvalidParameterException;
import com.echarm.qasystem.rating.error.MissingParameterErrorBody;
import com.echarm.qasystem.rating.error.NoContentException;
import com.echarm.qasystem.rating.error.ResourceNotExistException;
import com.echarm.qasystem.rating.error.ServerSideProblemException;
import com.echarm.qasystem.rating.model.Rating;
import com.echarm.qasystem.rating.repository.RatingRepository;
import com.echarm.qasystem.rating.repository.RatingSpecification;
import com.echarm.qasystem.rating.repository.RatingSpecificationFactory;
import com.echarm.qasystem.rating.util.Category;

/*
 * API for retrieving all ratings in a specific question
 *
 * HTTP Request: GET /questions/{category}/{questionId}/ratings
 * HTTP Response:
 *       Status Code  Json Body          Description
 *    1. 200          Array of Ratings 	 Successful Operation
 *    2. 204          null               No Questions Found
 *    3. 400          ErrorBody          The Request is malformed
 */

@RestController
public class ReadAllRatingController {

	@Autowired
	private RatingRepository repository;

   	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/questions/{category}/{questionId}/ratings", method = RequestMethod.GET)
	public List<Rating> readAllRating(@PathVariable String category, @PathVariable String questionId)
									throws NoContentException, ResourceNotExistException{

		// repository null, server error
		if (repository == null) {
		    throw new ServerSideProblemException("repository null");
		}
		
		// set Category and QuestionId into rating...modified by yoru
		Rating rating = new Rating();
		rating.setCategory(Category.valueOf(category));
		rating.setQuestionId(questionId);

		// retrieve all the ratings
		// when no questions found, NoContentException is thrown
		RatingSpecification readAllRatingSpec = RatingSpecificationFactory.getFindRatingAllSpecification(rating);
		List<Rating> results = repository.query(readAllRatingSpec);

		// query result should not be null, server error
		if (results == null) {
			throw new ServerSideProblemException("List<Rating> should not be null");
		}

		// query result should have at least one element
        // if not, server error
        if (results.size() <= 0) {
            throw new ServerSideProblemException("List<Rating> size should be at least 1");
        }

		return results;
	}
}
