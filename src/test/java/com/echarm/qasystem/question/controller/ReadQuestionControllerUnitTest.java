package com.echarm.qasystem.question.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import com.echarm.qasystem.question.QuestionServiceApplication;
import com.echarm.qasystem.question.controller.ReadQuestionController;
import com.echarm.qasystem.question.error.QuestionNotExistErrorBody;
import com.echarm.qasystem.question.error.CategoryNotExistErrorBody;
import com.echarm.qasystem.question.error.ResourceNotExistException;
import com.echarm.qasystem.question.error.ServerSideProblemException;
import com.echarm.qasystem.question.model.Question;
import com.echarm.qasystem.question.repository.QuestionRepositoryService;
import com.echarm.qasystem.question.repository.QuestionSpecification;
import com.echarm.qasystem.question.util.Category;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QuestionServiceApplication.class)
@WebAppConfiguration
public class ReadQuestionControllerUnitTest {
    @Mock
    private QuestionRepositoryService repository;

    @InjectMocks
    private ReadQuestionController controller;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /*
     * Scenario: The repository returns a list with N elements (wrong action).
     *     Branch I:  The input category exists.
     *     Branch II: The input category doesn't exist.
     *
     * I:  Check if ServerSideException is thrown with a message about the list size
     * II: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     */
    @Test
    public void testNList() throws Exception {
    	setRepository(QuestionTestObject.getQuestionTestList(QuestionTestObject.QuestionListType.N_ELEMENTS));

    	/*
    	 * Branch I
    	 */

    	testServerSideProblem("List<Question> size should be 1");

    	/*
    	 * Branch II
    	 */

    	testCategoryNotExist();
    }

    /*
	 * Scenario: The repository returns a list with 1 question
	 *     Branch I:  The input category exists.
     *     Branch II: The input category doesn't exist.
     *
     * I:  Check if the response status code is 200 and the attached json object contains
     *     the original Question object
     * II: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
	 */
    @Test
    public void testOneList() throws Exception {
    	List<Question> questionList = QuestionTestObject.getQuestionTestList(QuestionTestObject.QuestionListType.ONE_ELEMENT);
    	setRepository(questionList);

    	/*
	     * Branch I
	     */

	    // HttpStatus = 200
        Category[] category = Category.values();
        MvcResult result = this.mockMvc.perform(get("/questions/" + category[((int)(Math.random() * category.length)) % category.length] +
        				                            "/Question_Id")).andExpect(status().isOk()).andReturn();

        // check json object
        assertEquals(QuestionTestObject.generateJsonString(questionList.get(0)), result.getResponse().getContentAsString());

        /*
         * Branch II
         */

        testCategoryNotExist();
    }

    /*
     * Scenario: Scenario: The repository returns an empty list (wrong action)
     *     Branch I:  The input category exists.
     *     Branch II: The input category doesn't exist.
     *
     * I:  Check if the controller notice this and throw a ServerSideProblemException
     *     with message about the list size
     * II: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     */
	@Test
	public void testEmptyList() throws Exception {
	    setRepository(QuestionTestObject.getQuestionTestList(QuestionTestObject.QuestionListType.ZERO_ELEMENT));

	    /*
         * Branch I
         */

	    testServerSideProblem("List<Question> size should be 1");

	    /*
         * Branch II
         */

	    testCategoryNotExist();
	}

	/*
     * Scenario: Scenario: The repository returns an null (wrong action)
     *     Branch I:  The input category exists.
     *     Branch II: The input category doesn't exist.
     *
     * I:  Check if the controller notice this and throw a ServerSideProblemException
     *     with message about the list size
     * II: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     */
	@Test
    public void testNullList() throws Exception {
	    setRepository(QuestionTestObject.getQuestionTestList(QuestionTestObject.QuestionListType.NULL));

	    /*
         * Branch I
         */

        testServerSideProblem("List<Question> should not be null");

        /*
         * Branch II
         */

        testCategoryNotExist();
	}

	/*
     * Scenario: The question with specified id doesn't exist => the repository throws
     *           ResourceNotExistException with a QuestionNotExistErrorBody
     *     Branch I:  The input category exists.
     *     Branch II: The input category doesn't exist.
     *
     * I:  Check if ResourceNotExistException is thrown with a QuestionNotExistErrorBody
     * II: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     *
     */
    @Test
    public void testQuestionNotExist() throws Exception {
    	ResourceNotExistException exceptionByRepository = new ResourceNotExistException("Question Not Exist");
    	exceptionByRepository.setErrorBody(new QuestionNotExistErrorBody("Question_Id"));
        Mockito.when(repository.query(any(QuestionSpecification.class))).thenThrow(exceptionByRepository);

        /*
         * Branch I
         */

        Category[] category = Category.values();
        Throwable exception = null;

        try {
            this.mockMvc.perform(get("/questions/" + category[((int)(Math.random() * category.length)) % category.length] +
            						 "/Question_Id")).andReturn();
        } catch (NestedServletException e) {
            exception = e.getRootCause();
        } finally {
            assertNotNull(exception);
            assertTrue(exception instanceof ResourceNotExistException);
            assertTrue(((ResourceNotExistException) exception).getErrorBody() instanceof QuestionNotExistErrorBody);
        }

        /*
         * Branch II
         */

        testCategoryNotExist();
    }

    public void testServerSideProblem(String msg) throws Exception {
	    Throwable exception = null;
        Category[] category = Category.values();

        try {
            this.mockMvc.perform(get("/articles/" + category[((int)(Math.random() * category.length)) % category.length] +
            						 "/Question_Id")).andReturn();
        } catch (NestedServletException e) {
            exception = e.getRootCause();
        } finally {
            assertNotNull(exception);
            assertTrue(exception instanceof ServerSideProblemException);
            assertEquals(((ServerSideProblemException) exception).getMessage(), msg);
        }
	}

	public void testCategoryNotExist() throws Exception {
	    Throwable exception = null;

        try {
            this.mockMvc.perform(get("/questions/Incorrect_Category/Question_Id")).andReturn();
        } catch (NestedServletException e) {
            exception = e.getRootCause();
        } finally {
            assertNotNull(exception);
            assertTrue(exception instanceof ResourceNotExistException);
            assertTrue(((ResourceNotExistException) exception).getErrorBody() instanceof CategoryNotExistErrorBody);
        }
	}

	public void setRepository(List<Question> questionList) throws Exception{
        Mockito.when(repository.query(any(QuestionSpecification.class))).thenReturn(questionList);
    }
}
