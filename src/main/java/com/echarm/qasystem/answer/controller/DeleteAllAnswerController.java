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
 * API for deleting All Answers under Question with {questionId} in {category} collection of the database
 *
 * HTTP Request: DELETE /questions/{category}/{questionId}/answers
 * HTTP Response:
 *       Status Code  Json Body              Description
 *    1. 200          array of Answer        Successful Operation
 *    2. 204          null                   No answers under this question
 *    3. 404          CategoryNotExistError  The category doesn't exist
 *    4. 404          QuestionNotExistError  The Question with {questionId} doesn't exist
 */
@RestController
public class DeleteAllAnswerController {

    @Autowired
    private AnswerRepository repository;

    @RequestMapping(value = "/questions/{category}/{questionId}/answers", method = RequestMethod.DELETE)
    public List<Answer> deleteAllAnswerController(@PathVariable String category, @PathVariable String questionId) throws NoContentException, ResourceNotExistException {

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

        // set category and questionId
        Answer answer = new Answer();
        answer.setCategory(Category.valueOf(category));
        answer.setQuestionId(questionId);

        // delete all answers
        // when Question with {questionId} doesn't exist, ResourceNotExistException is thrown with QuestionNotExistErrorBody
        // when no answers exist under this question, NoContentException is thrown
        AnswerSpecification specification = AnswerSpecificationFactory.getDeleteAllAnswerSpecification(answer);
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
