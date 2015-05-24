package com.echarm.qasystem.rating.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.echarm.qasystem.rating.error.ErrorBody;
import com.echarm.qasystem.rating.error.InvalidParameterException;
import com.echarm.qasystem.rating.error.NoContentException;
import com.echarm.qasystem.rating.error.ResourceNotExistException;
import com.echarm.qasystem.rating.error.ServerSideProblemException;

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
