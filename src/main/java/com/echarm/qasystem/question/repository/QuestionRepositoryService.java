package com.echarm.qasystem.question.repository;

import java.util.List;

import com.echarm.qasystem.question.error.NoContentException;
import com.echarm.qasystem.question.error.ResourceNotExistException;
import com.echarm.qasystem.question.error.ServerSideProblemException;
import com.echarm.qasystem.question.model.Question;
/*
 * Interface for ArticleRepository, which is a wrapper class over data accessing objects.
 * Methods in a repository is restricted to create/update/delete and query.
 * Arguments and return type of these methods are all Article objects
 */
public interface QuestionRepositoryService {

	/*
	 * Add an article to the database.
	 *
	 * @param  article  the Article object that is going to be added into the database
	 * @return result   the Article object that is added into the database
	 */
	/*
	 * Precondition(s):   the input Article object must be not null in all fields, otherwise throws InputIncompleteException
	 * Postcondition(s): the return Article object must be not null in all fields
	 */
	public Question createQuestion(Question question) throws ServerSideProblemException;

	/*
	 * Update an article to the database.
	 *
	 * @param  article                    the Article object that is going to be updated into the database
	 * @return result                     the Article object that is updated into the database
	 * @throw  ResourceNotExistException  {article} is not existed in the database
	 * @throw  NoContentException         {article} is the same as the one in the database, nothing is updated
	 */
	 /*
	 * Precondition(s):   the input Article object must be not null in `articleId` and `category`, otherwise throws InputIncompleteException
	 * Postcondition(s): the return Article object must be not null in all fields
	 */
	public Question updateQuestion(Question question) throws ResourceNotExistException, NoContentException;

	/*
	 * Delete an article to the database.
	 *
	 * @param  article                    the Article object that is going to be deleted from the database
	 * @return result                     the Article object that is deleted
	 * @throw  ResourceNotExistException  {article} is not existed in the database
	 */
	 /*
	 * Precondition(s):   the input Article object must be not null in `articleId` and `category`, otherwise throws InputIncompleteException
	 * Postcondition(s): the return Article object must be not null in all fields
	 */
	public Question deleteQuestion(Question article) throws ResourceNotExistException;
	public List<Question> query(QuestionSpecification specification)throws ResourceNotExistException, NoContentException;
}
