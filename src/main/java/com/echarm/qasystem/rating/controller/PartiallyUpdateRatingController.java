package com.echarm.qasystem.rating.controller;

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
import com.echarm.qasystem.rating.util.Category;

/*
 * API for partially update some fields of the target rating
 *
 * HTTP Request: PATCH /questions/{category}/{questionId}/ratings/{ratingId}
 * HTTP Response:
 *       Status Code  Json Body              Description
 *    1. 200          Rating                 Successful Operation
 *    2. 400          ErrorBody              Missing JsonBody
 *    3. 404          CategoryNotExistError  The category doesn't exist
 */

@RestController
public class PartiallyUpdateRatingController {

    @Autowired
    private RatingRepository repository;

   	@ResponseStatus(HttpStatus.OK)


    // RequestBody(required=false) because:
    //     1. When there is no Json body in the HTTP request, "question" variable will be null
    //     2. When there is an empty Json body, "question" variable will not be null but have all fields empty
    //        Use Question.isQuestionEmpty(question) to verify
	@RequestMapping(value = "/questions/{category}/{questionId}/ratings/{ratingId}", method=RequestMethod.PATCH)
	public Rating partiallyUpdateRating(@PathVariable String category, @PathVariable String questionId, @PathVariable String ratingId, @RequestBody(required=false) Rating rating)
										  throws InvalidParameterException, ResourceNotExistException, NoContentException{

	    // no json body in the HTTP request
	    if (rating == null) {
	        MissingParameterErrorBody body = new MissingParameterErrorBody(MissingParameterErrorBody.generateDescription("Json Object: Rating", "Body"));
	        InvalidParameterException exception = new InvalidParameterException("No Json Body in the Request!");
	        exception.setErrorBody(body);
	        throw exception;
	    }

	    // empty json body in the HTTP request
	    if (rating.isAllJsonInputFieldNonNull()) {
	        EmptyParameterErrorBody body = new EmptyParameterErrorBody(EmptyParameterErrorBody.generateDescription("Json Object: Rating", "Body"));
            InvalidParameterException exception = new InvalidParameterException("Empty Json Body in the Request!");
            exception.setErrorBody(body);
            throw exception;
	    }

	    // repository null, server error
        if (repository == null) {
            throw new ServerSideProblemException("repository null");
        }

        // category doesn't exist, throw ResourceNotExistException
        if (Category.isCategoryExist(category) == null) {
            ResourceNotExistException exception = new ResourceNotExistException("Category Not Exist!");
            exception.setErrorBody(new CategoryNotExistErrorBody(category));
            throw exception;
        }

        // set category and questionId and ratingId
        rating.setCategory(Category.valueOf(category));
        rating.setQuestionId(questionId);
        rating.setRatingId(ratingId);

        // update Question with {questionId}
        // when Question with {questionId} doesn't exist, ResourceNotExistException is thrown
        // When nothing is updated (the question in request body is
        // the same as the one in the database), NoContentException is thrown
        Rating result = repository.updateRating(rating);


        if (result == null) {
        	throw new ServerSideProblemException("Question object should not be null");
        }

        if (!result.isAllJsonInputFieldNonNull()) {
        	throw new ServerSideProblemException("Question field should not be null");
        }

		return result;
	}
}