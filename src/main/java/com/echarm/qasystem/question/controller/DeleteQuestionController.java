package com.echarm.qasystem.question.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.echarm.qasystem.question.error.CategoryNotExistErrorBody;
import com.echarm.qasystem.question.error.NoContentException;
import com.echarm.qasystem.question.error.ResourceNotExistException;
import com.echarm.qasystem.question.error.ServerSideProblemException;
import com.echarm.qasystem.question.model.Question;
import com.echarm.qasystem.question.repository.QuestionRepositoryService;
import com.echarm.qasystem.question.repository.QuestionSpecification;
import com.echarm.qasystem.question.repository.QuestionSpecificationFactory;
import com.echarm.qasystem.question.util.Category;

/*
 * API for deleting the specified article with {article_id}.
 *
 * HTTP Request: DELETE /articles/{category}/{articleId}
 * HTTP Response:
 *       Status Code  Json Body              Description
 *    1. 200          Article                Successful Operation
 *    2. 400          ErrorBody              The Request is malformed
 *    3. 404          CategoryNotExistError  The category doesn't exist
 *    4. 404          ArticleNotExistError   The Article with {articleId} doesn't exist
 */

@RestController
public class DeleteQuestionController {

    @Autowired
    private QuestionRepositoryService repository;

	@RequestMapping(value = "/articles/{category}/{articleId}", method=RequestMethod.DELETE)
	public Question deleteArticle(@PathVariable String category, @PathVariable String articleId) throws ResourceNotExistException, NoContentException{

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

        // set category and articleId
        Question article = new Question();
        article.setCategory(Category.valueOf(category));
        article.setQuestionId(articleId);

        // delete Article with {articleId}
        // when Article with {articleId} doesn't exist, ResourceNotExistException will be thrown
        Question result = repository.deleteQuestion(article);

        // query result should not be null, server error
        if (result == null) {
        	throw new ServerSideProblemException("List<Article> should not be null");
        }

        // to check whether the field of 'result' is null
        if (!Question.isAllQuestionFieldNonNull(result)) {
        	throw new ServerSideProblemException("Article field should not be null");
        }

		return result;
	}
}