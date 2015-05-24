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
import com.echarm.qasystem.rating.error.ResourceNotExistException;
import com.echarm.qasystem.rating.error.ServerSideProblemException;
import com.echarm.qasystem.rating.model.Rating;
import com.echarm.qasystem.rating.repository.RatingRepository;
import com.echarm.qasystem.rating.util.Category;

/*
 * API for creating a Rating under Question with {questionId} in {category} collection of the database
 *
 * HTTP Request: POST /questions/{category}/{questionId}/ratings
 * HTTP Response:
 *       Status Code  Json Body              Description
 *    1. 201          Rating                 Successful Operation
 *    2. 400          ErrorBody              Missing JsonBody
 *    3. 404          CategoryNotExistError  The category doesn't exist
 */

@RestController
public class CreateRatingController {
	
	@Autowired
    private RatingRepository repository;
	
	@ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/questions/{category}/{questionId}/ratings", method = RequestMethod.POST)
	public Rating createRating(@PathVariable String category, @PathVariable String questionId, @RequestBody(required=false) Rating rating)
			throws InvalidParameterException, ResourceNotExistException{
		
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
        
        // some input json fields null
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
        
        // some input json fields empty
        if (!rating.isAllJsonInputFieldNonEmpty()) {
            String[] emptyFieldNameList = rating.getEmptyJsonInputFieldName();
            if (emptyFieldNameList != null) {
                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < emptyFieldNameList.length; i++) {
                    if (i != 0)
                        builder.append(",");
                    builder.append(emptyFieldNameList[i]);
                }

                EmptyParameterErrorBody body = new EmptyParameterErrorBody(EmptyParameterErrorBody.generateDescription(builder.toString(), "Body - Json Object: Rating"));
                InvalidParameterException exception = new InvalidParameterException("Some Fields of Rating is empty in Json Body!");
                exception.setErrorBody(body);
                throw exception;
            }
        }
        
        // set category and questionID
        rating.setCategory(Category.valueOf(category));
        rating.setQuestionId(questionId);
        
        Rating result = repository.createRating(rating);
        
        if (result == null) {
            throw new ServerSideProblemException("Rating object should not be null");
        }

        if (!result.isAllJsonInputFieldNonNull()) {
        	throw new ServerSideProblemException("Question field should not be null");
        }
        
        return result;
	}
}
