package com.echarm.qasystem.answer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.echarm.qasystem.answer.error.CategoryNotExistErrorBody;
import com.echarm.qasystem.answer.error.NoContentException;
import com.echarm.qasystem.answer.error.ResourceNotExistException;
import com.echarm.qasystem.answer.error.ServerSideProblemException;
import com.echarm.qasystem.answer.model.Answer;
import com.echarm.qasystem.answer.repository.AnswerRepository;
import com.echarm.qasystem.answer.repository.AnswerSpecification;
import com.echarm.qasystem.answer.repository.AnswerSpecificationFactory;
import com.echarm.qasystem.answer.util.Category;

/*
 * API for retrieving all answers under question with {questionId} in category with {category} in the database
 *
 * HTTP Request: GET /questions/{category}/{questionId}/answer
 * HTTP Response:
 *       Status Code  Json Body                  Description
 *    1. 200          Array of Answers           Successful Operation
 *    2. 204          null                       No Answers Found under this question
 *    3. 400          ErrorBody                  The Request is malformed
 *    4. 404          CategoryNotExistErrorBody  Category with {category} not exist
 *    5. 404          QuestionNotExistErrorBody  Question with {questionId} not found
 */
@RestController
public class ReadAllAnswerController {

	@Autowired
	private AnswerRepository repository;

	@RequestMapping(value = "/questions/{category}/{questionId}/answers", method = RequestMethod.GET)
	public List<Answer> readAllAnswer(@PathVariable String category, @PathVariable String questionId) throws NoContentException, ResourceNotExistException{

		// repository null, server error
		if (repository == null) {
		    throw new ServerSideProblemException("repository null");
		}

		// category not exist, throw ResourceNotExistException
		if (Category.isCategoryExist(category) == null) {
			ResourceNotExistException exception = new ResourceNotExistException("Category Not Exist");
			exception.setErrorBody(new CategoryNotExistErrorBody(category));
			throw exception;
		}

		// set category and questionId
		Answer answer = new Answer();
		answer.setCategory(Category.valueOf(category));
		answer.setQuestionId(questionId);

		// retrieve all answers under question
		// if no question with {questionId} in {category} throw ResourceNotExistException(QuestionNotExistErrorBody)
		// if no answers under this question, throw NoContentException
		AnswerSpecification specification = AnswerSpecificationFactory.getFindAllAnswerSpecification(answer);
		List<Answer> results = repository.query(specification);

		// query result should not be null, server error
     	if (results == null) {
     		throw new ServerSideProblemException("List<Answer> should not be null");
     	}

        // query result should have at least one element
        // if not, server error
        if (results.size() <= 0) {
            throw new ServerSideProblemException("List<Answer> size should be at least 1");
        }

		return results;
	}
}
