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
 * API for deleting the articles specified in the query field of {category}. 
 * If nothing is specified in the query, delete all the articles in that category.
 *
 * HTTP Request: DELETE /articles/{category}
 * HTTP Response:
 *       Status Code  Json Body              Description
 *    1. 200          Array of Articles      Successful Operation
 *    2. 204          null                   Nothing deleted
 *    3. 400          ErrorBody              The Request is malformed
 *    4. 404          CategoryNotExistError  The specified category not found
 */

@RestController
public class DeleteQuestionInCategoryController {

	@Autowired
	private QuestionRepositoryService repository;

	@RequestMapping(value = "/articles/{category}", method = RequestMethod.DELETE)
	public List<Question> deleteArticleInCategory(@PathVariable String category) throws ResourceNotExistException, NoContentException{

	    // repository null, server error
	    if (repository == null) {
	        throw new ServerSideProblemException("repository null");
	    }

	    // category doesn't exist, throw ResourceNotExistException
	    if (Category.isCategoryExist(category) == null) {
	        ResourceNotExistException exception = new ResourceNotExistException("Category " + category +" Not Exist!");
	        exception.setErrorBody(new CategoryNotExistErrorBody(category));
	        throw exception;
	    }

	    // set category
	    Question article = new Question();
	    article.setCategory(Category.valueOf(category));

	    // delete all articles under {category}
	    // when no articles found, NoContentException is thrown
        QuestionSpecification deleteCategoryArticleSpec = QuestionSpecificationFactory.getDeleteQuestionByCategorySpecification(article);
        List<Question> results = repository.query(deleteCategoryArticleSpec);

        // query result should not be null, server error
     	if (results == null) {
     		throw new ServerSideProblemException("List<Article> should not be null");
     	}

     	// query result should have at least one element
        // if not, server error
        if (results.size() <= 0) {
            throw new ServerSideProblemException("List<Article> size should be at least 1");
        }

	    return results;		
	}
}
