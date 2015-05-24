package com.echarm.qasystem.answer.repository;

import java.util.List;

import com.echarm.qasystem.answer.error.NoContentException;
import com.echarm.qasystem.answer.error.ResourceNotExistException;
import com.echarm.qasystem.answer.error.ServerSideProblemException;
import com.echarm.qasystem.answer.model.Answer;

/*
 * Interface for AnswerRepository, which is a wrapper class over data accessing objects.
 * Methods in a repository is restricted to create/update/delete and query.
 * Arguments and return type of these methods are all Answer objects
 */
public interface AnswerRepositoryService {
    /*
     * Add an answer to the database.
     *
     * @param  answer  the Answer object that is going to be added into the database
     * @return result   the Answer object that is added into the database
     */
    /*
     * Precondition(s):   the input Answer object must be not null in all fields, otherwise throws ServerSideProblemException
     * Postcondition(s): the return Answer object must be not null in all fields
     */
    public Answer createAnswer(Answer answer);

    /*
     * Update an answer to the database.
     *
     * @param  answer                    the Answer object that is going to be updated into the database
     * @return result                     the Answer object that is updated into the database
     * @throw  ResourceNotExistException  {answer} is not existed in the database
     * @throw  NoContentException         {answer} is the same as the one in the database, nothing is updated
     */
     /*
     * Precondition(s):   the input Answer object must be not null in `questionId`, `answerId` and `category`, otherwise throws ServerSideProblemException
     * Postcondition(s): the return Answer object must be not null in all fields
     */
    public Answer updateAnswer(Answer answer) throws ResourceNotExistException, NoContentException;

    /*
     * Delete an answer to the database.
     *
     * @param  answer                    the Answer object that is going to be deleted from the database
     * @return result                     the Answer object that is deleted
     * @throw  ResourceNotExistException  {answer} is not existed in the database
     */
     /*
     * Precondition(s):   the input Answer object must be not null in `questionId`, `answerId` and `category`, otherwise throws ServerSideProblemException
     * Postcondition(s): the return Answer object must be not null in all fields
     */
    public Answer deleteAnswer(Answer answer) throws ResourceNotExistException, NoContentException;
    public List<Answer> query(AnswerSpecification specification)throws ResourceNotExistException, NoContentException, ServerSideProblemException, Exception;
}
