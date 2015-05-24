package com.echarm.qasystem.question.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.echarm.qasystem.question.error.NoContentException;
import com.echarm.qasystem.question.error.ResourceNotExistException;
import com.echarm.qasystem.question.error.ServerSideProblemException;
import com.echarm.qasystem.question.model.Question;
import com.echarm.qasystem.question.repository.QuestionRepositoryService;
import com.echarm.qasystem.question.repository.QuestionSpecification;
import com.echarm.qasystem.question.repository.QuestionSpecificationFactory;

/*
 * API for deleting the articles specified in the query field
 *
 * HTTP Request: DELETE /articles
 * HTTP Response:
 *       Status Code  Json Body          Description
 *    1. 200          Array of Articles  Successful Operation
 *    2. 204          null               No Articles Deleted
 *    3. 400          ErrorBody          The Request is Malformed
 */

@RestController
public class DeleteAllQuestionController {

	@Autowired
	private QuestionRepositoryService repository;

	@RequestMapping(value = "/articles", method = RequestMethod.DELETE)
	public List<Question> deleteAllArticle() throws NoContentException, ResourceNotExistException{

		// repository null, server error
		if (repository == null) {
		    throw new ServerSideProblemException("repository null");
		}

		// delete all articles
		// when no articles found, NoContentException is thrown
		QuestionSpecification deleteAllArticleSpec = QuestionSpecificationFactory.getDeleteQuestionAllSpecification(null);
		List<Question> results = repository.query(deleteAllArticleSpec);

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
