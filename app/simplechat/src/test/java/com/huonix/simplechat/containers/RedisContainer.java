package com.huonix.simplechat.containers;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;
import org.testcontainers.containers.GenericContainer;

/**
 * This {@link Container} is based on the official Redis ({@code redis}) image from
 * dockerhub. If you need to use a custom Redis
 * image, you can provide the full image name as well.
 *
 * @author Kepler Vital
 */
@Component
public class RedisContainer extends GenericContainer<RedisContainer> {

	/**
     * This is the internal port on which Redis is running inside the container.
     * <p>
     * You can use this constant in case you want to map an explicit public port to it
     * instead of the default random port. This can be done using methods like
     * {@link #setPortBindings(java.util.List)}.
     */
	public static final int REDIS_PORT = 6379;
    public static final String DEFAULT_IMAGE_AND_TAG = "redis:5";
    
    /**
     * Creates a new {@link RedisContainer} with the {@value DEFAULT_IMAGE_AND_TAG} image.
     */
    public RedisContainer() {
        this(DEFAULT_IMAGE_AND_TAG);
    }
    
    /**
     * Creates a new {@link RedisContainer} with the given {@code 'image'}.
     *
     * @param image the image (e.g. {@value DEFAULT_IMAGE_AND_TAG}) to use
     */
    public RedisContainer(@NotNull String image) {
        super(image);
        addExposedPort(REDIS_PORT);
    }
    
    /**
     * Returns the actual public port of the internal Redis port ({@value REDIS_PORT}).
     *
     * @return the public port of this container
     * @see #getMappedPort(int)
     */
    @NotNull
    public Integer getPort() {
        return getMappedPort(REDIS_PORT);
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