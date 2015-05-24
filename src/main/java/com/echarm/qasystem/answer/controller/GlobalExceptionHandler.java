package com.echarm.qasystem.answer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.echarm.qasystem.answer.error.ErrorBody;
import com.echarm.qasystem.answer.error.InvalidParameterException;
import com.echarm.qasystem.answer.error.NoContentException;
import com.echarm.qasystem.answer.error.ResourceNotExistException;
import com.echarm.qasystem.answer.error.ServerSideProblemException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NoContentException.class)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public @ResponseBody ErrorBody handleNoContentException(NoContentException exception) {
		return exception.getErrorBody();
	}

	@ExceptionHandler(ResourceNotExistException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public @ResponseBody ErrorBody handleResourceNotExistException(ResourceNotExistException exception) {
		return exception.getErrorBody();
	}

	@ExceptionHandler(InvalidParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorBody handleInvalidParameterException(InvalidParameterException exception) {
		return exception.getErrorBody();
	}

	@ExceptionHandler(ServerSideProblemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorBody handleServerSideProblemException(ServerSideProblemException exception) {
        return exception.getErrorBody();
    }
}
