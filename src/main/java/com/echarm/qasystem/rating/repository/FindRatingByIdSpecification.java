package com.echarm.qasystem.rating.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.echarm.qasystem.rating.error.CategoryNotExistErrorBody;
import com.echarm.qasystem.rating.error.NoContentException;
import com.echarm.qasystem.rating.error.RatingNotExistErrorBody;
import com.echarm.qasystem.rating.error.ResourceNotExistException;
import com.echarm.qasystem.rating.error.ServerSideProblemException;
import com.echarm.qasystem.rating.model.QuestionWithRatings;
import com.echarm.qasystem.rating.model.Rating;

/**
 * @author yorunosora
 * @since 2015-03-28
 */
public class FindRatingByIdSpecification extends RatingSpecification {

	public FindRatingByIdSpecification(Rating rating) {
		super(rating);
	}

	
	@Override
	List<Rating> doActions(MongoTemplate mongoTemplate)
			throws ResourceNotExistException, NoContentException,
			ServerSideProblemException {
		
		QuestionWithRatings resultAwr = null;
		
		String questionId = rating.getQuestionId();
		String category = rating.getCategoryStr();
		String ratingId = rating.getRatingId();
		if(questionId == null || questionId.isEmpty() || category == null || category.isEmpty() || ratingId == null || ratingId.isEmpty()){
			throw new ServerSideProblemException("Input rating model is not complete!!! Missing field: `question_id` or `category`");
		}
		if(!mongoTemplate.collectionExists(category)){
			ResourceNotExistException e = new ResourceNotExistException("Requested question with id = \"" + questionId + "\" doesnot exists");
			e.setErrorBody(new CategoryNotExistErrorBody(questionId));
			throw e;
		}
		
		Query query = new Query(Criteria.where("_id").is(questionId));
    	query.addCriteria(Criteria.where("ratings.ratingId").is(ratingId));
    	resultAwr = mongoTemplate.findOne(query, QuestionWithRatings.class, category);
    	if(resultAwr == null) {
    		ResourceNotExistException e = new ResourceNotExistException("Requested rating with id = \"" + ratingId + "\" doesnot exists");
			e.setErrorBody(new RatingNotExistErrorBody(ratingId));
			throw e;
    	}
    	for(Rating result : resultAwr.getRatings()){
    		if(result.getRatingId().equals(ratingId)) {
    			if(result.getIsDeleted()) {
    				ResourceNotExistException e = new ResourceNotExistException("Requested rating with id = \"" + ratingId + "\" doesnot exists");
    				e.setErrorBody(new RatingNotExistErrorBody(ratingId));
    				throw e;
    			}
    			ArrayList<Rating> ratingArr = new ArrayList<Rating>();
    			ratingArr.add(result);
    			return ratingArr;
    		}
    	}
        return null;
	}

}
