package com.echarm.qasystem.rating.model;

import java.util.ArrayList;
import java.util.List;

import com.echarm.qasystem.rating.util.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Rating object
 *   Variable Name       Corresponding Json Key   Description
 *   category
 *   questionId           question_id
 *   ratingId            rating_id
 *   raterId             rater_id
 *   ratingValue         ratingValue
 *   createdAt           created_at
 *   updatedAt           updated_at
 *
 */
@JsonInclude(Include.NON_NULL)
public class Rating {
    private Category category;
    private String ratingId;
    private String questionId;
    private String raterId;
    private Integer ratingValue;
    private String createdAt;
    private String updatedAt;
    private Boolean isDeleted;

    public Rating() {

    }

    /*
     * setter
     */

    @JsonIgnore
    public void setCategory(Category category) {
        this.category = category;
    }

    @JsonIgnore
    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    @JsonIgnore
    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    @JsonProperty("rater_id")
    public void setRaterId(String raterId) {
        this.raterId = raterId;
    }

    @JsonProperty("rating_value")
    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("updated_at")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonIgnore
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
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

    @JsonProperty("rating_id")
    public String getRatingId() {
        return ratingId;
    }

    @JsonProperty("rater_id")
    public String getRaterId() {
        return raterId;
    }

    @JsonProperty("rating_value")
    public Integer getRatingValue() {
        return ratingValue;
    }

    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("updated_at")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @JsonIgnore
    public boolean getIsDeleted() {
        return isDeleted;
    }

    /*
     * Json Input Validation Helper Functions
     */

    @JsonIgnore
    public boolean isAllJsonInputFieldNonNull() {
        return this.raterId != null && this.ratingValue != null && this.createdAt != null &&
               this.updatedAt != null;
    }

    @JsonIgnore
    public boolean isAllJsonInputFieldNonEmpty() {
        return !this.raterId.equals("") && !this.updatedAt.equals("") && !this.createdAt.equals("");
    }

    @JsonIgnore
    public boolean isAllJsonInputFieldNull() {
        return this.raterId == null && this.ratingValue == null && this.createdAt == null &&
               this.updatedAt == null;
    }

    @JsonIgnore
    public String[] getNullJsonInputFieldName() {
        List<String> nullFieldNameList = new ArrayList<String>();

        if (this.raterId == null)
            nullFieldNameList.add("rater_id");

        if (this.ratingValue == null)
            nullFieldNameList.add("rating_value");

        if (this.createdAt == null)
            nullFieldNameList.add("created_at");

        if (this.updatedAt == null)
            nullFieldNameList.add("updated_at");

        if (nullFieldNameList.size() > 0)
            return nullFieldNameList.toArray(new String[nullFieldNameList.size()]);

        return null;
    }
    
    @JsonIgnore
    public String[] getNonNullFieldName() {
        List<String> fieldNameList = new ArrayList<String>();

        if (this.raterId != null)
        	fieldNameList.add("rater_id");

        if (this.ratingValue != null)
        	fieldNameList.add("rating_value");

        if (this.createdAt != null)
        	fieldNameList.add("created_at");

        if (this.updatedAt != null)
        	fieldNameList.add("updated_at");
        
        if (this.isDeleted != null)
        	fieldNameList.add("isDeleted");

        if (fieldNameList.size() > 0)
            return fieldNameList.toArray(new String[fieldNameList.size()]);

        return null;
    }

    @JsonIgnore
    public String[] getEmptyJsonInputFieldName() {
        List<String> emptyFieldNameList = new ArrayList<String>();

        if (this.raterId.equals(""))
            emptyFieldNameList.add("rater_id");

        if (this.createdAt.equals(""))
            emptyFieldNameList.add("created_at");

        if (this.updatedAt.equals(""))
            emptyFieldNameList.add("updated_at");

        if (emptyFieldNameList.size() > 0)
            return emptyFieldNameList.toArray(new String[emptyFieldNameList.size()]);

        return null;
    }
}
