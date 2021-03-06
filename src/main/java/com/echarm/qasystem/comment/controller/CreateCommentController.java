package com.echarm.qasystem.comment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.echarm.qasystem.comment.error.CategoryNotExistErrorBody;
import com.echarm.qasystem.comment.error.EmptyParameterErrorBody;
import com.echarm.qasystem.comment.error.InvalidParameterException;
import com.echarm.qasystem.comment.error.MissingParameterErrorBody;
import com.echarm.qasystem.comment.error.ResourceNotExistException;
import com.echarm.qasystem.comment.error.ServerSideProblemException;
import com.echarm.qasystem.comment.model.Comment;
import com.echarm.qasystem.comment.repository.CommentRepository;
import com.echarm.qasystem.comment.util.Category;

/*
 * API for creating a Comment under Question with {questionId} in {category} collection of the database
 *
 * HTTP Request: POST /questions/{category}/{questionId}/comments
 * HTTP Response:
 *       Status Code  Json Body              Description
 *    1. 201          Comment                Successful Operation
 *    2. 400          ErrorBody              Missing JsonBody
 *    3. 404          CategoryNotExistError  The category doesn't exist
 */
@RestController
public class CreateCommentController {

    @Autowired
    private CommentRepository repository;

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/questions/{category}/{questionId}/comments", method = RequestMethod.POST)
    public Comment createComment(@RequestBody Comment comment, @PathVariable String category, @PathVariable String questionId)
                                 throws InvalidParameterException, ResourceNotExistException {
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

        // no json body in the HTTP request
        if (comment == null) {
            MissingParameterErrorBody body = new MissingParameterErrorBody(MissingParameterErrorBody.generateDescription("Json Object: Comment", "Body"));
            InvalidParameterException exception = new InvalidParameterException("No Json Body in the Request!");
            exception.setErrorBody(body);
            throw exception;
        }

        // empty json body
        if (comment.isAllJsonInputFieldNull()) {
            EmptyParameterErrorBody body = new EmptyParameterErrorBody(EmptyParameterErrorBody.generateDescription("Json Object: Comment", "Body"));
            InvalidParameterException exception = new InvalidParameterException("Empty Json Body in the Request!");
            exception.setErrorBody(body);
            throw exception;
        }

        // some input json fields null
        if (!comment.isAllJsonInputFieldNonNull()) {
            String[] nullFieldNameList = comment.getNullJsonInputFieldName();
            if (nullFieldNameList != null) {
                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < nullFieldNameList.length; i++) {
                    if (i != 0)
                        builder.append(",");
                    builder.append(nullFieldNameList[i]);
                }

                MissingParameterErrorBody body = new MissingParameterErrorBody(MissingParameterErrorBody.generateDescription(builder.toString(), "Body - Json Object: Comment"));
                InvalidParameterException exception = new InvalidParameterException("Not All Fields of Comment is in Json Body!");
                exception.setErrorBody(body);
                throw exception;
            }
        }

        // some input json fields empty
        if (!comment.isAllJsonInputFieldNonEmpty()) {
            String[] emptyFieldNameList = comment.getEmptyJsonInputFieldName();
            if (emptyFieldNameList != null) {
                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < emptyFieldNameList.length; i++) {
                    if (i != 0)
                        builder.append(",");
                    builder.append(emptyFieldNameList[i]);
                }

                EmptyParameterErrorBody body = new EmptyParameterErrorBody(EmptyParameterErrorBody.generateDescription(builder.toString(), "Body - Json Object: Comment"));
                InvalidParameterException exception = new InvalidParameterException("Some Fields of Comment is empty in Json Body!");
                exception.setErrorBody(body);
                throw exception;
            }
        }

        // json input checked, set category, questionId
        comment.setCategory(Category.valueOf(category));
        comment.setQuestionId(questionId);

        Comment result = repository.createComment(comment);

        if (result == null) {
            throw new ServerSideProblemException("Comment object should not be null");
        }

        //TODO check comment's fields

        return result;
    }

}
