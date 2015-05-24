package com.echarm.qasystem.question.model;

import java.util.ArrayList;
import java.util.List;

import com.echarm.qasystem.question.util.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Question object
 *   Variable Name    Corresponding Json Key   Description
 *   category
 *   questionId       question_id
 *   questionerId     questioner_id
 *   title            title
 *   contentText      content_text
 *   imageArr         image_arr
 *   tagArr           tag_arr
 *   createdAt        created_at
 *   updatedAt        updated_at
 *   rating           rating
 *   ratingCount      rating_count
 *
 */

@JsonInclude(Include.NON_NULL)
public class Question {
	private Category category;           // the category of this question, not included in json
	private String questionId;
	private String questionerId;
	private String title;
	private String contentText;
	private String[] imageArr;
	private String[] tagArr;
	private String createdAt;
	private String updatedAt;
	private Integer rating;
	private Integer ratingCount;
	private Boolean deleted;     // the status of this question (whether it is deleted), not included in json

	public Question() {
	}

	/*
	 * setter
	 */
	@JsonIgnore
	public void setCategory(Category category) {
		this.category = category;
	}

	@JsonIgnore
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	@JsonProperty("questioner_id")
	public void setQuestionerId(String questionerId) {
		this.questionerId = questionerId;
	}

	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("content_text")
	public void setContentText(String contentText) {
		this.contentText = contentText;
	}

	@JsonProperty("image_arr")
	public void setImageArr(String[] imageArr) {
		this.imageArr = imageArr;
	}

	@JsonProperty("tag_arr")
	public void setTagArr(String[] tagArr) {
		this.tagArr = tagArr;
	}

	@JsonProperty("created_at")
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@JsonProperty("updated_at")
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	@JsonProperty("rating")
	public void setRating(int rating) {
		if (rating == -1) {
			this.rating = null;
		}
		else {
			this.rating = rating;
		}
	}

	@JsonProperty("rating_count")
	public void setRatingCount(int ratingCount) {
		if (ratingCount == -1) {
			this.ratingCount = null;
		}
		else {
			this.ratingCount = ratingCount;
		}

	}

	@JsonIgnore
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/*
	 * getter
	 */
	@JsonIgnore
	public Category getCategory() {
		return category;
	}

	@JsonProperty("category")
	public String getCategoryStr() {
		return category == null ? null : category.name();
	}

	@JsonProperty("question_id")
	public String getQuestionId() {
		return questionId;
	}

	@JsonProperty("questioner_id")
	public String getQuestionerId() {
		return questionerId;
	}

	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	@JsonProperty("content_text")
	public String getContentText() {
		return contentText;
	}

	@JsonProperty("image_arr")
	public String[] getImageArr() {
		return imageArr;
	}

	@JsonProperty("tag_arr")
	public String[] getTagArr() {
		return tagArr;
	}

	@JsonProperty("created_at")
	public String getCreatedAt() {
		return createdAt;
	}

	@JsonProperty("updated_at")
	public String getUpdatedAt() {
		return updatedAt;
	}

	@JsonProperty("rating")
	public Integer getRating() {
		return rating;
	}

	@JsonProperty("rating_count")
	public Integer getRatingCount() {
		return ratingCount;
	}

	@JsonIgnore
	public Boolean getDeleted() {
		return deleted;
	}

	// check if all fields are null (used in controllers to check if there is an empty json body from HTTP request)
	@JsonIgnore
	public static boolean isAllQuestionFieldNull(Question question) {
	    return question.getQuestionerId() == null && question.getCategory() == null &&
	           question.getContentText() == null && question.getCreatedAt() == null && question.getImageArr() == null &&
	           question.getRating() == null && question.getRatingCount() == null && question.getTagArr() == null &&
	           question.getTitle() == null && question.getUpdatedAt() == null;
	}

	// check if all fields are non-null (used in controllers to check if all question fields are in the json body from HTTP request)
	@JsonIgnore
	public static boolean isAllQuestionFieldNonNull(Question question) {
	    return question.getQuestionerId() != null &&
	           question.getContentText() != null && question.getCreatedAt() != null && question.getImageArr() != null &&
	           question.getRating() != null && question.getRatingCount() != null && question.getTagArr() != null &&
	           question.getTitle() != null && question.getUpdatedAt() != null;
	}

	/*
	 * Helper methods for description in Errorbody.
	 * getNullFieldName will generate a string array containing all field names that have null value.
	 * getEmptyFieldName will generate a string array containing all field names that have empty(i.e. "") value.
	 */

	@JsonIgnore
	public String[] getNullFieldName() {
	    List<String> nullList = new ArrayList<String>();

	    if (this.questionerId == null)
	        nullList.add("question_id");

	    if (this.contentText == null)
	        nullList.add("content_text");

	    if (this.createdAt == null)
	        nullList.add("created_at");

	    if (this.imageArr == null)
	        nullList.add("image_arr");

	    if (this.rating == null)
            nullList.add("rating");

	    if (this.ratingCount == null)
            nullList.add("rating_count");

	    if (this.tagArr == null)
	        nullList.add("tag_arr");

	    if (this.title == null)
	        nullList.add("title");

	    if (this.updatedAt == null)
            nullList.add("updated_at");

	    if (nullList.size() != 0)
	        return nullList.toArray(new String[nullList.size()]);

	    return null;
	}
	
	@JsonIgnore
	public String[] getNonNullFieldCamelName() {
	    List<String> nonNullList = new ArrayList<String>();

	    if (this.questionerId != null)
	        nonNullList.add("questioner_Id");

	    if (this.contentText != null)
	        nonNullList.add("contentText");

	    if (this.createdAt != null)
	        nonNullList.add("createdAt");

	    if (this.imageArr != null)
	        nonNullList.add("imageArr");

	    if (this.rating != null)
            nonNullList.add("rating");

	    if (this.ratingCount != null)
            nonNullList.add("ratingCount");

	    if (this.tagArr != null)
	        nonNullList.add("tagArr");

	    if (this.title != null)
	        nonNullList.add("title");

	    if (this.updatedAt != null)
            nonNullList.add("updatedAt");

	    if (nonNullList.size() != 0)
	        return nonNullList.toArray(new String[nonNullList.size()]);

	    return null;
	}

	@JsonIgnore
	public String[] getEmptyFieldName() {
	    List<String> emptyList = new ArrayList<String>();

        if (this.questionerId == null)
            emptyList.add("questioner_id");

        if (this.contentText == null)
            emptyList.add("content_text");

        if (this.createdAt == null)
            emptyList.add("created_at");

        if (this.imageArr == null)
            emptyList.add("image_arr");

        if (this.rating == null)
            emptyList.add("rating");

        if (this.ratingCount == null)
            emptyList.add("rating_count");

        if (this.tagArr == null)
            emptyList.add("tag_arr");

        if (this.title == null)
            emptyList.add("title");

        if (this.updatedAt == null)
            emptyList.add("updated_at");

        if (emptyList.size() != 0)
            return emptyList.toArray(new String[emptyList.size()]);

	    return null;
	}
}