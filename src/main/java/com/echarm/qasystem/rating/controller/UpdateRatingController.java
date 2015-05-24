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
 * API for updating the Rating with its ID
 *
 * HTTP Request: PUT /questions/{category}/{questionId}/ratings/{ratingId}
 * HTTP Response:
 *       Status Code  Json Body              Description
 *    1. 200          Rating                 Successful Operation
 *    2. 204          null                   Nothing is updated
 *    3. 400          ErrorBody              Missing JsonBody
 *    4. 404          CategoryNotExistError  The category doesn't exist
 *    5. 404          QuestionNotExistError   The Question with {questionId} doesn't exist
 *    6. 404          RatingNotExistError    The Rating with {ratingId} doesn't exist
 */

@RestController
public class UpdateRatingController {

    @Autowired
    private RatingRepository repository;

    // RequestBody(required=false) because:
    //     1. When there is no Json body in the HTTP request, "question" variable will be null
    //     2. When there is an empty Json body, "question" variable will not be null but have all fields empty
    //        Use Question.isQuestionEmpty(question) to verify

   	@ResponseStatus(HttpStatus.OK)

	@RequestMapping(value = "/questions/{category}/{questionId}/ratings/{ratingId}", method=RequestMethod.PUT)
	public Rating updateRating(@PathVariable String category, @PathVariable String questionId, @PathVariable String ratingId, @RequestBody(required=false) Rating rating)
                                 throws InvalidParameterException, ResourceNotExistException, NoContentException{

	    // no json body in the HTTP request
        if (rating == null) {
            MissingParameterErrorBody body = new MissingParameterErrorBody(MissingParameterErrorBody.generateDescription("Json Object: Rating", "Body"));
            InvalidParameterException exception = new InvalidParameterException("No Json Body in the Request!");
            exception.setErrorBody(body);
            throw exception;
        }

        // empty json body
        if (rating.isAllJsonInputFieldNull()) {
            EmptyParameterErrorBody body = new EmptyParameterErrorBody(EmptyParameterErrorBody.generateDescription("Json Object: Rating", "Body"));
            InvalidParameterException exception = new InvalidParameterException("Empty Json Body in the Request!");
            exception.setErrorBody(body);
            throw exception;
        }

        // complete Rating object must be included in the json body (because this is not partially update)
        // json body doesn't contain all fields of Rating object
        if (!rating.isAllJsonInputFieldNonNull()) {
            String[] nullFieldNameList = rating.getNullJsonInputFieldName();
            if (nullFieldNameList != null) {
                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < nullFieldNameList.length; i++) {
                    if (i != 0)
                        builder.append(",");
                    builder.append(nullFieldNameList[i]);
                }

                MissingParameterErrorBody body = new MissingParameterErrorBody(MissingParameterErrorBody.generateDescription(builder.toString(), "Body - Json Object: Rating"));
                InvalidParameterException exception = new InvalidParameterException("Not All Fields of Rating is in Json Body!");
                exception.setErrorBody(body);
                throw exception;
            }
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

        Rating result = repository.updateRating(rating);

        if (result == null) {
        	throw new ServerSideProblemException("Rating object should not be null");
        }

        if (!result.isAllJsonInputFieldNonNull()) {
        	throw new ServerSideProblemException("Rating field should not be null");
        }

		return result;
	}
}