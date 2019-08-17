package com.huonix.simplechat.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huonix.simplechat.containers.CassandraContainer;
import com.huonix.simplechat.containers.RabbitMQContainer;
import com.huonix.simplechat.containers.RedisContainer;

/**
 * Removes running containers
 * 
 * @author kepler vital
 *
 */
public class DockerContainer {

	private static final Logger LOGGER = LoggerFactory.getLogger(DockerContainer.class);
	private static final int SLEEP_TIMER = 3000;
	
	/**
	 * Remove cassandra container
	 * 
	 * @return void
	 */
	public static void removeCassandra() {
		try {
			((CassandraContainer) TestApplicationContext.getBean("cassandraContainer")).removeServer();
		} catch(Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	/**
	 * Remove rabbitmq container
	 * 
	 * @return void
	 */
	public static void removeRabbitMQ() {
		try {
			((RabbitMQContainer) TestApplicationContext.getBean("rabbitMQContainer")).removeServer();
		} catch(Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	/**
	 * Remove redis container
	 * 
	 * @return void
	 */
	public static void removeRedis() {
		try {
			((RedisContainer) TestApplicationContext.getBean("redisContainer")).removeServer();
		} catch(Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	/**
	 * Allow docker to stop containers
	 * 
	 * @return void
	 */
	public static void allowDockerToStopContainers() {
		try {
			Thread.sleep(SLEEP_TIMER);
		} catch(Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	/**
	 * Remove all running containers
	 * 
	 * @return void
	 */
	public static void removeAll() {
		removeCassandra();
		removeRabbitMQ();
		removeRedis();
		allowDockerToStopContainers();
	}
	
}
