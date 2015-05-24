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
 * API for retrieving the Rating within specific {ratingId}
 *
 * HTTP Request: GET /questions/{category}/{questionId}/ratings/{ratingId}
 * HTTP Response:
 *       Status Code  Json Body              Description
 *    1. 200          Rating                 Successful Operation
 *    2. 400          ErrorBody              The Request is malformed
 *    3. 404          CategoryNotExistError  The category doesn't exist
 *    4. 404          QuestionNotExistError   The Question with {questionId} doesn't exist
 */

@RestController
public class ReadRatingController {

    @Autowired
    private RatingRepository repository;

   	@ResponseStatus(HttpStatus.OK)

	@RequestMapping(value = "/questions/{category}/{questionId}/ratings/{ratingId}", method=RequestMethod.GET)
	public Rating readRating(@PathVariable String category, @PathVariable String questionId, @PathVariable String ratingId) 
							throws ResourceNotExistException, NoContentException{

        // repository null, server error
        if (repository == null) {
            throw new ServerSideProblemException("repository null");
        }

        // category doesn't exist, throw ResourceNotExistException
        if (Category.isCategoryExist(category) == null) {
            ResourceNotExistException exception = new ResourceNotExistException("Category" + category + "Not Exist!");
            exception.setErrorBody(new CategoryNotExistErrorBody(category));
            throw exception;
        }

        // set category and questionId and ratingId
        Rating rating = new Rating();
        rating.setCategory(Category.valueOf(category));
        rating.setQuestionId(questionId);
        rating.setRatingId(ratingId);

        // retrieve Rating with {ratingId}
        // when Rating with {ratingId} doesn't exist, ResourceNotExistException will be thrown
        RatingSpecification readRatingSpec = RatingSpecificationFactory.getFindRatingByIdSpecification(rating);
        List<Rating> results = repository.query(readRatingSpec);

        // query result should not be null, server error
        if (results == null) {
        	throw new ServerSideProblemException("List<Rating> should not be null");
        }

        // query result should have only one element
        // if not, server error
        if (results.size() != 1) {
            throw new ServerSideProblemException("List<Rating> size should be 1");
        }

		return results.get(0);
	}
}