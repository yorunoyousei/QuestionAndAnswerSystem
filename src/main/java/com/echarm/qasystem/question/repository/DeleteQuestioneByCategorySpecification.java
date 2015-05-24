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

public class DeleteQuestioneByCategorySpecification extends QuestionSpecification {

	public DeleteQuestioneByCategorySpecification(Question question) {
		super(question);
	}

	@Override
	List<Question> doActions(MongoTemplate mongoTemplate) throws ResourceNotExistException, NoContentException{
		// This is the working version code 
		List<Question> resultArr = null;
        
        String category = question.getCategoryStr();
        if(category == null || category.equals("")) {
            throw new ServerSideProblemException("Input question model is not complete!!! Missing `category`");
        }
        Query findQuery = new Query();
        findQuery.addCriteria(Criteria.where("deleted").is(false));
        resultArr = mongoTemplate.find(findQuery, Question.class, category);
        if(resultArr.size() == 0) {
            //System.out.println("");
            throw new NoContentException();
        }
        Query updateQuery = new Query();
        updateQuery.addCriteria(Criteria.where("deleted").is(false));
        Update update = new Update();
        update.set("deleted", true);
        mongoTemplate.updateMulti(updateQuery, update, Question.class, category);
        
		return resultArr;
		
		//return null;
	}

}
