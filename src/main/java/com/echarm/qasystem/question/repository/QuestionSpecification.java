package com.echarm.qasystem.question.repository;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.echarm.qasystem.question.error.NoContentException;
import com.echarm.qasystem.question.error.ResourceNotExistException;
import com.echarm.qasystem.question.model.Question;

public abstract class QuestionSpecification {
	protected Question question;

	public QuestionSpecification(Question article){
		this.question = article;
	}

	abstract List<Question> doActions(MongoTemplate mongoTemplate) throws ResourceNotExistException, NoContentException;
}
