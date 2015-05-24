package com.echarm.qasystem.rating.repository;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.echarm.qasystem.rating.error.NoContentException;
import com.echarm.qasystem.rating.error.ResourceNotExistException;
import com.echarm.qasystem.rating.error.ServerSideProblemException;
import com.echarm.qasystem.rating.model.Rating;

/**
 * @author yorunosora 2015-03-26
 */
public abstract class RatingSpecification {

	protected Rating rating;

	public RatingSpecification(Rating rating) {
		this.rating = rating;
	}

	abstract List<Rating> doActions(MongoTemplate mongoTemplate) throws ResourceNotExistException, NoContentException, ServerSideProblemException;
}
