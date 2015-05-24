package com.echarm.qasystem.rating.repository;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.echarm.qasystem.rating.error.NoContentException;
import com.echarm.qasystem.rating.error.ResourceNotExistException;
import com.echarm.qasystem.rating.error.ServerSideProblemException;
import com.echarm.qasystem.rating.model.QuestionWithRatings;
import com.echarm.qasystem.rating.model.Rating;

/**
 * @author yorunosora
 * @since 2015-03-28
 */
public class DeleteRatingAllSpecification extends RatingSpecification {

	public DeleteRatingAllSpecification(Rating rating) {
		super(rating);
	}

	@Override
	List<Rating> doActions(MongoTemplate mongoTemplate)
			throws ResourceNotExistException, NoContentException,
			ServerSideProblemException {
		
		List<Rating> resultArray = null;
		RatingSpecification ratingSpec = RatingSpecificationFactory.getFindRatingAllSpecification(rating);
		resultArray = ratingSpec.doActions(mongoTemplate);
		if(resultArray == null)
			throw new NoContentException();
		
		String category = rating.getCategoryStr();
		String questionId = rating.getQuestionId();
		
		//Query query = new Query(Criteria.where("_id").is(questionId));
		//Update update = new Update();
		
		/*
		for(int i = 0; i < resultArray.size(); ++i){
			update.set("ratings." + i + ".isDeleted", true);
		}
		mongoTemplate.findAndModify(query, update, QuestionWithRatings.class, category);
		*/
		
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("_id").is(questionId));
		QuestionWithRatings resultAWR = mongoTemplate.findOne(findQuery, QuestionWithRatings.class, category);
		
		Query query = new Query(Criteria.where("_id").is(questionId));
    	Update update = new Update();
    	for(int index = 0; index < resultAWR.getRatings().size(); index++)
    		update.set("ratings."+ index +".isDeleted", true);
        mongoTemplate.findAndModify(query, update, QuestionWithRatings.class, category);
		
		return resultArray;
	}

}
