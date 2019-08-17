package com.huonix.simplechat.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles = {"test"})
public class UnauthorizedAccessExceptionTest {

	@Test
    public void testEmptyNotNull() {
		UnauthorizedAccessException exception = new UnauthorizedAccessException();
		assertNotNull(exception);
    }
	
	@Test
    public void testMessageEquals() {
		UnauthorizedAccessException exception = new UnauthorizedAccessException("message");
		assertEquals("message", exception.getMessage());
    }
	
}
