package com.echarm.qasystem.comment.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.echarm.qasystem.comment.error.CategoryNotExistErrorBody;
import com.echarm.qasystem.comment.error.CommentNotExistErrorBody;
import com.echarm.qasystem.comment.error.ResourceNotExistException;
import com.echarm.qasystem.comment.error.ServerSideProblemException;
import com.echarm.qasystem.comment.model.QuestionWithComments;
import com.echarm.qasystem.comment.model.Comment;

public class FindCommentByIdSpecification extends CommentSpecification{

	public FindCommentByIdSpecification(Comment comment) {
		super(comment);
		// TODO Auto-generated constructor stub
	}

	@Override
	List<Comment> doActions(MongoTemplate mongoTemplate) throws ResourceNotExistException {
		QuestionWithComments qwc = null;
    	Comment cmt = null;
    	
    	String colName = comment.getCategory().toString();
    	String questionId = comment.getQuestionId();
    	String commentId = comment.getCommentId();
    	if(questionId == null || questionId.equals("") || colName == null || colName.equals("") || commentId == null || commentId.equals("")) {
			throw new ServerSideProblemException("Input question model is not complete!!! Missing field: `question_id`, `comment_id` or `category`");
		}
    	if(!mongoTemplate.collectionExists(colName)) {
    		ResourceNotExistException e = new ResourceNotExistException("Requested question with id = \"" + questionId + "\" doesnot exists");
			e.setErrorBody(new CategoryNotExistErrorBody(questionId));
			throw e;
    	}
    	
    	Query query = new Query(Criteria.where("_id").is(questionId));
    	query.addCriteria(Criteria.where("isDeleted").is(false));
    	//query.addCriteria(Criteria.where("comments.isDeleted").is(false));
    	//Update update = new Update();
        //update.set("comments.$.isDeleted", true);
        qwc = mongoTemplate.findOne(query, QuestionWithComments.class, colName);
    	if(qwc == null) {
    		ResourceNotExistException e = new ResourceNotExistException("Requested comment with id = \"" + commentId + "\" doesnot exists");
			e.setErrorBody(new CommentNotExistErrorBody(commentId));
			throw e;
    	}
    	for(int index = 0; index < qwc.getComments().size(); index++) {
    		cmt = qwc.getComments().get(index);
    		if(cmt.getCommentId().equals(commentId)) {
    			if(cmt.getIsDeleted()) {
    				ResourceNotExistException e = new ResourceNotExistException("Requested comment with id = \"" + commentId + "\" doesnot exists");
    				e.setErrorBody(new CommentNotExistErrorBody(commentId));
    				throw e;
    			}
    			ArrayList<Comment> cmtArr = new ArrayList<Comment>();
    			cmtArr.add(cmt);
    			return cmtArr;
    		}
    	}
        return null;
	}

}
