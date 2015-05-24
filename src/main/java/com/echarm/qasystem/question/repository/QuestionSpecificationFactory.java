package com.echarm.qasystem.question.repository;

import com.echarm.qasystem.question.model.Question;

/*
 * Factory class for QuestionSpecification subclasses
 */
public class QuestionSpecificationFactory {

	// get FindArticleAllSpecification
	public static QuestionSpecification getFindQuestionAllSpecification(Question question) {
		return new FindQuestionAllSpecification(question);
	}

	// get FindArticleByCategorySpecification
	public static QuestionSpecification getFindQuestionByCategorySpecification(Question question) {
		return new FindQuestionByCategorySpecification(question);
	}

	// get FindArticleByFilterSpecification
	public static QuestionSpecification getFindQuestionByFilterSpecification(Question question) {
		return new FindQuestionByFilterSpecification(question);
	}

	// get FindArticleByIdSpecification
	public static QuestionSpecification getFindQuestionByIdSpecification(Question question) {
		return new FindQuestionByIdSpecification(question);
	}

	// get FindArticleBySearchSpecification
	public static QuestionSpecification getFindQuestionBySearchSpecification(Question question) {
		return new FindQuestionBySearchSpecification(question);
	}

	// get DeleteArticleAllSpecification
	public static QuestionSpecification getDeleteQuestionAllSpecification(Question question) {
		return new DeleteQuestionAllSpecification(question);
	}

	// get DeleteArticleByCategorySpecification
	public static QuestionSpecification getDeleteQuestionByCategorySpecification(Question question) {
		return new DeleteQuestioneByCategorySpecification(question);
	}
}
