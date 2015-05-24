package com.echarm.qasystem.comment.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.echarm.qasystem.comment.error.QuestionNotExistErrorBody;
import com.echarm.qasystem.comment.error.NoContentException;
import com.echarm.qasystem.comment.error.ResourceNotExistException;
import com.echarm.qasystem.comment.error.ServerSideProblemException;
import com.echarm.qasystem.comment.model.QuestionWithComments;
import com.echarm.qasystem.comment.model.Comment;

public class FindAllCommentSpecification extends CommentSpecification{

	public FindAllCommentSpecification(Comment comment) {
		super(comment);
		// TODO Auto-generated constructor stub
	}

	@Override
	List<Comment> doActions(MongoTemplate mongoTemplate) throws ResourceNotExistException, NoContentException {
		// TODO Auto-generated method stub
		QuestionWithComments resultAWC = null;
		ArrayList<Comment> resultArr = null;
		
		String questionId = comment.getQuestionId();
		String category = comment.getCategoryStr();
		if(questionId == null || questionId.equals("") || category == null || category.equals("")) {
			throw new ServerSideProblemException("Input comment model is not complete!!! Missing field: `question_id` or `category`");
		}
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("questionId").is(questionId));
		findQuery.addCriteria(Criteria.where("isDeleted").is(false));
		resultAWC = mongoTemplate.findOne(findQuery, QuestionWithComments.class, category);
		if(resultAWC == null) {
			ResourceNotExistException e = new ResourceNotExistException("Requested question with id = \"" + questionId + "\" doesnot exists");
			e.setErrorBody(new QuestionNotExistErrorBody(questionId));
			throw e;
		}
		else {
			Comment tempComment = null;
			
			resultArr = new ArrayList<Comment>();
			for(int index = 0; index < resultAWC.getComments().size(); index++) {
				tempComment = resultAWC.getComments().get(index);
				if(!tempComment.getIsDeleted())
					resultArr.add(tempComment);
			}
			if(resultArr.size() == 0)
				throw new NoContentException();
			else
				return resultArr;
		}
	}

}
