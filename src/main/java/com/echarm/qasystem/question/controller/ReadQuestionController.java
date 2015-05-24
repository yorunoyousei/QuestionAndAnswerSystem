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
 * API for retrieving the Article with {articleId} in {category} collection of the database
 *
 * HTTP Request: GET /articles/{category}/{articleId}
 * HTTP Response:
 *       Status Code  Json Body              Description
 *    1. 200          Article                Successful Operation
 *    2. 400          ErrorBody              The Request is malformed
 *    3. 404          CategoryNotExistError  The category doesn't exist
 *    4. 404          ArticleNotExistError   The Article with {articleId} doesn't exist
 */
@RestController
public class ReadQuestionController {

    @Autowired
    private QuestionRepositoryService repository;

	@RequestMapping(value = "/articles/{category}/{articleId}", method=RequestMethod.GET)
	public Question readArticle(@PathVariable String category, @PathVariable String articleId) throws ResourceNotExistException, NoContentException{

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
        Question article = new Question();
        article.setCategory(Category.valueOf(category));
        article.setQuestionId(articleId);

        // retrieve Article with {articleId}
        // when Article with {articleId} doesn't exist, ResourceNotExistException will be thrown
        QuestionSpecification readAllArticleSpec = QuestionSpecificationFactory.getFindQuestionByIdSpecification(article);
        List<Question> results = repository.query(readAllArticleSpec);

        // query result should not be null, server error
        if (results == null) {
        	throw new ServerSideProblemException("List<Article> should not be null");
        }

        // query result should have only one element
        // if not, server error
        if (results.size() != 1) {
            throw new ServerSideProblemException("List<Article> size should be 1");
        }

		return results.get(0);
	}
}
