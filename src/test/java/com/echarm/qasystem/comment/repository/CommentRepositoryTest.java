package com.echarm.qasystem.comment.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.echarm.qasystem.comment.CommentServiceApplication;
import com.echarm.qasystem.comment.error.NoContentException;
import com.echarm.qasystem.comment.error.ResourceNotExistException;
import com.echarm.qasystem.comment.model.Comment;
import com.echarm.qasystem.comment.repository.CommentRepository;
import com.echarm.qasystem.comment.util.Category;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CommentServiceApplication.class)
public class CommentRepositoryTest {

	public CommentRepositoryTest() {
		// TODO Auto-generated constructor stub
	}
	
	@Test
	public void testUpdateComment() {
		Comment cmt = new Comment();
		cmt.setCommentId("1427009767683");
		cmt.setQuestionId("123457");
		cmt.setCommenterId("00000000");
		cmt.setCommentText("11111111");
		cmt.setCategory(Category.Category_1);
		
		CommentRepository cr = new CommentRepository();
		try {
			cr.updateComment(cmt);
		} catch (ResourceNotExistException | NoContentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
