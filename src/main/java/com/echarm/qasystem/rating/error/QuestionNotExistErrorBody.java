package com.echarm.qasystem.rating.error;

public class QuestionNotExistErrorBody extends ErrorBody{
	public static final int CODE = 1002;
	public static final String MESSAGE = "Question does not exist";
	
	public QuestionNotExistErrorBody(String questionId) {
		super(CODE, MESSAGE, generateDescription(questionId));
	}
	
	public static String generateDescription(String questionId) {
		return String.format("Question with ID: \"%s\" does not exist!", questionId);
	}
}
