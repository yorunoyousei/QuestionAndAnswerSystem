package com.echarm.qasystem.question.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.echarm.qasystem.question.error.QuestionNotExistErrorBody;
import com.echarm.qasystem.question.error.NoContentException;
import com.echarm.qasystem.question.error.ResourceNotExistException;
import com.echarm.qasystem.question.error.ServerSideProblemException;
import com.echarm.qasystem.question.model.Question;

public class FindQuestionByIdSpecification extends QuestionSpecification {

	public FindQuestionByIdSpecification(Question question) {
		super(question);
	}

	@Override
	List<Question> doActions(MongoTemplate mongoTemplate) throws ResourceNotExistException{
		List<Question> resultArr = new ArrayList<Question>();
		
		String questionId = question.getQuestionId();
		String category = question.getCategoryStr();
		if(questionId == null || questionId.equals("") || category == null || category.equals("")) {
			throw new ServerSideProblemException("Input question model is not complete!!! Missing field: `question_id` or `category`");
		}
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("questionId").is(questionId));
		findQuery.addCriteria(Criteria.where("deleted").is(false));
		Question ar = mongoTemplate.findOne(findQuery, Question.class, category);
		if(ar == null) {
			ResourceNotExistException e = new ResourceNotExistException("Requested question with id = \"" + questionId + "\" doesnot exists");
			e.setErrorBody(new QuestionNotExistErrorBody(questionId));
			throw e;
		}
		else
			resultArr.add(ar);
		
		return resultArr;
	}

}
