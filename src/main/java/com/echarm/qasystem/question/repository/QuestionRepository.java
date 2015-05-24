package com.echarm.qasystem.question.repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.echarm.qasystem.question.error.QuestionNotExistErrorBody;
import com.echarm.qasystem.question.error.NoContentException;
import com.echarm.qasystem.question.error.ResourceNotExistException;
import com.echarm.qasystem.question.error.ServerSideProblemException;
import com.echarm.qasystem.question.model.Question;
import com.echarm.qasystem.question.util.Category;
import com.echarm.qasystem.question.util.DatabaseParameters;
import com.echarm.qasystem.question.util.Time;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

@Repository
public class QuestionRepository implements QuestionRepositoryService  {

	private MongoTemplate mongoTemplate = null;

	public QuestionRepository() {
		try {
			connectToDB();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			throw new ServerSideProblemException("Cannot connect to the question database!!!");
		}
	}
	
	private void connectToDB() throws Exception {
		MongoClient mongoClient = new MongoClient();
		mongoTemplate = new MongoTemplate(new SimpleMongoDbFactory(mongoClient, DatabaseParameters.DB_NAME_DEFAULT));
	}

	@Override
	public Question createQuestion(Question question) throws ServerSideProblemException{
		String[] missingArr = question.getNullFieldName();
		if(missingArr != null) {
			throw new ServerSideProblemException("Input question model is not complete!!! Missing field: " + Arrays.toString(missingArr));
		}
		String colName = question.getCategory().toString();
		if(!mongoTemplate.collectionExists(colName))
			mongoTemplate.createCollection(colName);
		question.setQuestionId(Time.getCurrentTimeMillisStr());
		question.setDeleted(false);
		mongoTemplate.save(question, colName);
		return question;
	}

	@Override
	public Question updateQuestion(Question question)
			throws ResourceNotExistException, NoContentException {
		// TODO Auto-generated method stub
		
		String questionId = question.getQuestionId();
		String category = question.getCategoryStr();
		if(questionId == null || questionId.equals("") || category == null || category.equals("")) {
			throw new ServerSideProblemException("Input question model is not complete!!! Missing field: `question_id` or `category`");
		}
		String[] fieldNameArr = question.getNonNullFieldCamelName();
		Update updateParam = new Update();
		Method method;
		String fieldName;
		if(fieldNameArr == null) {
			throw new NoContentException();
		}
		for(int index = 0; index < fieldNameArr.length; index++) {
			fieldName = fieldNameArr[index];
			try {
				method = question.getClass().getMethod("get"+ fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
				updateParam.set(fieldName, method.invoke(question));
			} catch (NoSuchMethodException e) {
				throw new ServerSideProblemException("No such attribute to update!!!");
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
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("questionId").is(questionId));
		Question tempQuestion = mongoTemplate.findAndModify(findQuery, updateParam, new FindAndModifyOptions().returnNew(true), Question.class, category);
		if(tempQuestion == null) {
			ResourceNotExistException e = new ResourceNotExistException("Requested question with id = \"" + questionId + "\" doesnot exists");
			e.setErrorBody(new QuestionNotExistErrorBody(questionId));
			throw e;
		}
		
		return tempQuestion;
	}

	@Override
	public Question deleteQuestion(Question question)
			throws ResourceNotExistException {
		String questionId = question.getQuestionId();
		String category = question.getCategoryStr();
		if(questionId == null || questionId.equals("") || category == null || category.equals("")) {
			throw new ServerSideProblemException("Input question model is not complete!!! Missing field: `question_id`");
		}
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("questionId").is(questionId));
		findQuery.addCriteria(Criteria.where("deleted").is(false));
		Update updateParam = new Update();
		updateParam.set("deleted", true);
		Question tempQuestion = mongoTemplate.findAndModify(findQuery, updateParam, new FindAndModifyOptions().returnNew(true), Question.class, category);
		if(tempQuestion == null) {
			ResourceNotExistException e = new ResourceNotExistException("Requested question with id = \"" + questionId + "\" doesnot exists");
			e.setErrorBody(new QuestionNotExistErrorBody(questionId));
			throw e;
		}
		return tempQuestion;
	}

	@Override
	public List<Question> query(QuestionSpecification specification)
			throws ResourceNotExistException, NoContentException {
		return specification.doActions(mongoTemplate);
	}



}
