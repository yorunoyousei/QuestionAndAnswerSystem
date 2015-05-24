package com.echarm.qasystem.rating.repository;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.echarm.qasystem.rating.error.NoContentException;
import com.echarm.qasystem.rating.error.ResourceNotExistException;
import com.echarm.qasystem.rating.error.ServerSideProblemException;
import com.echarm.qasystem.rating.model.Rating;

public class FindRatingByFilterSpecification extends RatingSpecification {

	public FindRatingByFilterSpecification(Rating rating) {
		super(rating);
	}

	@Override
	List<Rating> doActions(MongoTemplate mongoTemplate)
			throws ResourceNotExistException, NoContentException,
			ServerSideProblemException {
		// TODO Auto-generated method stub
		return null;
	}

}
