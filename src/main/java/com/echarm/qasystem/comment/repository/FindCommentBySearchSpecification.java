package com.echarm.qasystem.comment.repository;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.echarm.qasystem.comment.model.Comment;

public class FindCommentBySearchSpecification extends CommentSpecification{

	public FindCommentBySearchSpecification(Comment comment) {
		super(comment);
		// TODO Auto-generated constructor stub
	}

	@Override
	List<Comment> doActions(MongoTemplate mongoTemplate) {
		// TODO Auto-generated method stub
		return null;
	}

}
