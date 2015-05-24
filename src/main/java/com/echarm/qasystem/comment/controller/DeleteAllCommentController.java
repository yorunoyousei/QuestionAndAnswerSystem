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
 * API for deleting All Comments under Question with {questionId} in {category} collection of the database
 *
 * HTTP Request: POST /questions/{category}/{questionId}/comments
 * HTTP Response:
 *       Status Code  Json Body              Description
 *    1. 200          array of Comment       Successful Operation
 *    2. 204          null                   No comments under this question
 *    3. 404          CategoryNotExistError  The category doesn't exist
 *    4. 404          QuestionNotExistError   The Question with {questionId} doesn't exist
 */
@RestController
public class DeleteAllCommentController {

    @Autowired
    private CommentRepository repository;

    @RequestMapping(value = "/questions/{category}/{questionId}/comments", method = RequestMethod.DELETE)
    public List<Comment> deleteAllCommentController(@PathVariable String category, @PathVariable String questionId) throws NoContentException, ResourceNotExistException {

        // repository null, server error
        if (repository == null) {
            throw new com.echarm.qasystem.comment.error.ServerSideProblemException("repository null");
        }

        // category doesn't exist, throw ResourceNotExistException
        if (Category.isCategoryExist(category) == null) {
            ResourceNotExistException exception = new ResourceNotExistException("Category Not Exist!");
            exception.setErrorBody(new CategoryNotExistErrorBody(category));
            throw exception;
        }

        // set category and questionId
        Comment comment = new Comment();
        comment.setCategory(Category.valueOf(category));
        comment.setQuestionId(questionId);

        // delete all comments
        // when Question with {questionId} doesn't exist, ResourceNotExistException is thrown with QuestionNotExistErrorBody
        // when no comments exist under this question, NoContentException is thrown
        CommentSpecification specification = CommentSpecificationFactory.getDeleteAllCommentSpecification(comment);
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
