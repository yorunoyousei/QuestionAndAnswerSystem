package com.echarm.qasystem.comment.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class QuestionWithComments {
	@Id
	private String questionId;
	private List<Comment> comments;
	private Boolean isDeleted;
	
	public QuestionWithComments() {
	}

	public QuestionWithComments(String id) {
		setQuestionId(id);
		setIsDeleted(false);
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	

}
