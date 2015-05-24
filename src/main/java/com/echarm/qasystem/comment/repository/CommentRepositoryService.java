package com.echarm.qasystem.comment.repository;

import java.util.List;

import com.echarm.qasystem.comment.error.NoContentException;
import com.echarm.qasystem.comment.error.ResourceNotExistException;
import com.echarm.qasystem.comment.error.ServerSideProblemException;
import com.echarm.qasystem.comment.model.Comment;

/*
 * Interface for CommentRepository, which is a wrapper class over data accessing objects.
 * Methods in a repository is restricted to create/update/delete and query.
 * Arguments and return type of these methods are all Comment objects
 */
public interface CommentRepositoryService {
    /*
     * Add an comment to the database.
     *
     * @param  comment  the Comment object that is going to be added into the database
     * @return result   the Comment object that is added into the database
     */
    /*
     * Precondition(s):   the input Comment object must be not null in all fields, otherwise throws ServerSideProblemException
     * Postcondition(s): the return Comment object must be not null in all fields
     */
    public Comment createComment(Comment comment);

    /*
     * Update an comment to the database.
     *
     * @param  comment                    the Comment object that is going to be updated into the database
     * @return result                     the Comment object that is updated into the database
     * @throw  ResourceNotExistException  {comment} is not existed in the database
     * @throw  NoContentException         {comment} is the same as the one in the database, nothing is updated
     */
     /*
     * Precondition(s):   the input Comment object must be not null in `questionId`, `commentId` and `category`, otherwise throws ServerSideProblemException
     * Postcondition(s): the return Comment object must be not null in all fields
     */
    public Comment updateComment(Comment comment) throws ResourceNotExistException, NoContentException;

    /*
     * Delete an comment to the database.
     *
     * @param  comment                    the Comment object that is going to be deleted from the database
     * @return result                     the Comment object that is deleted
     * @throw  ResourceNotExistException  {comment} is not existed in the database
     */
     /*
     * Precondition(s):   the input Comment object must be not null in `questionId`, `commentId` and `category`, otherwise throws ServerSideProblemException
     * Postcondition(s): the return Comment object must be not null in all fields
     */
    public Comment deleteComment(Comment question) throws ResourceNotExistException, NoContentException;
    public List<Comment> query(CommentSpecification specification)throws ResourceNotExistException, NoContentException, ServerSideProblemException, Exception;
}
