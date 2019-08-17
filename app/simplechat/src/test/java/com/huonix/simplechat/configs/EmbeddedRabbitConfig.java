package com.huonix.simplechat.configs;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.huonix.simplechat.containers.RabbitMQContainer;
import com.rabbitmq.client.ConnectionFactory;


@TestConfiguration
@ComponentScan(basePackages={"com.huonix.simplechat.containers"})
@PropertySource(name="application", value = "classpath:application.properties")
@Profile(value = {"test"})
public class EmbeddedRabbitConfig {
	
	private static final Log LOGGER = LogFactory.getLog(EmbeddedRabbitConfig.class);
	
	@Resource
    Environment env;
	
	@Autowired
	RabbitMQContainer container;
	
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
    CachingConnectionFactory connectionFactory() {
		this.startContainerServer();
		ConnectionFactory conn = new ConnectionFactory();
        conn.setHost(env.getProperty("test.rabbitmq.host"));
        conn.setPort(container.getPort());
        return new CachingConnectionFactory(conn);
    } 

	@Bean
	RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setRoutingKey(env.getProperty("events.messages.unread"));
		return rabbitTemplate;
	}

}
