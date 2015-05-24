package com.echarm.qasystem.answer.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.echarm.qasystem.answer.error.CategoryNotExistErrorBody;
import com.echarm.qasystem.answer.error.AnswerNotExistErrorBody;
import com.echarm.qasystem.answer.error.ResourceNotExistException;
import com.echarm.qasystem.answer.error.ServerSideProblemException;
import com.echarm.qasystem.answer.model.QuestionWithAnswers;
import com.echarm.qasystem.answer.model.Answer;

public class FindAnswerByIdSpecification extends AnswerSpecification{

	public FindAnswerByIdSpecification(Answer answer) {
		super(answer);
	}

	@Override
	List<Answer> doActions(MongoTemplate mongoTemplate) throws ResourceNotExistException {
		QuestionWithAnswers qwa = null;
    	Answer ans = null;
    	
    	String colName = answer.getCategory().toString();
    	String questionId = answer.getQuestionId();
//    	String answerId = answer.getAnswerId();
    	if(questionId == null || questionId.equals("") || colName == null || colName.equals("") ) {
			throw new ServerSideProblemException("Input question model is not complete!!! Missing field: `question_id`, or `category`");
		}
    	if(!mongoTemplate.collectionExists(colName)) {
    		ResourceNotExistException e = new ResourceNotExistException("Requested question with id = \"" + questionId + "\" doesnot exists");
			e.setErrorBody(new CategoryNotExistErrorBody(questionId));
			throw e;
    	}
    	
    	Query query = new Query(Criteria.where("_id").is(questionId));
    	query.addCriteria(Criteria.where("isDeleted").is(false));
        qwa = mongoTemplate.findOne(query, QuestionWithAnswers.class, colName);
    	if(qwa == null) {
    		ResourceNotExistException e = new ResourceNotExistException("Requested answer with id = \"" + questionId + "\" doesnot exists");
			e.setErrorBody(new AnswerNotExistErrorBody(questionId));
			throw e;
    	}
    	for(int index = 0; index < qwa.getAnswers().size(); index++) {
    		ans = qwa.getAnswers().get(index);
    		if(ans.getQuestionId().equals(questionId)) {
    			if(ans.getIsDeleted()) {
    				ResourceNotExistException e = new ResourceNotExistException("Requested answer with id = \"" + questionId + "\" doesnot exists");
    				e.setErrorBody(new AnswerNotExistErrorBody(questionId));
    				throw e;
    			}
    			ArrayList<Answer> answerArr = new ArrayList<Answer>();
    			answerArr.add(ans);
    			return answerArr;
    		}
    	}
        return null;
	}

}
