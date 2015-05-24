package com.echarm.qasystem.rating.repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.echarm.qasystem.rating.error.CategoryNotExistErrorBody;
import com.echarm.qasystem.rating.error.NoContentException;
import com.echarm.qasystem.rating.error.RatingNotExistErrorBody;
import com.echarm.qasystem.rating.error.ResourceNotExistException;
import com.echarm.qasystem.rating.error.ServerSideProblemException;
import com.echarm.qasystem.rating.model.QuestionWithRatings;
import com.echarm.qasystem.rating.model.Rating;
import com.echarm.qasystem.rating.util.DatabaseParameters;
import com.echarm.qasystem.rating.util.StringFormat;
import com.echarm.qasystem.rating.util.Time;
import com.mongodb.MongoClient;

@Repository
public class RatingRepository implements RatingRepositoryService {

	private MongoTemplate mongoTemplate = null;

	public RatingRepository(){
		try {
			connectToDB();
		} catch (Exception e) {
			throw new ServerSideProblemException("Cannot connect to the rating database!!!");
		}
	}

	@Override
    public Rating createRating(Rating rating) throws ServerSideProblemException {
		String[] missingArr = rating.getNullJsonInputFieldName();
		// 1.
    	if(missingArr != null) {
    		throw new ServerSideProblemException("Input question model is not complete!!! Missing field: " + Arrays.toString(missingArr));
    	}

    	// 2.
    	String colName = rating.getCategory().toString();
    	String questionId = rating.getQuestionId().toString();
		if(!mongoTemplate.collectionExists(colName))
			mongoTemplate.createCollection(colName);

		rating.setRatingId(Time.getCurrentTimeMillisStr());
		rating.setIsDeleted(false);

		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("questionId").is(questionId));
		if(mongoTemplate.exists(findQuery, QuestionWithRatings.class, colName)) {
			Update update = new Update();
			update.push("ratings", rating);
			mongoTemplate.updateFirst(findQuery, update, QuestionWithRatings.class, colName);
		}
		else {
			QuestionWithRatings awc = new QuestionWithRatings(questionId);
			ArrayList<Rating> ratingArr = new ArrayList<Rating>();
			ratingArr.add(rating);
			awc.setRatings(ratingArr);
			mongoTemplate.save(awc, colName);
		}
        return rating;
    }

    @Override
    public Rating updateRating(Rating rating)
            throws ResourceNotExistException, NoContentException {
    	// yoru 2015-03-24
        QuestionWithRatings awr = null;
    	Rating rate = null;

    	String colName = rating.getCategory().toString();
    	String questionId = rating.getQuestionId();
    	String ratingId = rating.getRatingId();
    	if(questionId == null || questionId.equals("") || colName == null || colName.equals("") || ratingId == null || ratingId.equals("")) {
			throw new ServerSideProblemException("Input question model is not complete!!! Missing field: `question_id`, `rating_id` or `category`");
		}
    	if(!mongoTemplate.collectionExists(colName)) {
    		ResourceNotExistException e = new ResourceNotExistException("Requested question with id = \"" + questionId + "\" doesnot exists");
			e.setErrorBody(new CategoryNotExistErrorBody(questionId));
			throw e;
    	}

    	String[] fieldNameArr = rating.getNonNullFieldName();
		Update updateParam = new Update();
		Method method;
		//String fieldName;
		if(fieldNameArr == null) {
			throw new NoContentException();
		}
		for(String fieldName : fieldNameArr){
			try {
				method = rating.getClass().getMethod("get"+ StringFormat.snake2CamelCapital(fieldName));
				updateParam.set("ratings.$." + StringFormat.snake2Camel(fieldName), method.invoke(rating));
				//System.out.println(fieldName + ", " + StringFormat.snake2CamelCapital(fieldName) + ", " + StringFormat.snake2Camel(fieldName));
			} catch (NoSuchMethodException e) {
				throw new ServerSideProblemException("No such attribute to update!!! (" + fieldName + ")");
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		Query query = new Query(Criteria.where("_id").is(questionId));
    	query.addCriteria(Criteria.where("ratings.ratingId").is(ratingId));
    	awr = mongoTemplate.findAndModify(query, updateParam, new FindAndModifyOptions().returnNew(true), QuestionWithRatings.class, colName);
    	if(awr == null) {
    		ResourceNotExistException e = new ResourceNotExistException("Requested rating with id = \"" + ratingId + "\" doesnot exists");
			e.setErrorBody(new CategoryNotExistErrorBody(ratingId));
			throw e;
    	}
    	for(int index = 0; index < awr.getRatings().size(); index++) {
    		rate = awr.getRatings().get(index);
    		if(rate.getRatingId().equals(ratingId))
    			return rate;
    	}

        return null;
    }

    @Override
    public Rating deleteRating(Rating rating)
            throws ResourceNotExistException {
    	// yoru 2015-03-26
    	QuestionWithRatings awr = null;

    	String colName = rating.getCategory().toString();
    	String questionId = rating.getQuestionId();
    	String ratingId = rating.getRatingId();

    	if(questionId == null || questionId.equals("") || colName == null || colName.equals("") || ratingId == null || ratingId.equals("")) {
			throw new ServerSideProblemException("Input question model is not complete!!! Missing field: `question_id`, `rating_id` or `category`");
		}
    	if(!mongoTemplate.collectionExists(colName)) {
    		ResourceNotExistException e = new ResourceNotExistException("Requested question with id = \"" + questionId + "\" doesnot exists");
			e.setErrorBody(new CategoryNotExistErrorBody(questionId));
			throw e;
    	}

    	Query query = new Query(Criteria.where("_id").is(questionId));
    	query.addCriteria(Criteria.where("ratings.ratingId").is(ratingId));
    	Update update = new Update();
        update.set("ratings.$.isDeleted", true);
        awr = mongoTemplate.findAndModify(query, update, QuestionWithRatings.class, colName);

        if(awr == null) {
    		ResourceNotExistException e = new ResourceNotExistException("Requested rating with id = \"" + ratingId + "\" doesnot exists");
			e.setErrorBody(new RatingNotExistErrorBody(ratingId));
			throw e;
    	}

    	// yoru-modified for-loop
    	for(Rating iterRating : awr.getRatings()){
    		if(iterRating.getRatingId().equals(ratingId))
    			return iterRating;
    	}

    	return null;
    }

    @Override
    public List<Rating> query(RatingSpecification specification)
            throws ResourceNotExistException, NoContentException {
    	// yoru 2015-03-26
    	return specification.doActions(mongoTemplate);
    }

    private void connectToDB() throws Exception {
		MongoClient mongoClient = new MongoClient();
		mongoTemplate = new MongoTemplate(new SimpleMongoDbFactory(mongoClient, DatabaseParameters.DB_NAME_DEFAULT));
	}
}
