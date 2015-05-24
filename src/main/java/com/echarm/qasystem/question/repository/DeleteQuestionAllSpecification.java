package com.echarm.qasystem.question.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.echarm.qasystem.question.error.NoContentException;
import com.echarm.qasystem.question.error.ResourceNotExistException;
import com.echarm.qasystem.question.error.ServerSideProblemException;
import com.echarm.qasystem.question.model.Question;

public class DeleteQuestionAllSpecification extends QuestionSpecification {

	public DeleteQuestionAllSpecification(Question question) {
		super(question);
	}

	@Override
	List<Question> doActions(MongoTemplate mongoTemplate) throws ResourceNotExistException, NoContentException{
		List<Question> list = new ArrayList<Question>();
		for(String name : mongoTemplate.getCollectionNames()) {
			System.out.println(name);
			List<Question> tempList = null;
			tempList = mongoTemplate.findAll(Question.class, name);
			if(tempList != null)
				list.addAll(tempList);
			if (name.compareTo("system.indexes")!=0)
			{
				Query updateQuery = new Query();
				updateQuery.addCriteria(Criteria.where("deleted").is(false));
				Update update = new Update();
				update.set("deleted", true);
				mongoTemplate.updateMulti(updateQuery, update, Question.class, name);
			}
			
		}
		if(list.size() == 0) {
			System.out.println("No question can be found!");
			throw new ResourceNotExistException("No question can be found!");
		}
		//
		return list;
	}

}
