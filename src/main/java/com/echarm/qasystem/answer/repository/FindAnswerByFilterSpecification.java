package com.echarm.qasystem.answer.repository;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.echarm.qasystem.answer.model.Answer;

public class FindAnswerByFilterSpecification extends AnswerSpecification{

	public FindAnswerByFilterSpecification(Answer answer) {
		super(answer);
	}

	@Override
	List<Answer> doActions(MongoTemplate mongoTemplate) {
		return null;
	}

}
