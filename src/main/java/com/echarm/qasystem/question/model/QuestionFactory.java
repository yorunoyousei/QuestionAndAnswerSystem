package com.echarm.qasystem.question.model;

import com.echarm.qasystem.question.util.Category;
import com.echarm.qasystem.question.util.Time;

public class QuestionFactory {

	public QuestionFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public static Question getBasicInitQuestionObject() {
		Question question = new Question();
		
		question.setQuestionId(Time.getCurrentTimeStr());
		question.setDeleted(false);
		question.setRating(0);
		question.setRatingCount(0);
		
		return question;
	}
	
	public static Question getBasicInitQuestionObject(String id, Category cat) {
		Question a = new Question();
		a.setQuestionId(id);
		a.setCategory(cat);
		a.setRating(2);
		a.setRatingCount(5);
		return a;
	}
	
	public static Question getTestUpdateQuestionObject(String id, Category cat) {
		Question a = QuestionFactory.getBasicInitQuestionObject(id, cat);
		
		return a;
	}
	
	public static Question getTestQuestionObject() {
		Question a = getBasicInitQuestionObject();
		a.setQuestionerId("test_author_id");
		a.setCategory(Category.Category_1);
		a.setContentText("test_content");
		a.setImageArr(new String[1]);
		a.setTagArr(new String[1]);
		a.setTitle("test_title");
		a.setCreatedAt(Time.getCurrentTimeStr());
		a.setUpdatedAt(Time.getCurrentTimeStr());
		return a;
	}
	
	public static Question getIncompleteQuestionObject() {
		Question a = getBasicInitQuestionObject();
		a.setQuestionerId("test_author_id");
		a.setCategory(Category.Category_1);
		//a.setContentText("test_content");
		a.setImageArr(new String[1]);
		a.setTagArr(new String[1]);
		//a.setTitle("test_title");
		return a;
	}
	
	
}
