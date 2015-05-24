package com.echarm.qasystem.comment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.echarm.qasystem.comment.error.CategoryNotExistErrorBody;
import com.echarm.qasystem.comment.error.NoContentException;
import com.echarm.qasystem.comment.error.ResourceNotExistException;
import com.echarm.qasystem.comment.error.ServerSideProblemException;
import com.echarm.qasystem.comment.model.Comment;
import com.echarm.qasystem.comment.repository.CommentRepository;
import com.echarm.qasystem.comment.repository.CommentSpecification;
import com.echarm.qasystem.comment.repository.CommentSpecificationFactory;
import com.echarm.qasystem.comment.util.Category;

/*
 * API for retrieving all comments under question with {questionId} in category with {category} in the database
 *
 * HTTP Request: GET /questions/{category}/{questionId}/comment
 * HTTP Response:
 *       Status Code  Json Body                  Description
 *    1. 200          Array of Comments          Successful Operation
 *    2. 204          null                       No Comments Found under this question
 *    3. 400          ErrorBody                  The Request is malformed
 *    4. 404          CategoryNotExistErrorBody  Category with {category} not exist
 *    5. 404          QuestionNotExistErrorBody   Question with {questionId} not found
 */
@RestController
public class ReadAllCommentController {

	@Autowired
	private CommentRepository repository;

	@RequestMapping(value = "/questions/{category}/{questionId}/comments", method = RequestMethod.GET)
	public List<Comment> readAllComment(@PathVariable String category, @PathVariable String questionId) throws NoContentException, ResourceNotExistException{

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
		Comment comment = new Comment();
		comment.setCategory(Category.valueOf(category));
		comment.setQuestionId(questionId);

		// retrieve all comments under question
		// if no question with {questionId} in {category} throw ResourceNotExistException(QuestionNotExistErrorBody)
		// if no comments under this question, throw NoContentException
		CommentSpecification specification = CommentSpecificationFactory.getFindAllCommentSpecification(comment);
		List<Comment> results = repository.query(specification);

		// query result should not be null, server error
     	if (results == null) {
     		throw new ServerSideProblemException("List<Comment> should not be null");
     	}

        // query result should have at least one element
        // if not, server error
        if (results.size() <= 0) {
            throw new ServerSideProblemException("List<Comment> size should be at least 1");
        }

		return results;
	}
}
