package com.huonix.simplechat.configs;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
@PropertySource(name="application", value = "classpath:application.properties")
@EnableRedisRepositories(basePackages = {"com.huonix.simplechat.redis"})
@Profile(value = {"default"})
public class RedisConfig {

	@Resource
    Environment env;
    
	@Bean
    JedisConnectionFactory jedisConnectionFactory() {
		final String host = env.getProperty("spring.redis.host");
		final int port = Integer.parseInt(env.getProperty("spring.redis.port"));
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
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        return container;
    }
	
}
