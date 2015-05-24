package com.echarm.qasystem.question.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
 * API for updating the Article with {articleId} in {category} collection of the database
 *
 * HTTP Request: PUT /articles/{category}/{articleId}
 * HTTP Response:
 *       Status Code  Json Body              Description
 *    1. 200          Article                Successful Operation
 *    2. 204          null                   Nothing is updated
 *    3. 400          ErrorBody              Missing JsonBody
 *    4. 404          CategoryNotExistError  The category doesn't exist
 *    5. 404          ArticleNotExistError   The Article with {articleId} doesn't exist
 */
@RestController
public class UpdateQuestionController {

    @Autowired
    private QuestionRepositoryService repository;

    // RequestBody(required=false) because:
    //     1. When there is no Json body in the HTTP request, "article" variable will be null
    //     2. When there is an empty Json body, "article" variable will not be null but have all fields empty
    //        Use Article.isArticleEmpty(article) to verify
	@RequestMapping(value = "/articles/{category}/{articleId}", method=RequestMethod.PUT)
	public Question updateArticle(@PathVariable String category, @PathVariable String articleId, @RequestBody(required=false) Question article)
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

        // complete Article object must be included in the json body (because this is not partially update)
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
            ResourceNotExistException exception = new ResourceNotExistException("Category Not Exist!");
            exception.setErrorBody(new CategoryNotExistErrorBody(category));
            throw exception;
        }

        // set category and articleId
        article.setCategory(Category.valueOf(category));
        article.setQuestionId(articleId);

        // update Article with {articleId}
        // when Article with {articleId} doesn't exist, ResourceNotExistException is thrown
        // When nothing is updated (the article in request body is
        // the same as the one in the database), NoContentException is thrown
        Question result = repository.updateQuestion(article);

        if (result == null) {
        	throw new ServerSideProblemException("Article object should not be null");
        }

        if (!Question.isAllQuestionFieldNonNull(result)) {
        	throw new ServerSideProblemException("Article field should not be null");
        }

		return result;
	}
}
