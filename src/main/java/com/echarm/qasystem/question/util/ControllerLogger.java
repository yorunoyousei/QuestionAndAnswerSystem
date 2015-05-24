package com.echarm.qasystem.question.util;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.echarm.qasystem.question.error.InvalidParameterException;
import com.echarm.qasystem.question.error.NoContentException;
import com.echarm.qasystem.question.error.ResourceNotExistException;
import com.echarm.qasystem.question.error.ServerSideProblemException;

@Aspect
@Component
public class ControllerLogger {

	private Logger logger = Logger.getLogger(ControllerLogger.class);

	//Log for receiving a HTTP Request
	@Before("execution(* com.echarm.qasystem.question.controller.*Controller.*(..))")
	public void logBeforeController(JoinPoint joinPoint) {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		if (req == null) {
			return;
		}

		logger.info("Receiving Request from " + req.getRemoteAddr());
		logger.info(req.getMethod() + ": " + req.getRequestURI());
		logger.info("Query: " + req.getQueryString());

		//TODO add question printing

		//TODO change these to debug later
		logger.info("Arguments : "
				+ Arrays.toString(joinPoint.getArgs()));
		logger.info("Method Name: " + joinPoint.getSignature().getName());
	}

	//Log for returning a HTTP Response (200)
	@AfterReturning(pointcut = "execution(* com.echarm.qasystem.question.controller.*Controller.*(..))",
			        returning = "result")
	public void logAfterController(JoinPoint joinPoint) {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpServletResponse res = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

		if (req == null || res == null) {
			return;
		}

		logger.info("Sending Response to " + req.getRemoteAddr());
		logger.info("Http Status Code: " + res.getStatus());
	}

	//Log for returning a HTTP Response in GlobalExceptionHandler (not 200)
	@AfterThrowing(pointcut = "execution(* com.echarm.qasystem.question.controller.*Controller.*(..))",
			       throwing = "exception")
	public void logAfterControllerException(JoinPoint joinPoint, Throwable exception) {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		if (req == null) {
			return;
		}

		int statusCode = -1;

		// TODO getting wrong status code when using {res} for some reason, fix later
		if (exception instanceof ResourceNotExistException) {
			statusCode = 404;
		}
		else if (exception instanceof InvalidParameterException) {
			statusCode = 400;
		}
		else if (exception instanceof NoContentException) {
			statusCode = 204;
		}
		else if (exception instanceof ServerSideProblemException) {
			statusCode = 500;
		}

		logger.info("Sending Response to " + req.getRemoteAddr());
		logger.info("Http Status Code: " + statusCode);
	}
}
