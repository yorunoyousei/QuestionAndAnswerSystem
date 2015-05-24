package com.echarm.qasystem.answer.error;

public class AnswerNotExistErrorBody extends ErrorBody{
	public static final int CODE = 1003;
	public static final String MESSAGE = "Answer does not exist";

	public AnswerNotExistErrorBody(String answerId) {
		super(CODE, MESSAGE, generateDescription(answerId));
	}

	public static String generateDescription(String answerId) {
		return String.format("Answer with ID: \"%s\" does not exist!", answerId);
	}
}
