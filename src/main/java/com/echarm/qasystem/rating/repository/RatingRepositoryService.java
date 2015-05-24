package com.echarm.qasystem.rating.repository;

import java.util.List;

import com.echarm.qasystem.rating.error.NoContentException;
import com.echarm.qasystem.rating.error.ResourceNotExistException;
import com.echarm.qasystem.rating.error.ServerSideProblemException;
import com.echarm.qasystem.rating.model.Rating;

/*
 * Interface for RatingRepository, which is a wrapper class over data accessing objects.
 * Methods in a repository is restricted to create/update/delete and query.
 * Arguments and return type of these methods are all Rating objects
 */
public interface RatingRepositoryService {
	
	/*
	 * Add a rating to the database.
	 *
	 * @param  rating  the Rating object that is going to be added into the database
	 * @return result   the Rating object that is added into the database
	 */
	/*
	 * Precondition(s):   the input Rating object must be not null in all fields, otherwise throws InputIncompleteException
	 * Postcondition(s): the return Rating object must be not null in all fields
	 */
	public Rating createRating(Rating rating) throws ServerSideProblemException;
	
	/*
	 * Update an rating to the database.
	 *
	 * @param  rating                    the Rating object that is going to be updated into the database
	 * @return result                     the Rating object that is updated into the database
	 * @throw  ResourceNotExistException  {rating} is not existed in the database
	 * @throw  NoContentException         {rating} is the same as the one in the database, nothing is updated
	 */
	 /*
	 * Precondition(s):   the input Rating object must be not null in `ratingId` and `category`, otherwise throws InputIncompleteException
	 * Postcondition(s): the return Rating object must be not null in all fields
	 */
	public Rating updateRating(Rating rating) throws ResourceNotExistException, NoContentException;
	
	/*
	 * Delete an rating to the database.
	 *
	 * @param  rating                    the Rating object that is going to be deleted from the database
	 * @return result                     the Rating object that is deleted
	 * @throw  ResourceNotExistException  {rating} is not existed in the database
	 */
	 /*
	 * Precondition(s):   the input Rating object must be not null in `ratingId` and `category`, otherwise throws InputIncompleteException
	 * Postcondition(s): the return Rating object must be not null in all fields
	 */
	public Rating deleteRating(Rating rating) throws ResourceNotExistException;
	public List<Rating> query(RatingSpecification specification) throws ResourceNotExistException, NoContentException;
}
