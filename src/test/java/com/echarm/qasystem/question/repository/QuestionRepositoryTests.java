package com.echarm.qasystem.question.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.echarm.qasystem.question.QuestionServiceApplication;
import com.echarm.qasystem.question.model.Question;
import com.echarm.qasystem.question.model.QuestionFactory;
import com.echarm.qasystem.question.repository.QuestionRepository;
import com.echarm.qasystem.question.util.Category;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QuestionServiceApplication.class)
public class QuestionRepositoryTests {

	@Test
	public void testCreateQuestion() throws Exception {
		QuestionRepository qr = new QuestionRepository();
		Question question = QuestionFactory.getTestQuestionObject();
		qr.createQuestion(question);
	}
	
	@Test
	public void testDeleteQuestion() throws Exception {
		QuestionRepository qr = new QuestionRepository();
		Question question = QuestionFactory.getTestQuestionObject();
		question.setQuestionId("1425905932479");
		qr.deleteQuestion(question);
	}
	
	@Test
	public void testUpdateQuestion() throws Exception {
		QuestionRepository qr = new QuestionRepository();
		Question question = QuestionFactory.getTestUpdateQuestionObject("1425906190178", Category.Category_1);
		qr.updateQuestion(question);
	}
}
