package com.echarm.qasystem.answer.model;

import java.util.ArrayList;
import java.util.List;

import com.echarm.qasystem.answer.util.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Answer object
 *   Variable Name       Corresponding Json Key   Description
 *   category
 *   questionId          question_id
 *   answerId            answer_id
 *   answererId          answerer_id
 *   answerText          answer_text
 *   createdAt           created_at
 *   updatedAt           updated_at
 *   authorResponseText  author_response_text
 *   respondedAt         responded_at
 *   isDeleted           is_deleted
 */

@JsonInclude(Include.NON_NULL)
public class Answer {
    private Category category;
    private String questionId;
//    private String answerId;
    private String answererId;
    private String answerText;
    private String createdAt;
    private String updatedAt;
//    private String authorResponseText;
//    private String respondedAt;
    private Boolean isDeleted;

    public Answer() {

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

//    @JsonIgnore
//    public void setAnswerId(String answerId) {
//        this.answerId = answerId;
//    }

    @JsonProperty("answerer_id")
    public void setAnswererId(String answererId) {
        this.answererId = answererId;
    }

    @JsonProperty("answer_text")
    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("updated_at")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

//    @JsonProperty("author_response_text")
//    public void setAuthorResponseText(String authorResponseText) {
//        this.authorResponseText = authorResponseText;
//    }
//
//    @JsonProperty("responded_at")
//    public void setRespondedAt(String respondedAt) {
//        this.respondedAt = respondedAt;
//    }

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

//    @JsonProperty("answer_id")
//    public String getAnswerId() {
//        return answerId;
//    }

    @JsonProperty("answerer_id")
    public String getAnswererId() {
        return answererId;
    }

    @JsonProperty("answer_text")
    public String getAnswerText() {
        return answerText;
    }

    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("updated_at")
    public String getUpdatedAt() {
        return updatedAt;
    }

//    @JsonProperty("author_response_text")
//    public String getAuthorResponseText() {
//        return authorResponseText;
//    }

//    @JsonProperty("responded_at")
//    public String getRespondedAt() {
//        return respondedAt;
//    }

    @JsonIgnore
    public boolean getIsDeleted() {
        return isDeleted;
    }

    /*
     * Json Input Validation Helper Functions
     */

    @JsonIgnore
    public boolean isAllJsonInputFieldNonNull() {
        return this.answererId != null && this.answerText != null && this.createdAt != null &&
               this.updatedAt != null;
    }

    @JsonIgnore
    public boolean isAllJsonInputFieldNonEmpty() {
        return (this.answererId == null || !this.answererId.equals("")) &&
               (this.answerText == null || !this.answerText.equals("")) &&
               (this.createdAt == null   || !this.createdAt.equals("")) &&
               (this.updatedAt == null   || !this.updatedAt.equals(""));
    }

    @JsonIgnore
    public boolean isAllJsonInputFieldNull() {
        return this.answererId == null && this.answerText == null && this.createdAt == null &&
               this.updatedAt == null;
    }

    @JsonIgnore
    public String[] getNullJsonInputFieldName() {
        List<String> nullFieldNameList = new ArrayList<String>();

        if (this.answererId == null)
            nullFieldNameList.add("answerer_id");

        if (this.answerText == null)
            nullFieldNameList.add("answer_text");

        if (this.createdAt == null)
            nullFieldNameList.add("created_at");

        if (this.updatedAt == null)
            nullFieldNameList.add("updated_at");

//        if (this.authorResponseText == null)
//            nullFieldNameList.add("author_response_text");
//
//        if (this.respondedAt == null)
//            nullFieldNameList.add("responded_at");

        if (nullFieldNameList.size() > 0)
            return nullFieldNameList.toArray(new String[nullFieldNameList.size()]);

        return null;
    }

    @JsonIgnore
    public String[] getNonNullFieldName() {
        List<String> fieldNameList = new ArrayList<String>();

        if (this.answererId != null)
            fieldNameList.add("answerer_id");

        if (this.answerText != null)
            fieldNameList.add("answer_text");

        if (this.createdAt != null)
            fieldNameList.add("created_at");

        if (this.updatedAt != null)
            fieldNameList.add("updated_at");

//        if (this.authorResponseText != null)
//            fieldNameList.add("author_response_text");
//
//        if (this.respondedAt != null)
//            fieldNameList.add("responded_at");

        if (this.isDeleted != null)
            fieldNameList.add("isDeleted");

        if (fieldNameList.size() > 0)
            return fieldNameList.toArray(new String[fieldNameList.size()]);

        return null;
    }

    // allow empty authorResponseText and respondedAt
    @JsonIgnore
    public String[] getEmptyJsonInputFieldName() {
        List<String> emptyFieldNameList = new ArrayList<String>();

        if (this.answererId != null && this.answererId.equals(""))
            emptyFieldNameList.add("answerer_id");

        if (this.answerText != null && this.answerText.equals(""))
            emptyFieldNameList.add("answer_text");

        if (this.createdAt != null && this.createdAt.equals(""))
            emptyFieldNameList.add("created_at");

        if (this.updatedAt != null && this.updatedAt.equals(""))
            emptyFieldNameList.add("updated_at");

        if (emptyFieldNameList.size() > 0)
            return emptyFieldNameList.toArray(new String[emptyFieldNameList.size()]);

        return null;
    }
}
