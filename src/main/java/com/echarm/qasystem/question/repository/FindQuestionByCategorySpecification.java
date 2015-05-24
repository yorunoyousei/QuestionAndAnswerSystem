package com.echarm.qasystem.question.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.echarm.qasystem.question.error.NoContentException;
import com.echarm.qasystem.question.error.ResourceNotExistException;
import com.echarm.qasystem.question.error.ServerSideProblemException;
import com.echarm.qasystem.question.model.Question;
import com.echarm.qasystem.question.util.Category;

public class FindQuestionByCategorySpecification extends QuestionSpecification {

	public FindQuestionByCategorySpecification(Question question) {
		super(question);
	}

	@Override
	List<Question> doActions(MongoTemplate mongoTemplate) throws ServerSideProblemException, ResourceNotExistException, NoContentException {
		
		List<Question> list = null;
		String colName = question.getCategory().toString();
		if(Category.isCategoryExist(colName) == null) {
			throw new ServerSideProblemException("Such category doesn't exist");
		}
		list = mongoTemplate.findAll(Question.class, colName);
		if(list.size() == 0) {
			throw new NoContentException();
		}
		//
		List<Question> endList = new ArrayList<Question>();
		for(Question question : list){
			if(!question.getDeleted()){
				endList.add(question);
			}
		}
		if(endList.size() == 0) 
			throw new NoContentException();
		else
			return endList;
	}

}
