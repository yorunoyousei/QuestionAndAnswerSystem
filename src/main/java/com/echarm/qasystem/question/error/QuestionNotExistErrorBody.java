package com.echarm.qasystem.question.error;

public class QuestionNotExistErrorBody extends ErrorBody{
	public static final int CODE = 1002;
	public static final String MESSAGE = "Question does not exist";
	
	public QuestionNotExistErrorBody(String articleId) {
		super(CODE, MESSAGE, generateDescription(articleId));
	}
	
	public static String generateDescription(String articleId) {
		return String.format("Question with ID: \"%s\" does not exist!", articleId);
	}
}
