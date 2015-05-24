package com.echarm.qasystem.question.controller;

import java.util.ArrayList;
import java.util.List;

import com.echarm.qasystem.question.model.Question;
import com.echarm.qasystem.question.util.Category;

public class QuestionTestObject {
	public static enum QuestionType {
		FILLED,
		MISSING_ONE_ATTRIBUTE,
		EMPTY,
		NULL;
	}

	public static enum QuestionListType {
		N_ELEMENTS,
		ONE_ELEMENT,
		ZERO_ELEMENT,
		NULL;
	}

	public static Question getTestQuestion(QuestionType type) {
		Question question = new Question();

		if (type.equals(QuestionType.FILLED)) {
			question.setQuestionId("Test_ID");
			question.setQuestionerId("Test_QuestionerID");
			Category[] categoryArr = Category.values();
			question.setCategory(categoryArr[((int)(Math.random() * categoryArr.length)) % categoryArr.length]);
			question.setContentText("Test_ContentText");
			question.setCreatedAt("Test_CreatedAt");
			String[] imgArr = {"img_1", "img_2", "img_3", "img_4", "img_5"};
			question.setImageArr(imgArr);
			question.setRating((int) (Math.random() * 100));
			question.setRatingCount((int) (Math.random() * 1000));
			String[] tagArr = {"tag_1", "tag_2", "tag_3", "tag_4"};
			question.setTagArr(tagArr);
			question.setTitle("Test_Title");
			question.setUpdatedAt("Test_UpdatedAt");
		}
		else if (type.equals(QuestionType.MISSING_ONE_ATTRIBUTE)) {
			question.setQuestionId("Test_ID");
			question.setQuestionerId("Test_QuestionerID");
			Category[] categoryArr = Category.values();
			question.setCategory(categoryArr[((int)(Math.random() * categoryArr.length)) % categoryArr.length]);
			question.setContentText("Test_ContentText");
			question.setCreatedAt("Test_CreatedAt");
			String[] imgArr = {"img_1", "img_2", "img_3", "img_4", "img_5"};
			question.setImageArr(imgArr);
			question.setRating((int) (Math.random() * 100));
			question.setRatingCount((int) (Math.random() * 1000));
			String[] tagArr = {"tag_1", "tag_2", "tag_3", "tag_4"};
			question.setTagArr(tagArr);
			question.setTitle("Test_Title");
			question.setUpdatedAt("Test_UpdatedAt");

			int missingIndex = (int) (Math.random() * 10);
			switch(missingIndex) {
				case 0:
				case 1:
					question.setQuestionerId(null);
					break;
				case 2:
					question.setContentText(null);
					break;
				case 3:
					question.setCreatedAt(null);
					break;
				case 4:
					question.setImageArr(null);
					break;
				case 5:
					question.setRating(-1);
					break;
				case 6:
					question.setRatingCount(-1);
					break;
				case 7:
					question.setTagArr(null);
					break;
				case 8:
					question.setTitle(null);
					break;
				case 9:
					question.setUpdatedAt(null);
					break;
				default:
					break;
			}
		}
		else if (type.equals(QuestionType.EMPTY)) {
			//do nothing
		}
		else if (type.equals(QuestionType.NULL)) {
			question = null;
		}

		return question;
	}

	public static Question getTestQuestion(int index) {
		Question question = new Question();
		question.setQuestionId("ID_" + index);
		question.setQuestionerId("QuestionerID_" + index);
		Category[] categoryArr = Category.values();
		question.setCategory(categoryArr[index % categoryArr.length]);
		question.setContentText("ContentText_" + index);
		question.setCreatedAt("CreatedAt_" + index);
		String[] imgArr = {"img_1", "img_2", "img_3", "img_4", "img_5"};
		question.setImageArr(imgArr);
		question.setRating(index);
		question.setRatingCount(index);
		question.setTagArr(imgArr);
		question.setTitle("Title_" + index);
		question.setUpdatedAt("UpdatedAt_" + index);

		return question;
	}

	public static List<Question> getQuestionTestList(QuestionListType type) {
		List<Question> questionList = new ArrayList<Question>();

		if (type.equals(QuestionListType.N_ELEMENTS)) {
			for (int i = 0; i < 5; i++) {
				questionList.add(getTestQuestion(i));
			}
		}
		else if (type.equals(QuestionListType.ONE_ELEMENT)) {
			questionList.add(getTestQuestion(0));
		}
		else if (type.equals(QuestionListType.ZERO_ELEMENT)) {

		}
		else if (type.equals(QuestionListType.NULL)) {
			return null;
		}

		return questionList;
	}

	public static String generateListJsonString(List<Question> questionList) {
	    if (questionList == null)
	        return null;

	    StringBuilder builder = new StringBuilder();
	    builder.append("[");

	    for (int i = 0; i < questionList.size(); i++) {
	        if (i != 0)
	            builder.append(",");
	        builder.append(generateJsonString(questionList.get(i)));
	    }

	    builder.append("]");

	    return builder.toString();
	}

	public static String generateJsonString(Question question) {
		if (question == null) {
			return null;
		}

		boolean isStarted = false;
		StringBuilder builder = new StringBuilder();
		builder.append("{");

		if (question.getQuestionId() != null) {
			builder.append("\"question_id\":\"" + question.getQuestionId() + "\"");
			isStarted = true;
		}

		if (question.getQuestionerId() != null) {
			if (isStarted)
				builder.append(",");
			builder.append("\"questioner_id\":\"" + question.getQuestionerId() + "\"");
			isStarted = true;
		}

		if (question.getTitle() != null) {
			if (isStarted)
				builder.append(",");
			builder.append("\"title\":\"" + question.getTitle() + "\"");
			isStarted = true;
		}

		if (question.getContentText() != null) {
			if (isStarted)
				builder.append(",");
			builder.append("\"content_text\":\"" + question.getContentText() + "\"");
			isStarted = true;
		}

		if (question.getImageArr() != null) {
			if (isStarted)
				builder.append(",");
			builder.append("\"image_arr\":[");
			String[] imgArr = question.getImageArr();
			for (int i = 0; i < imgArr.length; i++) {
				if (i != 0)
					builder.append(",");
				builder.append("\"" + imgArr[i] + "\"");
			}
			builder.append("]");
			isStarted = true;
		}

		if (question.getTagArr() != null) {
			if (isStarted)
				builder.append(",");
			builder.append("\"tag_arr\":[");
			String[] tagArr = question.getTagArr();
			for (int i = 0; i < tagArr.length; i++) {
				if (i != 0)
					builder.append(",");
				builder.append("\"" + tagArr[i] + "\"");
			}
			builder.append("]");
			isStarted = true;
		}

		if (question.getCreatedAt() != null) {
			if (isStarted)
				builder.append(",");
			builder.append("\"created_at\":\"" + question.getCreatedAt() + "\"");
			isStarted = true;
		}

		if (question.getUpdatedAt() != null) {
			if (isStarted)
				builder.append(",");
			builder.append("\"updated_at\":\"" + question.getUpdatedAt() + "\"");
			isStarted = true;
		}

		if (question.getRating() != null) {
			if (isStarted)
				builder.append(",");
			builder.append("\"rating\":" + question.getRating());
			isStarted = true;
		}

		if (question.getRatingCount() != null) {
			if (isStarted)
				builder.append(",");
			builder.append("\"rating_count\":" + question.getRatingCount());
			isStarted = true;
		}

		if (question.getCategoryStr() != null) {
			if (isStarted)
				builder.append(",");
			builder.append("\"category\":\"" + question.getCategoryStr() + "\"");
			isStarted = true;
		}

		builder.append("}");
		return builder.toString();
	}
}
