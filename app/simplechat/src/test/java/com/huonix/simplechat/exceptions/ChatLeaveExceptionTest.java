package com.huonix.simplechat.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles = {"test"})
public class ChatLeaveExceptionTest {

	@Test
    public void testEmptyNotNull() {
		ChatLeaveException exception = new ChatLeaveException();
		assertNotNull(exception);
    }
	
	@Test
    public void testMessageEquals() {
		ChatLeaveException exception = new ChatLeaveException("message");
		assertEquals("message", exception.getMessage());
    }
	
}
