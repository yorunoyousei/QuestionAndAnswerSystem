package com.echarm.qasystem.answer.repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.echarm.qasystem.answer.error.QuestionNotExistErrorBody;
import com.echarm.qasystem.answer.error.CategoryNotExistErrorBody;
import com.echarm.qasystem.answer.error.AnswerNotExistErrorBody;
import com.echarm.qasystem.answer.error.NoContentException;
import com.echarm.qasystem.answer.error.ResourceNotExistException;
import com.echarm.qasystem.answer.error.ServerSideProblemException;
import com.echarm.qasystem.answer.model.QuestionWithAnswers;
import com.echarm.qasystem.answer.model.Answer;
import com.echarm.qasystem.answer.util.DatabaseParameters;
import com.echarm.qasystem.answer.util.StringFormat;
import com.echarm.qasystem.answer.util.Time;
import com.mongodb.MongoClient;

@Repository
public class AnswerRepository implements AnswerRepositoryService {
	
	private MongoTemplate mongoTemplate = null;
	
	public AnswerRepository() {
		try {
			connectToDB();
		} catch (Exception e) {
			throw new ServerSideProblemException("Cannot connect to the answer database!!!");
		}
	}

    @Override
    public Answer createAnswer(Answer answer) {
        // TODO Auto-generated method stub
    	/**
    	 * 1. Check if all necessary fields are filled. If not, throw an exception  
    	 * 2. Check if the category exists. If not, create one.
    	 * 3. Check if the question exists. 
    	 *   3.1 If yes, insert the answer in the question. 
    	 *   3.2 If not, create one with the answer built in.
    	 */
    	String[] missingArr = answer.getNullJsonInputFieldName();
    	
    	// 1.
    	if(missingArr != null) {
    		throw new ServerSideProblemException("Input question model is not complete!!! Missing field: " + Arrays.toString(missingArr));
    	}
    	
    	// 2.
    	String colName = answer.getCategory().toString();
    	String questionId = answer.getQuestionId().toString();
		if(!mongoTemplate.collectionExists(colName))
			mongoTemplate.createCollection(colName);
    	
    	// 3
//		answer.setAnswerId(Time.getCurrentTimeMillisStr());
		answer.setIsDeleted(false);
		
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("questionId").is(questionId));
		if(mongoTemplate.exists(findQuery, QuestionWithAnswers.class, colName)) {
			Update update = new Update();
			update.push("answers", answer);
			mongoTemplate.updateFirst(findQuery, update, QuestionWithAnswers.class, colName);
		}
		else {
			QuestionWithAnswers awc = new QuestionWithAnswers(questionId);
			ArrayList<Answer> cmtArr = new ArrayList<Answer>();
			cmtArr.add(answer);
			awc.setAnswers(cmtArr);
			mongoTemplate.save(awc, colName);
		}
		
        return answer;
    }

    @Override
    public Answer updateAnswer(Answer answer)
            throws ResourceNotExistException, NoContentException {
        // TODO Auto-generated method stub
    	QuestionWithAnswers awc = null;
    	Answer cmt = null;
    	
    	String colName = answer.getCategory().toString();
    	String questionId = answer.getQuestionId();
//    	String answerId = answer.getAnswerId();
    	if(questionId == null || questionId.equals("") || colName == null || colName.equals("") ) {
			throw new ServerSideProblemException("Input question model is not complete!!! Missing field: `question_id`, `answer_id` or `category`");
		}
    	if(!mongoTemplate.collectionExists(colName)) {
    		ResourceNotExistException e = new ResourceNotExistException("Requested question with id = \"" + questionId + "\" doesnot exists");
			e.setErrorBody(new CategoryNotExistErrorBody(questionId));
			throw e;
    	}
    	String[] fieldNameArr = answer.getNonNullFieldName();
		Update updateParam = new Update();
		Method method;
		String fieldName;
		if(fieldNameArr == null) {
			throw new NoContentException();
		}
		for(int index = 0; index < fieldNameArr.length; index++) {
			fieldName = fieldNameArr[index];
			try {
				method = answer.getClass().getMethod("get"+ StringFormat.snake2CamelCapital(fieldName));
				updateParam.set("answers.$." + StringFormat.snake2Camel(fieldName), method.invoke(answer));
			} catch (NoSuchMethodException e) {
				throw new ServerSideProblemException("No such attribute to update!!! (" + fieldName + ")");
				//e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Query query = new Query(Criteria.where("_id").is(questionId));
    	query.addCriteria(Criteria.where("answers.questionId").is(questionId));
    	awc = mongoTemplate.findAndModify(query, updateParam, new FindAndModifyOptions().returnNew(true), QuestionWithAnswers.class, colName);
    	if(awc == null) {
    		ResourceNotExistException e = new ResourceNotExistException("Requested answer with questionId = \"" + questionId + "\" doesnot exists");
			e.setErrorBody(new AnswerNotExistErrorBody(questionId));
			throw e;
    	}
    	for(int index = 0; index < awc.getAnswers().size(); index++) {
    		cmt = awc.getAnswers().get(index);
    		if(cmt.getQuestionId().equals(questionId))
    			return cmt;
    	}
        return null;
    }

    @Override
    public Answer deleteAnswer(Answer answer)
            throws ResourceNotExistException{
        // TODO Auto-generated method stub
    	
    	QuestionWithAnswers awc = null;
    	Answer cmt = null;
    	
    	String colName = answer.getCategory().toString();
    	String questionId = answer.getQuestionId();
//    	String answerId = answer.getAnswerId();
    	if(questionId == null || questionId.equals("") || colName == null || colName.equals("") ) {
			throw new ServerSideProblemException("Input question model is not complete!!! Missing field: `question_id`, `answer_id` or `category`");
		}
    	if(!mongoTemplate.collectionExists(colName)) {
    		ResourceNotExistException e = new ResourceNotExistException("Requested question with id = \"" + questionId + "\" doesnot exists");
			e.setErrorBody(new CategoryNotExistErrorBody(questionId));
			throw e;
    	}
    	
    	Query query = new Query(Criteria.where("_id").is(questionId));
    	query.addCriteria(Criteria.where("answers.questionId").is(questionId));
    	Update update = new Update();
        update.set("answers.$.isDeleted", true);
        awc = mongoTemplate.findAndModify(query, update, QuestionWithAnswers.class, colName);
    	if(awc == null) {
    		ResourceNotExistException e = new ResourceNotExistException("Requested answer with questionId = \"" + questionId + "\" doesnot exists");
			e.setErrorBody(new AnswerNotExistErrorBody(questionId));
			throw e;
    	}
    	for(int index = 0; index < awc.getAnswers().size(); index++) {
    		cmt = awc.getAnswers().get(index);
    		if(cmt.getQuestionId().equals(questionId))
    			return cmt;
    	}
        return null;
        
    }

    @Override
    public List<Answer> query(AnswerSpecification specification)
            throws ResourceNotExistException, NoContentException, ServerSideProblemException {
    	return specification.doActions(mongoTemplate);
    }
    
    private void connectToDB() throws Exception {
		MongoClient mongoClient = new MongoClient();
		mongoTemplate = new MongoTemplate(new SimpleMongoDbFactory(mongoClient, DatabaseParameters.DB_NAME_DEFAULT));
	}

}
