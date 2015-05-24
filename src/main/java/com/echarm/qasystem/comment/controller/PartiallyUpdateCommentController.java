package com.echarm.qasystem.comment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.echarm.qasystem.comment.error.CategoryNotExistErrorBody;
import com.echarm.qasystem.comment.error.EmptyParameterErrorBody;
import com.echarm.qasystem.comment.error.InvalidParameterException;
import com.echarm.qasystem.comment.error.MissingParameterErrorBody;
import com.echarm.qasystem.comment.error.NoContentException;
import com.echarm.qasystem.comment.error.ResourceNotExistException;
import com.echarm.qasystem.comment.error.ServerSideProblemException;
import com.echarm.qasystem.comment.model.Comment;
import com.echarm.qasystem.comment.repository.CommentRepository;
import com.echarm.qasystem.comment.util.Category;

/*
 * API for partially updating the Comment with {commentId} under Question with {questionId} in {category} collection of the database
 *
 * HTTP Request: PATCH /questions/{category}/{questionId}/comments/{commentId}
 * HTTP Response:
 *       Status Code  Json Body              Description
 *    1. 200          Comment                Successful Operation
 *    2. 204          null                   Nothing is updated
 *    3. 400          ErrorBody              Missing JsonBody
 *    4. 404          CategoryNotExistError  The category doesn't exist
 *    5. 404          QuestionNotExistError   The Question with {questionId} doesn't exist
 *    6. 404          CommentNotExistError   The Comment with {commentId} doesn't exist
 */
@RestController
public class PartiallyUpdateCommentController {

    @Autowired
    private CommentRepository repository;

    // RequestBody(required=false) because:
    //     1. When there is no Json body in the HTTP request, "comment" variable will be null
    //     2. When there is an empty Json body, "comment" variable will not be null but have all fields empty
    //        Use Comment.isCommentEmpty(comment) to verify
	@RequestMapping(value = "/questions/{category}/{questionId}/comments/{commentId}", method=RequestMethod.PATCH)
	public Comment partiallyUpdateComment(@PathVariable String category, @PathVariable String questionId,
			                              @PathVariable String commentId, @RequestBody(required=false) Comment comment)
			throws InvalidParameterException, ResourceNotExistException, NoContentException {

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

        // json input checked, set category, questionId and commentId
        comment.setCategory(Category.valueOf(category));
        comment.setQuestionId(questionId);
        comment.setCommentId(commentId);

        // partially update Comment
        // when Question with {questionId} doesn't exist, ResourceNotExistException is thrown with QuestionNotExistErrorBody
        // when Comment with {commentId} doesn't exist, ResourceNotExistException is thrown with CommentNotExistErrorBody
        // When nothing is updated (the question in request body is
        // the same as the one in the database), NoContentException is thrown
        Comment result = repository.updateComment(comment);

        if (result == null) {
            throw new ServerSideProblemException("Comment object should not be null");
        }

        //TODO check comment's fields

		return result;
	}
}
