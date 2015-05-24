package com.echarm.qasystem.answer.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class QuestionWithAnswers {
	@Id
	private String questionId;
	private List<Answer> answers;
	private Boolean isDeleted;
	
	public QuestionWithAnswers() {
	}

	public QuestionWithAnswers(String id) {
		setQuestionId(id);
		setIsDeleted(false);
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	

}
