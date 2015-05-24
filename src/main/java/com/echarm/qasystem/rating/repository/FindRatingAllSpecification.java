package com.echarm.qasystem.rating.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.echarm.qasystem.rating.error.QuestionNotExistErrorBody;
import com.echarm.qasystem.rating.error.NoContentException;
import com.echarm.qasystem.rating.error.ResourceNotExistException;
import com.echarm.qasystem.rating.error.ServerSideProblemException;
import com.echarm.qasystem.rating.model.QuestionWithRatings;
import com.echarm.qasystem.rating.model.Rating;

/**
 * @author yorunosora
 * @since 2015-03-28
 */
public class FindRatingAllSpecification extends RatingSpecification {

	public FindRatingAllSpecification(Rating rating) {
		super(rating);
	}

	@Override
	List<Rating> doActions(MongoTemplate mongoTemplate)
			throws ResourceNotExistException, NoContentException,
			ServerSideProblemException {
		QuestionWithRatings resultAwr = null;
		ArrayList<Rating> resultArray = null;
		
		String questionId = rating.getQuestionId();
		String category = rating.getCategoryStr();
		if(questionId == null || questionId.isEmpty() || category == null || category.isEmpty()){
			throw new ServerSideProblemException("Input rating model is not complete!!! Missing field: `question_id` or `category`");
		}
		
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("questionId").is(questionId));
		findQuery.addCriteria(Criteria.where("isDeleted").is(false));
		resultAwr = mongoTemplate.findOne(findQuery, QuestionWithRatings.class, category);
		
		if(resultAwr == null) {
			ResourceNotExistException e = new ResourceNotExistException("Requested question with id = \"" + questionId + "\" doesnot exists");
			e.setErrorBody(new QuestionNotExistErrorBody(questionId));
			throw e;
		}
		else {
			resultArray = new ArrayList<Rating>();
			for(Rating tempRating : resultAwr.getRatings()){
				if(!tempRating.getIsDeleted())
					resultArray.add(tempRating);
			}
			if(resultArray.size() == 0)
				throw new NoContentException();
			else
				return resultArray;
		}
	}

}
