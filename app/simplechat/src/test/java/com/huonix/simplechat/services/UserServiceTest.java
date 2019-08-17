package com.huonix.simplechat.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.datastax.driver.core.utils.UUIDs;
import com.huonix.simplechat.configs.CassandraTestExecutionListener;
import com.huonix.simplechat.configs.EmbeddedCassandraConfig;
import com.huonix.simplechat.configs.EmbeddedRabbitConfig;
import com.huonix.simplechat.configs.EmbeddedRedisConfig;
import com.huonix.simplechat.models.User;
import com.huonix.simplechat.models.UserByApiKey;
import com.huonix.simplechat.models.UserByName;
import com.huonix.simplechat.repositories.UserByApiKeyRepository;
import com.huonix.simplechat.repositories.UserByNameRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		EmbeddedCassandraConfig.class, 
		EmbeddedRedisConfig.class,
		EmbeddedRabbitConfig.class
}, initializers = ConfigFileApplicationContextInitializer.class)
@TestExecutionListeners({ 
	CassandraTestExecutionListener.class,
	DependencyInjectionTestExecutionListener.class 
})
@SpringBootTest
@ActiveProfiles(profiles = {"test"})
public class UserServiceTest {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserByApiKeyRepository userByApiKeyRepository;
	
	@Autowired
	private UserByNameRepository userByNameRepository;
	
	@Test
    public void testFindAllEmpty() {
		List<User> users = userService.findAll();
		assertTrue("The users list should be empty", users.isEmpty());
    }
	
	@Test
    public void testFindAllNotEmpty() {
		userService.add(new User(UUIDs.timeBased(), true, "John Doe1"));
		userService.add(new User(UUIDs.timeBased(), true, "John Doe2"));
		List<User> users = userService.findAll();
		assertEquals("The users list should be 2", 2, users.size());
    }
	
	@Test
    public void testGetByIdNull() {
		UUID id = UUIDs.timeBased();
		Optional<User> user = userService.getById(id);
		assertTrue("User should not exists for id" + id, !user.isPresent());
    }
	
	@Test
    public void testExistsByIdFalse() {
		UUID id = UUIDs.timeBased();
		assertFalse("User should not exists for id" + id, userService.existsById(id));
    }
	
	@Test
    public void testCountEmpty() {
		assertEquals("User count should be 0", 0, userService.count());
    }
	
	@Test
    public void testAddNew() {
		User user = new User(UUIDs.timeBased(), true, "John Doe");
		assertEquals("User should be added successfully", user, userService.add(user));
    }
	
	@Test
    public void testAddNotUnique() {
		User user = new User(UUIDs.timeBased(), true, "John Doe");
		userService.add(user);
		userService.add(user);
		assertTrue("User can't have the same name as a previous inserted user=" + user.getName(), !userService.getErrors().isEmpty());
    }
	
	@Test
    public void testUpdateSuccess() {
		String mood = "I love simplechat!";
		User user = new User(UUIDs.timeBased(), true, "John Doe");
		user = userService.add(user);
		assertTrue("User with id " + user.getId() + "should exist", userService.existsById(user.getId()));
		user.setMood(mood);
		user = userService.update(user.getId(), user);
		assertEquals("The user mood should be:" + mood, mood, user.getMood());
    }
	
	@Test
    public void testUpdateWithoutDuplication() {
		List<User> users = new ArrayList<>();
		String name = "John Doe 2";
		User user = new User(UUIDs.timeBased(), true, "John Doe");
		user = userService.add(user);
		assertTrue("User with id " + user.getId() + "should exist", userService.existsById(user.getId()));
		user.setName(name);
		user = userService.update(user.getId(), user);
		final User u = user;
		userService.findAll().forEach(e->{
			if(e.getId().equals(u.getId())) {
				users.add(e);
			}
		});
		assertEquals("The user count should be 1:", 1, users.size());
    }
	
	@Test
    public void testUpdateWhereNotExists() {
		User user = new User(UUIDs.timeBased(), true, "John Doe");
		user = userService.update(user.getId(), user);
		assertNull("The user update should return null when not exists", user);
    }
	
	@Test
    public void testUpdateWhereNameBelongsToOther() {
		String name = "John Doe";
		User user = new User(UUIDs.timeBased(), true, "John Doe 2");
		userService.add(user);
		userService.add(new User(UUIDs.timeBased(), true, name));
		user.setName(name);		
		user = userService.update(user.getId(), user);
		assertNull("The user update should return null when other user has the name", user);
	}
	
	@Test
    public void testUpdateWhereNameBelongsToMe() {
		String name = "John Doe";
		User user = new User(UUIDs.timeBased(), true, "John Doe 2");
		userService.add(user);
		user = userService.update(user.getId(), user);		
		assertTrue(
				"Should be true because the user name was from the same user.", 
				(!name.isEmpty() && !user.getName().equals(name))
		);
	}
	
	@Test
    public void testDeleteByIdSuccess() {
		User user = new User(UUIDs.timeBased(), true, "John Doe");
		userService.add(user);
		assertTrue(
				"Should delete the user by it's id", 
				userService.deleteById(user.getId())
		);
	}
	
	@Test
    public void testDeleteByIdUserNotFound() {
		User user = new User(UUIDs.timeBased(), true, "John Doe");
		assertFalse(
				"Should return false when user not found.", 
				userService.deleteById(user.getId())
		);
	}
	
	@Test
    public void testDeleteByIdCheckApiKey() {
		User user = new User(UUIDs.timeBased(), true, "John Doe");
		userService.add(user);
		Optional<UserByApiKey> userByApiKey = userByApiKeyRepository.findByKey(user.getAccessKey());
		assertTrue(
				"Should return the user api key.", 
				userByApiKey.isPresent()
		);
	}
	
	@Test
    public void testDeleteByIdCheckUserName() {
		User user = new User(UUIDs.timeBased(), true, "John Doe");
		userService.add(user);
		Optional<UserByName> userByName = userByNameRepository.findByName(user.getName());
		assertTrue(
				"Should return the user name.", 
				userByName.isPresent()
		);
	}
	
	@Test
    public void testDeleteSuccess() {
		User user = new User(UUIDs.timeBased(), true, "John Doe");
		userService.add(user);
		assertTrue(
				"Should return the true when deleted.", 
				userService.delete(user)
		);
	}
	
	
}
