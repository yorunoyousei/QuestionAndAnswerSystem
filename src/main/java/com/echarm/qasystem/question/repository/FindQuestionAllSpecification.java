package com.echarm.qasystem.question.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.echarm.qasystem.question.error.NoContentException;
import com.echarm.qasystem.question.error.ResourceNotExistException;
import com.echarm.qasystem.question.error.ServerSideProblemException;
import com.echarm.qasystem.question.model.Question;
import com.echarm.qasystem.question.util.Category;

public class FindQuestionAllSpecification extends QuestionSpecification {

	public FindQuestionAllSpecification(Question question) {
		super(question);
	}

	@Override
	List<Question> doActions(MongoTemplate mongoTemplate) throws ResourceNotExistException, NoContentException {
		System.out.println("yoru enter");
		List<Question> list = new ArrayList<Question>();
		List<Question> tempList = null;
		for(String name : mongoTemplate.getCollectionNames()) {
			Query findQuery = new Query();
			findQuery.addCriteria(Criteria.where("deleted").is(false));
			tempList = mongoTemplate.find(findQuery, Question.class, name);
			if(tempList != null)
				list.addAll(tempList);
		}
		if(list.size() == 0) {
			throw new NoContentException();
		}
		return list;
	}

}
