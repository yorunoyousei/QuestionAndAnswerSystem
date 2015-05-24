package com.echarm.qasystem.comment.repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.echarm.qasystem.comment.error.QuestionNotExistErrorBody;
import com.echarm.qasystem.comment.error.CategoryNotExistErrorBody;
import com.echarm.qasystem.comment.error.CommentNotExistErrorBody;
import com.echarm.qasystem.comment.error.NoContentException;
import com.echarm.qasystem.comment.error.ResourceNotExistException;
import com.echarm.qasystem.comment.error.ServerSideProblemException;
import com.echarm.qasystem.comment.model.QuestionWithComments;
import com.echarm.qasystem.comment.model.Comment;
import com.echarm.qasystem.comment.util.DatabaseParameters;
import com.echarm.qasystem.comment.util.StringFormat;
import com.echarm.qasystem.comment.util.Time;
import com.mongodb.MongoClient;

@Repository
public class CommentRepository implements CommentRepositoryService {
	
	private MongoTemplate mongoTemplate = null;
	
	public CommentRepository() {
		try {
			connectToDB();
		} catch (Exception e) {
			throw new ServerSideProblemException("Cannot connect to the comment database!!!");
		}
	}

    @Override
    public Comment createComment(Comment comment) {
        // TODO Auto-generated method stub
    	/**
    	 * 1. Check if all necessary fields are filled. If not, throw an exception  
    	 * 2. Check if the category exists. If not, create one.
    	 * 3. Check if the question exists. 
    	 *   3.1 If yes, insert the comment in the question. 
    	 *   3.2 If not, create one with the comment built in.
    	 */
    	String[] missingArr = comment.getNullJsonInputFieldName();
    	
    	// 1.
    	if(missingArr != null) {
    		throw new ServerSideProblemException("Input question model is not complete!!! Missing field: " + Arrays.toString(missingArr));
    	}
    	
    	// 2.
    	String colName = comment.getCategory().toString();
    	String questionId = comment.getQuestionId().toString();
		if(!mongoTemplate.collectionExists(colName))
			mongoTemplate.createCollection(colName);
    	
    	// 3
		comment.setCommentId(Time.getCurrentTimeMillisStr());
		comment.setIsDeleted(false);
		
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("questionId").is(questionId));
		if(mongoTemplate.exists(findQuery, QuestionWithComments.class, colName)) {
			Update update = new Update();
			update.push("comments", comment);
			mongoTemplate.updateFirst(findQuery, update, QuestionWithComments.class, colName);
		}
		else {
			QuestionWithComments awc = new QuestionWithComments(questionId);
			ArrayList<Comment> cmtArr = new ArrayList<Comment>();
			cmtArr.add(comment);
			awc.setComments(cmtArr);
			mongoTemplate.save(awc, colName);
		}
		
        return comment;
    }

    @Override
    public Comment updateComment(Comment comment)
            throws ResourceNotExistException, NoContentException {
        // TODO Auto-generated method stub
    	QuestionWithComments awc = null;
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
    	String[] fieldNameArr = comment.getNonNullFieldName();
		Update updateParam = new Update();
		Method method;
		String fieldName;
		if(fieldNameArr == null) {
			throw new NoContentException();
		}
		for(int index = 0; index < fieldNameArr.length; index++) {
			fieldName = fieldNameArr[index];
			try {
				method = comment.getClass().getMethod("get"+ StringFormat.snake2CamelCapital(fieldName));
				updateParam.set("comments.$." + StringFormat.snake2Camel(fieldName), method.invoke(comment));
			} catch (NoSuchMethodException e) {
				throw new ServerSideProblemException("No such attribute to update!!! (" + fieldName + ")");
				//e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Query query = new Query(Criteria.where("_id").is(questionId));
    	query.addCriteria(Criteria.where("comments.commentId").is(commentId));
    	awc = mongoTemplate.findAndModify(query, updateParam, new FindAndModifyOptions().returnNew(true), QuestionWithComments.class, colName);
    	if(awc == null) {
    		ResourceNotExistException e = new ResourceNotExistException("Requested comment with id = \"" + commentId + "\" doesnot exists");
			e.setErrorBody(new CommentNotExistErrorBody(commentId));
			throw e;
    	}
    	for(int index = 0; index < awc.getComments().size(); index++) {
    		cmt = awc.getComments().get(index);
    		if(cmt.getCommentId().equals(commentId))
    			return cmt;
    	}
        return null;
    }

    @Override
    public Comment deleteComment(Comment comment)
            throws ResourceNotExistException{
        // TODO Auto-generated method stub
    	
    	QuestionWithComments awc = null;
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
    	query.addCriteria(Criteria.where("comments.commentId").is(commentId));
    	Update update = new Update();
        update.set("comments.$.isDeleted", true);
        awc = mongoTemplate.findAndModify(query, update, QuestionWithComments.class, colName);
    	if(awc == null) {
    		ResourceNotExistException e = new ResourceNotExistException("Requested comment with id = \"" + commentId + "\" doesnot exists");
			e.setErrorBody(new CommentNotExistErrorBody(commentId));
			throw e;
    	}
    	for(int index = 0; index < awc.getComments().size(); index++) {
    		cmt = awc.getComments().get(index);
    		if(cmt.getCommentId().equals(commentId))
    			return cmt;
    	}
        return null;
        
    }

    @Override
    public List<Comment> query(CommentSpecification specification)
            throws ResourceNotExistException, NoContentException, ServerSideProblemException {
    	return specification.doActions(mongoTemplate);
    }
    
    private void connectToDB() throws Exception {
		MongoClient mongoClient = new MongoClient();
		mongoTemplate = new MongoTemplate(new SimpleMongoDbFactory(mongoClient, DatabaseParameters.DB_NAME_DEFAULT));
	}

}
