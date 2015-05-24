package com.echarm.qasystem.question.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.echarm.qasystem.question.error.CategoryNotExistErrorBody;
import com.echarm.qasystem.question.error.EmptyParameterErrorBody;
import com.echarm.qasystem.question.error.InvalidParameterException;
import com.echarm.qasystem.question.error.MissingParameterErrorBody;
import com.echarm.qasystem.question.error.NoContentException;
import com.echarm.qasystem.question.error.ResourceNotExistException;
import com.echarm.qasystem.question.error.ServerSideProblemException;
import com.echarm.qasystem.question.model.Question;
import com.echarm.qasystem.question.repository.QuestionRepositoryService;
import com.echarm.qasystem.question.util.Category;

/*
 * API for creating an article object in the category specified {category}
 *
 * HTTP Request: POST /articles/{category}
 * HTTP Response:
 *       Status Code  Json Body              Description
 *    1. 200          Article                Successful Operation
 *    2. 400          ErrorBody              The Request is malformed
 *    3. 404          CategoryNotExistError  The category doesn't exist
 */

@RestController
public class CreateQuestionController {

    @Autowired
    private QuestionRepositoryService repository;

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/articles/{category}", method = RequestMethod.POST)
	public Question createArticle(@PathVariable String category, @RequestBody(required=false) Question article)
                                 throws InvalidParameterException, ResourceNotExistException, NoContentException{

	    // no json body in the HTTP request
        if (article == null) {
            MissingParameterErrorBody body = new MissingParameterErrorBody(MissingParameterErrorBody.generateDescription("Json Object: Article", "Body"));
            InvalidParameterException exception = new InvalidParameterException("No Json Body in the Request!");
            exception.setErrorBody(body);
            throw exception;
        }

        // empty json body
        if (Question.isAllQuestionFieldNull(article)) {
            EmptyParameterErrorBody body = new EmptyParameterErrorBody(EmptyParameterErrorBody.generateDescription("Json Object: Article", "Body"));
            InvalidParameterException exception = new InvalidParameterException("Empty Json Body in the Request!");
            exception.setErrorBody(body);
            throw exception;
        }

        // although it dose need some fields of article to post, the unrequired fields (post time) will be automatically filled by API gateway
        // thus, the complete article body in request is required
        // json body doesn't contain all fields of Article object
        if (!Question.isAllQuestionFieldNonNull(article)) {
            String[] nullFieldNameList = article.getNullFieldName();
            if (nullFieldNameList != null) {
                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < nullFieldNameList.length; i++) {
                    if (i != 0)
                        builder.append(",");
                    builder.append(nullFieldNameList[i]);
                }

                MissingParameterErrorBody body = new MissingParameterErrorBody(MissingParameterErrorBody.generateDescription(builder.toString(), "Body - Json Object: Article"));
                InvalidParameterException exception = new InvalidParameterException("Not All Fields of Article is in Json Body!");
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
            ResourceNotExistException exception = new ResourceNotExistException("Category "+category+" Not Exist!");
            exception.setErrorBody(new CategoryNotExistErrorBody(category));
            throw exception;
        }

        // set category
        article.setCategory(Category.valueOf(category));

        // create Article with {category}
        Question result = repository.createQuestion(article);

        if (result == null) {
        	throw new ServerSideProblemException("Article object should not be null");
        }

        if (!Question.isAllQuestionFieldNonNull(result)) {
        	throw new ServerSideProblemException("Article field should not be null");
        }

		return result;
	}
}
