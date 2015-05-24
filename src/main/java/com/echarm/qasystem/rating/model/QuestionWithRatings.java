package com.echarm.qasystem.rating.model;

import java.util.List;

import org.springframework.data.annotation.Id;


public class QuestionWithRatings {
	@Id
	private String questionId;
	private List<Rating> ratings;
	private Boolean isDeleted;
	
	public QuestionWithRatings() {
	}

	public QuestionWithRatings(String id) {
		setQuestionId(id);
		setIsDeleted(false);
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String articleId) {
		this.questionId = articleId;
	}

	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}
