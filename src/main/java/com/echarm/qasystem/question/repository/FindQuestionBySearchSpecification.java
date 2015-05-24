package com.echarm.qasystem.question.repository;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.echarm.qasystem.question.model.Question;

public class FindQuestionBySearchSpecification extends QuestionSpecification {

	public FindQuestionBySearchSpecification(Question question) {
		super(question);
		// TODO Auto-generated constructor stub
	}

	@Override
	List<Question> doActions(MongoTemplate mongoTemplate) {
		// TODO Auto-generated method stub
		return null;
	}

}
