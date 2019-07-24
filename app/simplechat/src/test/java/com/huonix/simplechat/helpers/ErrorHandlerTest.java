package com.huonix.simplechat.helpers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class ErrorHandlerTest {
	
	public class ConcreteErrorHandler extends ErrorHandler {}

	@Test
    public void testExistsErrorsNotEmpty() {
		ConcreteErrorHandler errors = new ConcreteErrorHandler();
		errors.addError("1")
			  .addError("2");
		assertTrue("Errors should not be empty", errors.existsErrors());
    }
	
	@Test
    public void testExistsErrorsEmpty() {
		ConcreteErrorHandler errors = new ConcreteErrorHandler();
		assertFalse("Errors should be empty", errors.existsErrors());
    }
	
}
