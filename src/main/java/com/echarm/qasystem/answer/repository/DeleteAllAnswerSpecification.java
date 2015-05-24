package com.echarm.qasystem.answer.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.echarm.qasystem.answer.error.NoContentException;
import com.echarm.qasystem.answer.error.ResourceNotExistException;
import com.echarm.qasystem.answer.error.ServerSideProblemException;
import com.echarm.qasystem.answer.model.QuestionWithAnswers;
import com.echarm.qasystem.answer.model.Answer;

public class DeleteAllAnswerSpecification extends AnswerSpecification{

	public DeleteAllAnswerSpecification(Answer answer) {
		super(answer);
	}

	@Override
	List<Answer> doActions(MongoTemplate mongoTemplate) throws ServerSideProblemException, ResourceNotExistException, NoContentException {
		QuestionWithAnswers resultQWA = null;
		List<Answer> resultArr = null;
		AnswerSpecification cSpec = AnswerSpecificationFactory.getFindAllAnswerSpecification(answer);
		resultArr = cSpec.doActions(mongoTemplate);
		if(resultArr == null)
			throw new NoContentException();
		String colName = answer.getCategory().toString();
    	String questionId = answer.getQuestionId();
    	
    	Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("_id").is(questionId));
		resultQWA = mongoTemplate.findOne(findQuery, QuestionWithAnswers.class, colName);
		
		Query query = new Query(Criteria.where("_id").is(questionId));
    	Update update = new Update();
    	for(int index = 0; index < resultQWA.getAnswers().size(); index++)
    		update.set("answers."+ index +".isDeleted", true);
        mongoTemplate.findAndModify(query, update, QuestionWithAnswers.class, colName);
		
		return resultArr;
	}

}
