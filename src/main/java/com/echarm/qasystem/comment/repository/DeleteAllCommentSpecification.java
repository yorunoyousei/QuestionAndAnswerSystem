package com.echarm.qasystem.comment.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.echarm.qasystem.comment.error.NoContentException;
import com.echarm.qasystem.comment.error.ResourceNotExistException;
import com.echarm.qasystem.comment.error.ServerSideProblemException;
import com.echarm.qasystem.comment.model.QuestionWithComments;
import com.echarm.qasystem.comment.model.Comment;

public class DeleteAllCommentSpecification extends CommentSpecification{

	public DeleteAllCommentSpecification(Comment comment) {
		super(comment);
		// TODO Auto-generated constructor stub
	}

	@Override
	List<Comment> doActions(MongoTemplate mongoTemplate) throws ServerSideProblemException, ResourceNotExistException, NoContentException {
		// TODO Auto-generated method stub
		QuestionWithComments resultAWC = null;
		List<Comment> resultArr = null;
		CommentSpecification cSpec = CommentSpecificationFactory.getFindAllCommentSpecification(comment);
		resultArr = cSpec.doActions(mongoTemplate);
		if(resultArr == null)
			throw new NoContentException();
		String colName = comment.getCategory().toString();
    	String questionId = comment.getQuestionId();
    	
    	Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("_id").is(questionId));
		resultAWC = mongoTemplate.findOne(findQuery, QuestionWithComments.class, colName);
		
		Query query = new Query(Criteria.where("_id").is(questionId));
    	Update update = new Update();
    	for(int index = 0; index < resultAWC.getComments().size(); index++)
    		update.set("comments."+ index +".isDeleted", true);
        mongoTemplate.findAndModify(query, update, QuestionWithComments.class, colName);
		
		return resultArr;
	}

}
