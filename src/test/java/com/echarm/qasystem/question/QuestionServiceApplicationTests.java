package com.echarm.qasystem.question;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.echarm.qasystem.question.QuestionServiceApplication;
import com.echarm.qasystem.question.model.Question;
import com.echarm.qasystem.question.repository.QuestionRepository;
import com.echarm.qasystem.question.util.Category;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QuestionServiceApplication.class)
public class QuestionServiceApplicationTests {

	@Test
	public void contextLoad() {
		
	}
}
