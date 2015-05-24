package com.echarm.qasystem.comment.repository;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.echarm.qasystem.comment.model.Comment;

public class FindCommentByFilterSpecification extends CommentSpecification{

	public FindCommentByFilterSpecification(Comment comment) {
		super(comment);
	}

	@Override
	List<Comment> doActions(MongoTemplate mongoTemplate) {
		return null;
	}

}
