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
 * API for retrieving all articles in the database
 *
 * HTTP Request: GET /articles
 * HTTP Response:
 *       Status Code  Json Body          Description
 *    1. 200          Array of Articles  Successful Operation
 *    2. 204          null               No Articles Found
 *    3. 400          ErrorBody          The Request is malformed
 */
@RestController
public class ReadAllQuestionController {

	@Autowired
	private QuestionRepositoryService repository;

	@RequestMapping(value = "/articles", method = RequestMethod.GET)
	public List<Question> readAllArticle() throws NoContentException, ResourceNotExistException{

		// repository null, server error
		if (repository == null) {
		    throw new ServerSideProblemException("repository null");
		}

		// retrieve all articles
		// when no articles found, NoContentException is thrown
		QuestionSpecification readAllArticleSpec = QuestionSpecificationFactory.getFindQuestionAllSpecification(null);
		List<Question> results = repository.query(readAllArticleSpec);

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
