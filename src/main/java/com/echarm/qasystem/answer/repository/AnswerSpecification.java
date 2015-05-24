package com.echarm.qasystem.answer.repository;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.echarm.qasystem.answer.error.NoContentException;
import com.echarm.qasystem.answer.error.ResourceNotExistException;
import com.echarm.qasystem.answer.error.ServerSideProblemException;
import com.echarm.qasystem.answer.model.Answer;

public abstract class AnswerSpecification {
    protected Answer answer;

    public AnswerSpecification(Answer comment){
        this.answer = comment;
    }

    abstract List<Answer> doActions(MongoTemplate mongoTemplate) throws ResourceNotExistException, NoContentException, ServerSideProblemException;
}
