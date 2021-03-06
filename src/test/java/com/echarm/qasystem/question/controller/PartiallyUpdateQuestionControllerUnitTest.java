package com.echarm.qasystem.question.controller;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import com.echarm.qasystem.question.controller.PartiallyUpdateQuestionController;
import com.echarm.qasystem.question.error.QuestionNotExistErrorBody;
import com.echarm.qasystem.question.error.CategoryNotExistErrorBody;
import com.echarm.qasystem.question.error.EmptyParameterErrorBody;
import com.echarm.qasystem.question.error.InvalidParameterException;
import com.echarm.qasystem.question.error.MissingParameterErrorBody;
import com.echarm.qasystem.question.error.ResourceNotExistException;
import com.echarm.qasystem.question.error.ServerSideProblemException;
import com.echarm.qasystem.question.model.Question;
import com.echarm.qasystem.question.repository.QuestionRepositoryService;
import com.echarm.qasystem.question.util.Category;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QuestionServiceApplication.class)
@WebAppConfiguration
public class PartiallyUpdateQuestionControllerUnitTest {
    @Mock
    private QuestionRepositoryService repository;

    @InjectMocks
    private PartiallyUpdateQuestionController controller;

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
     * Scenario: The repository returns an question object with all attributes non-null
     *           , excluding category.
     *     Branch I:  The input category exists.
     *         case 1: HTTP request contains a filled question object (json body)
     *         case 2: HTTP request contains an question object that contains one null attribute (json body)
     *         case 3: HTTP request contains an question object with all attributes null (json body)
     *         case 4: HTTP request contains no object (json body)
     *     Branch II: The input category doesn't exist.
     *         case 1: HTTP request contains a filled question object (json body)
     *         case 2: HTTP request contains an question object that contains one null attribute (json body)
     *         case 3: HTTP request contains an question object with all attributes null (json body)
     *         case 4: HTTP request contains no object (json body)
     *
     * I-1:  Check if the response status code is 200 and the attached json object contains
     *       the original question object
     * I-2:  Check if the response status code is 200 and the attached json object contains
     *       the original question object
     * I-3:  Check if InvalidParameterException is thrown with an EmptyParameterErrorBody
     * I-4:  Check if InvalidParameterException is thrown with an MissingParameterErrorBody
     * II-1: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     * II-2: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     * II-3: Check if InvalidParameterException is thrown with an EmptyParameterErrorBody
     * II-4: Check if InvalidParameterException is thrown with an MissingParameterErrorBody
     */
    @Test
    public void testFilledQuestionFromRepository() throws Exception {
        // repository will return a filled question
    	setRepository(questionArr[0]);
    	Category[] categoryArr = Category.values();
    	Category category = categoryArr[((int)(Math.random() * categoryArr.length)) % categoryArr.length];
    	questionArr[0].setCategory(category);



        /*
         * Branch I-1 and I-2
         */

        for (int i = 0; i < 2; i++) {
        	// check status == 200
        	MvcResult result = this.mockMvc.perform(patch("/questions/" + category.name() +
    				           "/Question_Id").contentType(MediaType.APPLICATION_JSON)
    				           .content(jsonQuestionArr[0])).andExpect(status().isOk()).andReturn();
        	//check json object
            assertEquals(QuestionTestObject.generateJsonString(questionArr[0]), result.getResponse().getContentAsString());
        }

        /*
         * Branch I-3 to II-4
         */

        testBranchI3toII4();
    }

    /*
     * Scenario: The repository returns an question object with all attributes non-null
     *           except for one random attribute, excluding category.(wrong action)
     *     Branch I:  The input category exists.
     *         case 1: HTTP request contains a filled question object (json body)
     *         case 2: HTTP request contains an question object that contains one null attribute (json body)
     *         case 3: HTTP request contains an question object with all attributes null (json body)
     *         case 4: HTTP request contains no object (json body)
     *     Branch II: The input category doesn't exist.
     *         case 1: HTTP request contains a filled question object (json body)
     *         case 2: HTTP request contains an question object that contains one null attribute (json body)
     *         case 3: HTTP request contains an question object with all attributes null (json body)
     *         case 4: HTTP request contains no object (json body)
     *
     * I-1:  Check if ServerSideProblemException is thrown with the message about question's
     *       field being null
     * I-2:  Check if ServerSideProblemException is thrown with the message about question's
     *       field being null
     * I-3:  Check if InvalidParameterException is thrown with an EmptyParameterErrorBody
     * I-4:  Check if InvalidParameterException is thrown with an MissingParameterErrorBody
     * II-1: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     * II-2: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     * II-3: Check if InvalidParameterException is thrown with an EmptyParameterErrorBody
     * II-4: Check if InvalidParameterException is thrown with an MissingParameterErrorBody
     */
    @Test
    public void testMissingOneAttributeQuestionFromRepository() throws Exception {
        // repository will return a question with one random attribute null and others non-null
        setRepository(questionArr[1]);

        /*
         * Branch I-1 and I-2
         */

        for (int i = 0; i < 2; i++)
            testServerSideProblem("Question field should not be null", jsonQuestionArr[i]);

        /*
         * Branch I-3 to II-4
         */

        testBranchI3toII4();
    }

    /*
     * Scenario: The repository returns an question object with all attributes null, excluding category.
     *           (wrong action)
     *     Branch I:  The input category exists.
     *         case 1: HTTP request contains a filled question object (json body)
     *         case 2: HTTP request contains an question object that contains one null attribute (json body)
     *         case 3: HTTP request contains an question object with all attributes null (json body)
     *         case 4: HTTP request contains no object (json body)
     *     Branch II: The input category doesn't exist.
     *         case 1: HTTP request contains a filled question object (json body)
     *         case 2: HTTP request contains an question object that contains one null attribute (json body)
     *         case 3: HTTP request contains an question object with all attributes null (json body)
     *         case 4: HTTP request contains no object (json body)
     *
     * I-1:  Check if ServerSideProblemException is thrown with a message about null question field
     * I-2:  Check if ServerSideProblemException is thrown with a message about null question field
     * I-3:  Check if InvalidParameterException is thrown with an EmptyParameterErrorBody
     * I-4:  Check if InvalidParameterException is thrown with an MissingParameterErrorBody
     * II-1: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     * II-2: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     * II-3: Check if InvalidParameterException is thrown with an EmptyParameterErrorBody
     * II-4: Check if InvalidParameterException is thrown with an MissingParameterErrorBody
     */
    @Test
    public void testEmptyQuestionFromRepository() throws Exception {
        // repository will return a question with all attributes null
        setRepository(questionArr[2]);

        /*
         * Branch I-1 and I-2
         */

        for (int i = 0; i < 2; i++)
            testServerSideProblem("Question field should not be null", jsonQuestionArr[i]);

        /*
         * Branch I-3 to II-4
         */

        testBranchI3toII4();
    }

    /*
     * Scenario: The repository returns null (wrong action).
     *     Branch I:  The input category exists.
     *         case 1: HTTP request contains a filled question object (json body)
     *         case 2: HTTP request contains an question object that contains one null attribute (json body)
     *         case 3: HTTP request contains an question object with all attributes null (json body)
     *         case 4: HTTP request contains no object (json body)
     *     Branch II: The input category doesn't exist.
     *         case 1: HTTP request contains a filled question object (json body)
     *         case 2: HTTP request contains an question object that contains one null attribute (json body)
     *         case 3: HTTP request contains an question object with all attributes null (json body)
     *         case 4: HTTP request contains no object (json body)
     *
     * I-1:  Check if ServerSideProblemException is thrown with a message about null question object
     * I-2:  Check if ServerSideProblemException is thrown with a message about null question object
     * I-3:  Check if InvalidParameterException is thrown with an EmptyParameterErrorBody
     * I-4:  Check if InvalidParameterException is thrown with an MissingParameterErrorBody
     * II-1: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     * II-2: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     * II-3: Check if InvalidParameterException is thrown with an EmptyParameterErrorBody
     * II-4: Check if InvalidParameterException is thrown with an MissingParameterErrorBody
     */
    @Test
    public void testNullQuestionFromRepository() throws Exception {
        // repository will return null
        setRepository(questionArr[3]);

        /*
         * Branch I-1 and I-2
         */

        for (int i = 0; i < 2; i++)
            testServerSideProblem("Question object should not be null", jsonQuestionArr[i]);

        /*
         * Branch I-3 to II-4
         */

        testBranchI3toII4();
    }

    /*
     * Scenario: The question of specified id doesn't exist in the database. Therefore, the repository returns
     *           ResourceNotExistException with QuestionNotExistErrorBody
     *     Branch I:  The input category exists.
     *         case 1: HTTP request contains a filled question object (json body)
     *         case 2: HTTP request contains an question object that contains one null attribute (json body)
     *         case 3: HTTP request contains an question object with all attributes null (json body)
     *         case 4: HTTP request contains no object (json body)
     *     Branch II: The input category doesn't exist.
     *         case 1: HTTP request contains a filled question object (json body)
     *         case 2: HTTP request contains an question object that contains one null attribute (json body)
     *         case 3: HTTP request contains an question object with all attributes null (json body)
     *         case 4: HTTP request contains no object (json body)
     *
     * I-1:  Check if ResourceNotExistException is thrown with questionNotExistErrorBody
     * I-2:  Check if ResourceNotExistException is thrown with questionNotExistErrorBody
     * I-3:  Check if InvalidParameterException is thrown with an EmptyParameterErrorBody
     * I-4:  Check if InvalidParameterException is thrown with an MissingParameterErrorBody
     * II-1: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     * II-2: Check if ResourceNotExistException is thrown with a CategoryNotExistErrorBody
     * II-3: Check if InvalidParameterException is thrown with an EmptyParameterErrorBody
     * II-4: Check if InvalidParameterException is thrown with an MissingParameterErrorBody
     */
    @Test
    public void testQuestionNotExistFromRepository() throws Exception {
        // repository will throw ResourceNotExistException is thrown with QuestionNotExistErrorBody
        QuestionNotExistErrorBody body = new QuestionNotExistErrorBody("Question_Id");
        ResourceNotExistException exception = new ResourceNotExistException("Question Not Exist");
        exception.setErrorBody(body);
        Mockito.when(repository.updateQuestion(any(Question.class))).thenThrow(exception);

        /*
         * Branch I-1 and I-2
         */

        for (int i = 0; i < 2; i++)
            testQuestionNotExist(jsonQuestionArr[i]);

        /*
         * Branch I-3 to II-4
         */

        testBranchI3toII4();
    }

    public void testBranchI3toII4() throws Exception{

        /*
         * Branch I-3
         */

        testInvalidParameter(jsonQuestionArr[2], false, true);

        /*
         * Branch I-4
         */

        testInvalidParameter(jsonQuestionArr[3], true, true);

        /*
         * Branch II-1
         */

        testCategoryNotExist(jsonQuestionArr[0]);

        /*
         * Branch II-2
         */

        testCategoryNotExist(jsonQuestionArr[1]);

        /*
         * Branch II-3
         */

        testInvalidParameter(jsonQuestionArr[2], false, false);

        /*
         * Branch II-4
         */

        testInvalidParameter(jsonQuestionArr[3], true, false);
    }

    public void testServerSideProblem(String msg, String jsonQuestion) throws Exception {
        Throwable exception = null;
        Category[] category = Category.values();

        try {
            this.mockMvc.perform(patch("/questions/" + category[((int)(Math.random() * category.length)) % category.length] +
                               "/Question_Id").contentType(MediaType.APPLICATION_JSON)
                               .content(jsonQuestion)).andReturn();
        } catch (NestedServletException e) {
            exception = e.getRootCause();
        } finally {
            assertNotNull(exception);
            assertTrue(exception instanceof ServerSideProblemException);
            assertEquals(((ServerSideProblemException) exception).getMessage(), msg);
        }
    }

    public void testQuestionNotExist(String jsonQuestion) throws Exception {
        Throwable exception = null;
        Category[] category = Category.values();

        try {
            this.mockMvc.perform(patch("/questions/" + category[((int)(Math.random() * category.length)) % category.length] +
                     "/Question_Id").contentType(MediaType.APPLICATION_JSON)
                       .content(jsonQuestion)).andReturn();
        } catch (NestedServletException e) {
            exception = e.getRootCause();
        } finally {
            assertNotNull(exception);
            assertTrue(exception instanceof ResourceNotExistException);
            assertTrue(((ResourceNotExistException) exception).getErrorBody() instanceof QuestionNotExistErrorBody);
        }
    }

    public void testCategoryNotExist(String jsonQuestion) throws Exception {
        Throwable exception = null;

        try {
            this.mockMvc.perform(patch("/questions/Incorrect_Category/Question_Id").contentType(MediaType.APPLICATION_JSON)
                       .content(jsonQuestion)).andReturn();
        } catch (NestedServletException e) {
            exception = e.getRootCause();
        } finally {
            assertNotNull(exception);
            assertTrue(exception instanceof ResourceNotExistException);
            assertTrue(((ResourceNotExistException) exception).getErrorBody() instanceof CategoryNotExistErrorBody);
        }
    }

    public void testInvalidParameter(String jsonQuestion, boolean isNull, boolean isCategoryExisted) throws Exception {
        Throwable exception = null;
        Category[] category = Category.values();

        String categoryStr;

        if (isCategoryExisted)
            categoryStr = category[((int)(Math.random() * category.length)) % category.length].name();
        else
            categoryStr = "INCORRECT_CATEGORY";

        try {
            if (!isNull) {
                this.mockMvc.perform(patch("/questions/" + categoryStr +
                     "/Question_Id").contentType(MediaType.APPLICATION_JSON)
                       .content(jsonQuestion)).andReturn();
            }
            else {
                this.mockMvc.perform(patch("/questions/" + categoryStr +
                         "/Question_Id")).andReturn();
            }

        } catch (NestedServletException e) {
            exception = e.getRootCause();
        } finally {
            assertNotNull(exception);
            assertTrue(exception instanceof InvalidParameterException);
            if (isNull)
                assertTrue(((InvalidParameterException) exception).getErrorBody() instanceof MissingParameterErrorBody);
            else
                assertTrue(((InvalidParameterException) exception).getErrorBody() instanceof EmptyParameterErrorBody);
        }
    }

    public void setRepository(Question question) throws Exception{
        Mockito.when(repository.updateQuestion(any(Question.class))).thenReturn(question);
    }
}
