package com.huonix.simplechat.configs;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import com.huonix.simplechat.containers.RedisContainer;


@TestConfiguration
@ComponentScan(basePackages={"com.huonix.simplechat.containers"})
@PropertySource(name="application", value = "classpath:application.properties")
@EnableRedisRepositories(basePackages = "com.huonix.simplechat.redis")
@Profile(value = {"test"})
public class EmbeddedRedisConfig {

	private static final Log LOGGER = LogFactory.getLog(EmbeddedRedisConfig.class);
	
	@Resource
    Environment env;
	
	@Autowired
	RedisContainer container;
	
	/**
	 * Starts a container server
	 * 
	 * @return void
	 */
	private void startContainerServer() {
		try {
			container.startServer();
		} catch(Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
    
	@Bean
    JedisConnectionFactory jedisConnectionFactory() {
		this.startContainerServer();
		final String host = env.getProperty("test.redis.host");
		final int port = container.getPort();
		RedisStandaloneConfiguration redisStandaloneConfiguration = 
				new RedisStandaloneConfiguration(host, port);
		return new JedisConnectionFactory(redisStandaloneConfiguration);
    }
	
	@Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        return template;
    }
	
	@Bean
    RedisMessageListenerContainer redisMessageContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        return container;
    }
	
}
