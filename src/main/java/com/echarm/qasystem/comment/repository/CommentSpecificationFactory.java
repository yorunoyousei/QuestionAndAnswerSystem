package com.echarm.qasystem.comment.repository;

import com.echarm.qasystem.comment.model.Comment;

public class CommentSpecificationFactory {

	public static CommentSpecification getFindAllCommentSpecification(Comment comment) {
		return new FindAllCommentSpecification(comment);
	}

	public static CommentSpecification getFindCommentByFilterSpecification(Comment comment) {
		return new FindCommentByFilterSpecification(comment);
	}

	public static CommentSpecification getFindCommentByIdSpecification(Comment comment) {
		return new FindCommentByIdSpecification(comment);
	}

	public static CommentSpecification getFindCommentBySearchSpecification(Comment comment) {
		return new FindCommentBySearchSpecification(comment);
	}

	public static CommentSpecification getDeleteAllCommentSpecification(Comment comment) {
		return new DeleteAllCommentSpecification(comment);
	}

}
