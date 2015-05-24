package com.echarm.qasystem.answer.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.echarm.qasystem.answer.AnswerServiceApplication;
import com.echarm.qasystem.answer.error.NoContentException;
import com.echarm.qasystem.answer.error.ResourceNotExistException;
import com.echarm.qasystem.answer.model.Answer;
import com.echarm.qasystem.answer.repository.AnswerRepository;
import com.echarm.qasystem.answer.util.Category;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AnswerServiceApplication.class)
public class AnswerRepositoryTest {

	public AnswerRepositoryTest() {
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public void testUpdateComment() {
		Answer ans = new Answer();
//		ans.setAnswerId("1427009767683");
		ans.setQuestionId("123457");
		ans.setAnswererId("00000000");
		ans.setAnswerText("11111111");
		ans.setCategory(Category.Category_1);
		
		AnswerRepository cr = new AnswerRepository();
		try {
			cr.updateAnswer(ans);
		} catch (ResourceNotExistException | NoContentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
