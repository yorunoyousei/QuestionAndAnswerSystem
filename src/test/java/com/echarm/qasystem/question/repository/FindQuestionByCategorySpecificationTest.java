package com.echarm.qasystem.question.repository;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.echarm.qasystem.question.QuestionServiceApplication;
import com.echarm.qasystem.question.error.NoContentException;
import com.echarm.qasystem.question.error.ResourceNotExistException;
import com.echarm.qasystem.question.model.Question;
import com.echarm.qasystem.question.model.QuestionFactory;
import com.echarm.qasystem.question.repository.QuestionRepository;
import com.echarm.qasystem.question.repository.QuestionSpecification;
import com.echarm.qasystem.question.repository.QuestionSpecificationFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QuestionServiceApplication.class)
public class FindQuestionByCategorySpecificationTest {
	@Test
	public void testMain(){
		QuestionRepository qr = new QuestionRepository();
		Question question = QuestionFactory.getTestQuestionObject();
		QuestionSpecification readAllQuestionSpec = QuestionSpecificationFactory.getFindQuestionByCategorySpecification(question);
		List<Question> results = null;
        try {
			results = qr.query(readAllQuestionSpec);
			
		} catch (ResourceNotExistException | NoContentException e) {
			e.printStackTrace();
		} catch(Exception e ){
			e.printStackTrace();
		} finally{
			System.out.print(Arrays.toString(results.toArray()));;
		}
	} 
}
