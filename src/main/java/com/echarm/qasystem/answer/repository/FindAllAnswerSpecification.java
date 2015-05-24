package com.echarm.qasystem.answer.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.echarm.qasystem.answer.error.QuestionNotExistErrorBody;
import com.echarm.qasystem.answer.error.NoContentException;
import com.echarm.qasystem.answer.error.ResourceNotExistException;
import com.echarm.qasystem.answer.error.ServerSideProblemException;
import com.echarm.qasystem.answer.model.QuestionWithAnswers;
import com.echarm.qasystem.answer.model.Answer;

public class FindAllAnswerSpecification extends AnswerSpecification{

	public FindAllAnswerSpecification(Answer answer) {
		super(answer);
	}

	@Override
	List<Answer> doActions(MongoTemplate mongoTemplate) throws ResourceNotExistException, NoContentException {
		QuestionWithAnswers resultQWA = null;
		ArrayList<Answer> resultArr = null;
		
		String questionId = answer.getQuestionId();
		String category = answer.getCategoryStr();
		if(questionId == null || questionId.equals("") || category == null || category.equals("")) {
			throw new ServerSideProblemException("Input comment model is not complete!!! Missing field: `question_id` or `category`");
		}
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("questionId").is(questionId));
		findQuery.addCriteria(Criteria.where("isDeleted").is(false));
		resultQWA = mongoTemplate.findOne(findQuery, QuestionWithAnswers.class, category);
		if(resultQWA == null) {
			ResourceNotExistException e = new ResourceNotExistException("Requested question with id = \"" + questionId + "\" doesnot exists");
			e.setErrorBody(new QuestionNotExistErrorBody(questionId));
			throw e;
		}
		else {
			Answer tempAnswer = null;
			
			resultArr = new ArrayList<Answer>();
			for(int index = 0; index < resultQWA.getAnswers().size(); index++) {
				tempAnswer = resultQWA.getAnswers().get(index);
				if(!tempAnswer.getIsDeleted())
					resultArr.add(tempAnswer);
			}
			if(resultArr.size() == 0)
				throw new NoContentException();
			else
				return resultArr;
		}
	}

}
