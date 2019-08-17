package com.huonix.simplechat.configs;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.rabbitmq.client.ConnectionFactory;


@Configuration
@PropertySource(name="application", value = "classpath:application.properties")
@Profile(value = {"default"})
public class RabbitConfig {
	
	@Resource
    Environment env;
	
	@Bean
    CachingConnectionFactory connectionFactory() {
        ConnectionFactory conn = new ConnectionFactory();
        conn.setHost(env.getProperty("spring.rabbitmq.host"));
        conn.setPort(Integer.parseInt(env.getProperty("spring.rabbitmq.port")));
        return new CachingConnectionFactory(conn);
    } 

	@Bean
	RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setRoutingKey(env.getProperty("events.messages.unread"));
		return rabbitTemplate;
	}

}
