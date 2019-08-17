package com.huonix.simplechat.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles = {"test"})
public class MessageExceptionTest {

	@Test
    public void testEmptyNotNull() {
		MessageException exception = new MessageException();
		assertNotNull(exception);
    }
	
	@Test
    public void testMessageEquals() {
		MessageException exception = new MessageException("message");
		assertEquals("message", exception.getMessage());
    }
	
}
