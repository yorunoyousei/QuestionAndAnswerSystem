package com.echarm.qasystem.answer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.echarm.qasystem.answer.error.CategoryNotExistErrorBody;
import com.echarm.qasystem.answer.error.EmptyParameterErrorBody;
import com.echarm.qasystem.answer.error.InvalidParameterException;
import com.echarm.qasystem.answer.error.MissingParameterErrorBody;
import com.echarm.qasystem.answer.error.NoContentException;
import com.echarm.qasystem.answer.error.ResourceNotExistException;
import com.echarm.qasystem.answer.error.ServerSideProblemException;
import com.echarm.qasystem.answer.model.Answer;
import com.echarm.qasystem.answer.repository.AnswerRepository;
import com.echarm.qasystem.answer.util.Category;

/*
 * API for updating the Answer with {answerId} under Question with {questionId} in {category} collection of the database
 *
 * HTTP Request: PUT /questions/{category}/{questionId}/answers/{answerId}
 * HTTP Response:
 *       Status Code  Json Body              Description
 *    1. 200          Answer                 Successful Operation
 *    2. 204          null                   Nothing is updated
 *    3. 400          ErrorBody              Missing JsonBody
 *    4. 404          CategoryNotExistError  The category doesn't exist
 *    5. 404          QuestionNotExistError  The Question with {questionId} doesn't exist
 *    6. 404          AnswerNotExistError    The Answer with {answerId} doesn't exist
 */
@RestController
public class UpdateAnswerController {

	@Autowired
	private AnswerRepository repository;

	// RequestBody(required=false) because:
    //     1. When there is no Json body in the HTTP request, "answer" variable will be null
    //     2. When there is an empty Json body, "answer" variable will not be null but have all fields empty
    //        Use Answer.isAnswerEmpty(answer) to verify
	@RequestMapping(value = "/questions/{category}/{questionId}/answers/{answerId}", method=RequestMethod.PUT)
	public Answer updateAnswer(@PathVariable String category, @PathVariable String questionId,
			                     @PathVariable String answerId, @RequestBody(required=false) Answer answer)
			                     throws InvalidParameterException, ResourceNotExistException, NoContentException {

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

        // no json body in the HTTP request
        if (answer == null) {
            MissingParameterErrorBody body = new MissingParameterErrorBody(MissingParameterErrorBody.generateDescription("Json Object: Answer", "Body"));
            InvalidParameterException exception = new InvalidParameterException("No Json Body in the Request!");
            exception.setErrorBody(body);
            throw exception;
        }

        // empty json body
        if (answer.isAllJsonInputFieldNull()) {
            EmptyParameterErrorBody body = new EmptyParameterErrorBody(EmptyParameterErrorBody.generateDescription("Json Object: Answer", "Body"));
            InvalidParameterException exception = new InvalidParameterException("Empty Json Body in the Request!");
            exception.setErrorBody(body);
            throw exception;
        }

        // some input json fields null
        if (!answer.isAllJsonInputFieldNonNull()) {
            String[] nullFieldNameList = answer.getNullJsonInputFieldName();
            if (nullFieldNameList != null) {
                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < nullFieldNameList.length; i++) {
                    if (i != 0)
                        builder.append(",");
                    builder.append(nullFieldNameList[i]);
                }

                MissingParameterErrorBody body = new MissingParameterErrorBody(MissingParameterErrorBody.generateDescription(builder.toString(), "Body - Json Object: Answer"));
                InvalidParameterException exception = new InvalidParameterException("Not All Fields of Answer is in Json Body!");
                exception.setErrorBody(body);
                throw exception;
            }
        }

        // some input json fields empty
        if (!answer.isAllJsonInputFieldNonEmpty()) {
            String[] emptyFieldNameList = answer.getEmptyJsonInputFieldName();
            if (emptyFieldNameList != null) {
                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < emptyFieldNameList.length; i++) {
                    if (i != 0)
                        builder.append(",");
                    builder.append(emptyFieldNameList[i]);
                }

                EmptyParameterErrorBody body = new EmptyParameterErrorBody(EmptyParameterErrorBody.generateDescription(builder.toString(), "Body - Json Object: Answer"));
                InvalidParameterException exception = new InvalidParameterException("Some Fields of Answer is empty in Json Body!");
                exception.setErrorBody(body);
                throw exception;
            }
        }

        // json input checked, set category, questionId and answerId
        answer.setCategory(Category.valueOf(category));
        answer.setQuestionId(questionId);
//        answer.setAnswerId(answerId);

        // update Answer
        // when Question with {questionId} doesn't exist, ResourceNotExistException is thrown with QuestionNotExistErrorBody
        // when Answer with {answerId} doesn't exist, ResourceNotExistException is thrown with AnswerNotExistErrorBody
        // When nothing is updated (the question in request body is
        // the same as the one in the database), NoContentException is thrown
        Answer result = repository.updateAnswer(answer);

        if (result == null) {
            throw new ServerSideProblemException("Answer object should not be null");
        }

        //TODO check answer's fields

		return result;
	}
}
