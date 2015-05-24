package com.echarm.qasystem.answer.repository;

import com.echarm.qasystem.answer.model.Answer;

public class AnswerSpecificationFactory {

	public static AnswerSpecification getFindAllAnswerSpecification(Answer comment) {
		return new FindAllAnswerSpecification(comment);
	}

	public static AnswerSpecification getFindCommentByFilterSpecification(Answer comment) {
		return new FindAnswerByFilterSpecification(comment);
	}

	public static AnswerSpecification getFindAnswerByIdSpecification(Answer comment) {
		return new FindAnswerByIdSpecification(comment);
	}

	public static AnswerSpecification getFindCommentBySearchSpecification(Answer comment) {
		return new FindAnswerBySearchSpecification(comment);
	}

	public static AnswerSpecification getDeleteAllAnswerSpecification(Answer comment) {
		return new DeleteAllAnswerSpecification(comment);
	}

}
