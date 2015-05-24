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
 * API for deleting the specified rating with {ratingId}.
 *
 * HTTP Request: DELETE /questions/{category}/{questionId}/ratings/{ratingId}
 * HTTP Response:
 *       Status Code  Json Body              Description
 *    1. 200          Question                Successful Operation
 *    2. 400          ErrorBody              The Request is malformed
 *    3. 404          CategoryNotExistError  The category doesn't exist
 *    4. 404          QuestionNotExistError   The Question with {questionId} doesn't exist
 */

@RestController
public class DeleteRatingController {

    @Autowired
    private RatingRepository repository;

	@ResponseStatus(HttpStatus.OK)

	@RequestMapping(value = "/questions/{category}/{questionId}/ratings/{ratingId}", method=RequestMethod.DELETE)
	public Rating deleteRating(@PathVariable String category, @PathVariable String questionId, @PathVariable String ratingId)
								throws ResourceNotExistException, NoContentException{

        // repository null, server error
        if (repository == null) {
            throw new ServerSideProblemException("repository null");
        }

        // category doesn't exist, throw ResourceNotExistException
        if (Category.isCategoryExist(category) == null) {
            ResourceNotExistException exception = new ResourceNotExistException("Category" + category + " Not Exist!");
            exception.setErrorBody(new CategoryNotExistErrorBody(category));
            throw exception;
        }

        // set category and questionId and ratingId
        Rating rating = new Rating();
        rating.setCategory(Category.valueOf(category));
        rating.setQuestionId(questionId);
        rating.setRatingId(ratingId);

        // delete Rating with repository
        // when Question with {questionId} doesn't exist, ResourceNotExistException will be thrown
        Rating result = repository.deleteRating(rating);

        // query result should not be null, server error
        if (result == null) {
        	throw new ServerSideProblemException("List<Rating> should not be null");
        }

        if (!result.isAllJsonInputFieldNonNull()) {
        	throw new ServerSideProblemException("Question field should not be null");
        }

		return result;
	}
}