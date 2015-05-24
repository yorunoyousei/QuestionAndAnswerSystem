package com.echarm.qasystem.question.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import com.echarm.qasystem.question.QuestionServiceApplication;
import com.echarm.qasystem.question.controller.DeleteQuestionController;
import com.echarm.qasystem.question.error.QuestionNotExistErrorBody;
import com.echarm.qasystem.question.error.CategoryNotExistErrorBody;
import com.echarm.qasystem.question.error.ResourceNotExistException;
import com.echarm.qasystem.question.error.ServerSideProblemException;
import com.echarm.qasystem.question.model.Question;
import com.echarm.qasystem.question.repository.QuestionRepositoryService;
import com.echarm.qasystem.question.util.Category;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QuestionServiceApplication.class)
@WebAppConfiguration
public class DeleteQuestionControllerUnitTest {
    @Mock
    private QuestionRepositoryService repository;

    @InjectMocks
    private DeleteQuestionController controller;

    private MockMvc mockMvc;

    private Question[] questionArr;
    private String[] jsonQuestionArr;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        questionArr = new Question[4];
        questionArr[0] = QuestionTestObject.getTestQuestion(QuestionTestObject.QuestionType.FILLED);
        questionArr[1] = QuestionTestObject.getTestQuestion(QuestionTestObject.QuestionType.MISSING_ONE_ATTRIBUTE);
        questionArr[2] = QuestionTestObject.getTestQuestion(QuestionTestObject.QuestionType.EMPTY);
        questionArr[3] = QuestionTestObject.getTestQuestion(QuestionTestObject.QuestionType.NULL);
        jsonQuestionArr = new String[4];
        for (int i = 0; i < 4; i++) {
            jsonQuestionArr[i] = QuestionTestObject.generateJsonString(questionArr[i]);
        }
    }

    /*
     * Scenario: The repository returns an question with all fields non-null.
     *     Branch I:  The input category exists.
     *     Branch II: The input category doesn't exist.
     *
     * I:  Check if the response status code is 200 and the attached json object contains
     *     the original Question object
     * II: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     */
    @Test
    public void testFilledQuestionFromRepository() throws Exception {
     // repository will return a filled question
        setRepository(questionArr[0]);
        Category[] category = Category.values();

        /*
         * Branch I
         */

        // check status == 200
        MvcResult result = this.mockMvc.perform(delete("/questions/" + category[((int)(Math.random() * category.length)) % category.length] +
                           "/Question_Id").contentType(MediaType.APPLICATION_JSON)
                           .content(jsonQuestionArr[0])).andExpect(status().isOk()).andReturn();
        //check json object
        assertEquals(jsonQuestionArr[0], result.getResponse().getContentAsString());

        /*
         * Branch II
         */

        testCategoryNotExist();
    }

    /*
     * Scenario: The repository returns an question with all fields but one non-null (wrong action).
     *     Branch I:  The input category exists.
     *     Branch II: The input category doesn't exist.
     *
     * I:  Check if ServerSideProblemException is thrown with a message about the null field
     * II: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     */
    @Test
    public void testMissingOneAttributeQuestionFromRepository() throws Exception {
        // repository will return a question with one random attribute null and others non-null
        setRepository(questionArr[1]);

        /*
         * Branch I
         */

        testServerSideProblem("Question field should not be null");

        /*
         * Branch II
         */

        testCategoryNotExist();
    }

    /*
     * Scenario: The repository returns an question with all fields null (wrong action).
     *     Branch I:  The input category exists.
     *     Branch II: The input category doesn't exist.
     *
     * I:  Check if ServerSideProblemException is thrown with a message about the null field
     * II: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     */
    @Test
    public void testEmptyQuestionFromRepository() throws Exception {
        // repository will return a question with all attributes null
        setRepository(questionArr[2]);

        /*
         * Branch I
         */

        testServerSideProblem("Question field should not be null");

        /*
         * Branch II
         */

        testCategoryNotExist();
    }

    /*
     * Scenario: The repository returns a null question (wrong action).
     *     Branch I:  The input category exists.
     *     Branch II: The input category doesn't exist.
     *
     * I:  Check if ServerSideProblemException is thrown with a message about the null object
     * II: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     */
    @Test
    public void testNullQuestionFromRepository() throws Exception {
        // repository will return null
        setRepository(questionArr[3]);

        /*
         * Branch I
         */

        testServerSideProblem("Question object should not be null");

        /*
         * Branch II
         */

        testCategoryNotExist();
    }

    /*
     * Scenario: The question of specified id doesn't exist in the database. Therefore, the repository returns
     *           ResourceNotExistException with QuestionNotExistErrorBody.
     *     Branch I:  The input category exists.
     *     Branch II: The input category doesn't exist.
     *
     * I:  Check if ResourceNotExistException is thrown with a QuestionNotExistErrorBody
     * II: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     */
    @Test
    public void testQuestionNotExistFromRepository() throws Exception {
        // repository will throw ResourceNotExistException is thrown with QuestionNotExistErrorBody
        QuestionNotExistErrorBody body = new QuestionNotExistErrorBody("Question_Id");
        ResourceNotExistException exception = new ResourceNotExistException("Question Not Exist");
        exception.setErrorBody(body);
        Mockito.when(repository.deleteQuestion(any(Question.class))).thenThrow(exception);

        /*
         * Branch I
         */

        testQuestionNotExist();

        /*
         * Branch II
         */

        testCategoryNotExist();
    }

    public void testQuestionNotExist() throws Exception {
        Throwable exception = null;
        Category[] category = Category.values();

        try {
            this.mockMvc.perform(delete("/questions/" + category[((int)(Math.random() * category.length)) % category.length] +
                     "/Question_Id"));
        } catch (NestedServletException e) {
            exception = e.getRootCause();
        } finally {
            assertNotNull(exception);
            assertTrue(exception instanceof ResourceNotExistException);
            assertTrue(((ResourceNotExistException) exception).getErrorBody() instanceof QuestionNotExistErrorBody);
        }
    }

    public void testServerSideProblem(String msg) throws Exception {
        Throwable exception = null;
        Category[] category = Category.values();

        try {
            this.mockMvc.perform(delete("/questions/" + category[((int)(Math.random() * category.length)) % category.length] +
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
            this.mockMvc.perform(delete("/questions/Incorrect_Category/Question_Id")).andReturn();
        } catch (NestedServletException e) {
            exception = e.getRootCause();
        } finally {
            assertNotNull(exception);
            assertTrue(exception instanceof ResourceNotExistException);
            assertTrue(((ResourceNotExistException) exception).getErrorBody() instanceof CategoryNotExistErrorBody);
        }
    }

    public void setRepository(Question question) throws Exception{
        Mockito.when(repository.deleteQuestion(any(Question.class))).thenReturn(question);
    }
}
