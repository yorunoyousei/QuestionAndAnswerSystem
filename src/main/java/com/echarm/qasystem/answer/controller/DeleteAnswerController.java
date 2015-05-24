package com.echarm.qasystem.answer.controller;

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
import com.echarm.qasystem.answer.util.Category;

/*
 * API for deleting the Comment with {answerId} under Question with {questionId} in {category} collection of the database
 *
 * HTTP Request: DELETE /questions/{category}/{questionId}/answers/{answerId}
 * HTTP Response:
 *       Status Code  Json Body              Description
 *    1. 200          array of Comment       Successful Operation
 *    3. 404          CategoryNotExistError  The category doesn't exist
 *    4. 404          QuestionNotExistError   The Question with {questionId} doesn't exist
 *    4. 404          CommentNotExistError   The Comment with {answerId} doesn't exist
 */
@RestController
public class DeleteAnswerController {

    @Autowired
    private AnswerRepository repository;

    @RequestMapping(value = "/questions/{category}/{questionId}/answers/{answerId}", method=RequestMethod.DELETE)
    public Answer deleteComment(@PathVariable String category, @PathVariable String questionId, @PathVariable String answerId)
                                 throws ResourceNotExistException {
        // repository null, server error
        if (repository == null) {
            throw new com.echarm.qasystem.answer.error.ServerSideProblemException("repository null");
        }

        // category doesn't exist, throw ResourceNotExistException
        if (Category.isCategoryExist(category) == null) {
            ResourceNotExistException exception = new ResourceNotExistException("Category Not Exist!");
            exception.setErrorBody(new CategoryNotExistErrorBody(category));
            throw exception;
        }

        // set category, questionId and answerId
        Answer answer = new Answer();
        answer.setCategory(Category.valueOf(category));
        answer.setQuestionId(questionId);
//        answer.setAnswerId(answerId);

        // delete answer
        // when Question with {questionId} doesn't exist, ResourceNotExistException is thrown with QuestionNotExistErrorBody
        // when Comment with {answerId} doesn't exist, ResourceNotExistException is thrown with CommentNotExistErrorBody
        Answer result = repository.deleteAnswer(answer);

        if (result == null) {
            throw new ServerSideProblemException("Comment object should not be null");
        }

        //TODO check answer's fields

        return result;
    }

}
