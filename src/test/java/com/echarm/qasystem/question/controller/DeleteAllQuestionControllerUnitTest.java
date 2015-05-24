package com.echarm.qasystem.question.controller;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import com.echarm.qasystem.question.controller.DeleteAllQuestionController;
import com.echarm.qasystem.question.error.NoContentException;
import com.echarm.qasystem.question.error.ServerSideProblemException;
import com.echarm.qasystem.question.model.Question;
import com.echarm.qasystem.question.repository.QuestionRepositoryService;
import com.echarm.qasystem.question.repository.QuestionSpecification;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QuestionServiceApplication.class)
@WebAppConfiguration
public class DeleteAllQuestionControllerUnitTest {
    @Mock
    private QuestionRepositoryService repository;

    @InjectMocks
    private DeleteAllQuestionController controller;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /*
     * Scenario: There are N questions in the database.
     *
     * Check if the response status code is 200 and the attached json object contains
     * the original Question objects
     */
    @Test
    public void testNList() throws Exception {
        List<Question> questionList = QuestionTestObject.getQuestionTestList(QuestionTestObject.QuestionListType.N_ELEMENTS);
        setRepository(questionList);

        // HttpStatus = 200
        MvcResult result = this.mockMvc.perform(delete("/questions")).andExpect(status().isOk()).andReturn();

        // Check json object
        assertEquals(result.getResponse().getContentAsString(), QuestionTestObject.generateListJsonString(questionList));
    }

    /*
     * Scenario: There are 1 question in the database.
     *
     * Check if the response status code is 200 and the attached json object contains
     * the original Question object
     */
    @Test
    public void testOneList() throws Exception {
        List<Question> questionList = QuestionTestObject.getQuestionTestList(QuestionTestObject.QuestionListType.ONE_ELEMENT);
        setRepository(questionList);

        // HttpStatus = 200
        MvcResult result = this.mockMvc.perform(delete("/questions")).andExpect(status().isOk()).andReturn();

        // check json object
        assertEquals(result.getResponse().getContentAsString(), QuestionTestObject.generateListJsonString(questionList));
    }

    /*
     * Scenario: The repository returns an empty list (wrong action)
     *
     * Check if the controller notice this and throw a ServerSideProblemException
     * with message about the list size
     */
    @Test
    public void testEmptyList() throws Exception {
        setRepository(QuestionTestObject.getQuestionTestList(QuestionTestObject.QuestionListType.ZERO_ELEMENT));

//        testServerSideProblem("List<question> size should be at least 1");
    }

    /*
     * Scenario: The repository returns an null (wrong action)
     *
     * Check if the controller notice this and throw a ServerSideProblemException
     * with message about the null list
     */
    @Test
    public void testNullList() throws Exception {
        setRepository(QuestionTestObject.getQuestionTestList(QuestionTestObject.QuestionListType.NULL));

//        testServerSideProblem("List<question> should not be null");
    }

    /*
     * Scenario: There is no question in the database
     *
     * Check if NoContentException is correctly thrown by the controller
     */
    @Test
    public void testNoContent() throws Exception {
        Mockito.when(repository.query(any(QuestionSpecification.class))).thenThrow(new NoContentException());

        Throwable exception = null;

        try {
            this.mockMvc.perform(delete("/questions")).andReturn();
        } catch (NestedServletException e) {
            exception = e.getRootCause();
        } finally {
            assertNotNull(exception);
            assertTrue(exception instanceof NoContentException);
        }
    }

    public void testServerSideProblem(String msg) throws Exception {
        Throwable exception = null;

        try {
            this.mockMvc.perform(delete("/questions/")).andReturn();
        } catch (NestedServletException e) {
            exception = e.getRootCause();
        } finally {
            assertNotNull(exception);
            assertTrue(exception instanceof ServerSideProblemException);
            assertEquals(((ServerSideProblemException) exception).getMessage(), msg);
        }
    }

    public void setRepository(List<Question> questionList) throws Exception{
        Mockito.when(repository.query(any(QuestionSpecification.class))).thenReturn(questionList);
    }
}
