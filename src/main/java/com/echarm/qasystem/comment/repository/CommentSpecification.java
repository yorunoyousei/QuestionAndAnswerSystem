package com.echarm.qasystem.comment.repository;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.echarm.qasystem.comment.error.NoContentException;
import com.echarm.qasystem.comment.error.ResourceNotExistException;
import com.echarm.qasystem.comment.error.ServerSideProblemException;
import com.echarm.qasystem.comment.model.Comment;

public abstract class CommentSpecification {
    protected Comment comment;

    public CommentSpecification(Comment comment){
        this.comment = comment;
    }

    abstract List<Comment> doActions(MongoTemplate mongoTemplate) throws ResourceNotExistException, NoContentException, ServerSideProblemException;
}
