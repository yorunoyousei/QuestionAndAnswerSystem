package com.echarm.qasystem.question.repository;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.echarm.qasystem.question.model.Question;

public class FindQuestionByFilterSpecification extends QuestionSpecification {

	public FindQuestionByFilterSpecification(Question question) {
		super(question);
	}

	@Override
	List<Question> doActions(MongoTemplate mongoTemplate) {
		return null;
	}

}
