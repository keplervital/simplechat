package com.huonix.simplechat.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class ChatCreationErrorExceptionTest {

	@Test
    public void testEmptyNotNull() {
		ChatCreationErrorException exception = new ChatCreationErrorException();
		assertNotNull(exception);
    }
	
	@Test
    public void testMessageEquals() {
		ChatCreationErrorException exception = new ChatCreationErrorException("message");
		assertEquals("message", exception.getMessage());
    }
	
}
