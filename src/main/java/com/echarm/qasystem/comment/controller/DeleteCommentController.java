package com.echarm.qasystem.comment.controller;

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
import com.echarm.qasystem.comment.util.Category;

/*
 * API for deleting the Comment with {commentId} under Question with {questionId} in {category} collection of the database
 *
 * HTTP Request: DELETE /questions/{category}/{questionId}/comments/{commentId}
 * HTTP Response:
 *       Status Code  Json Body              Description
 *    1. 200          array of Comment       Successful Operation
 *    3. 404          CategoryNotExistError  The category doesn't exist
 *    4. 404          QuestionNotExistError   The Question with {questionId} doesn't exist
 *    4. 404          CommentNotExistError   The Comment with {commentId} doesn't exist
 */
@RestController
public class DeleteCommentController {

    @Autowired
    private CommentRepository repository;

    @RequestMapping(value = "/questions/{category}/{questionId}/comments/{commentId}", method=RequestMethod.DELETE)
    public Comment deleteComment(@PathVariable String category, @PathVariable String questionId, @PathVariable String commentId)
                                 throws ResourceNotExistException {
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

        // set category, questionId and commentId
        Comment comment = new Comment();
        comment.setCategory(Category.valueOf(category));
        comment.setQuestionId(questionId);
        comment.setCommentId(commentId);

        // delete comment
        // when Question with {questionId} doesn't exist, ResourceNotExistException is thrown with QuestionNotExistErrorBody
        // when Comment with {commentId} doesn't exist, ResourceNotExistException is thrown with CommentNotExistErrorBody
        Comment result = repository.deleteComment(comment);

        if (result == null) {
            throw new ServerSideProblemException("Comment object should not be null");
        }

        //TODO check comment's fields

        return result;
    }

}
