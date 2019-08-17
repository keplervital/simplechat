package com.huonix.simplechat.containers;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;
import org.testcontainers.containers.GenericContainer;

/**
 * This {@link Container} is based on the official RabbitMQ ({@code rabbitmq}) image from
 * dockerhub. If you need to use a custom RabbitMQ
 * image, you can provide the full image name as well.
 *
 * @author Kepler Vital
 */
@Component
public class RabbitMQContainer extends GenericContainer<RabbitMQContainer> {

	/**
     * This is the internal port on which RabbitMQ is running inside the container.
     * <p>
     * You can use this constant in case you want to map an explicit public port to it
     * instead of the default random port. This can be done using methods like
     * {@link #setPortBindings(java.util.List)}.
     */
	public static final int RABBBITMQ_PORT = 5672;
    public static final String DEFAULT_IMAGE_AND_TAG = "rabbitmq:3";
    
    /**
     * Creates a new {@link RabbitMQContainer} with the {@value DEFAULT_IMAGE_AND_TAG} image.
     */
    public RabbitMQContainer() {
        this(DEFAULT_IMAGE_AND_TAG);
    }
    
    /**
     * Creates a new {@link RabbitMQContainer} with the given {@code 'image'}.
     *
     * @param image the image (e.g. {@value DEFAULT_IMAGE_AND_TAG}) to use
     */
    public RabbitMQContainer(@NotNull String image) {
        super(image);
        addExposedPort(RABBBITMQ_PORT);
    }
    
    /**
     * Returns the actual public port of the internal RAbbitMQ port ({@value RABBBITMQ_PORT}).
     *
     * @return the public port of this container
     * @see #getMappedPort(int)
     */
    @NotNull
    public Integer getPort() {
        return getMappedPort(RABBBITMQ_PORT);
    }
    
    /**
     * Start the server if it's not already running
     *
     * @return void
     */
    public void startServer() {
    	if(!this.isRunning())
        	this.start();
    }
    
    /**
     * Remove the server if it's running
     *
     * @return void
     */
    public void removeServer() {
        if(this.isRunning()) {
        	this.stop();
        }
    }
	
}
