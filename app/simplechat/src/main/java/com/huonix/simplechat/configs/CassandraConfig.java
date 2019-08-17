package com.huonix.simplechat.configs;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

/**
 * 
 * @author Kepler Vital
 *
 */
@Configuration
@PropertySource(value = { "classpath:application.properties" })
@EnableCassandraRepositories(basePackages = {"com.huonix.simplechat.repositories"})
@Profile(value = {"default"})
public class CassandraConfig extends AbstractCassandraConfiguration {

	private static final Log LOGGER = LogFactory.getLog(CassandraConfig.class);

	@Autowired
	private Environment environment;

	@Override
	protected String getKeyspaceName() {
		return environment.getProperty("spring.cassandra.keyspace");
	}

	@Override
	protected String getContactPoints() {
		return environment.getProperty("spring.cassandra.contactpoints");
	}

	@Override
	protected int getPort() {
		return Integer.parseInt(environment.getProperty("spring.cassandra.port"));
	}
	
	@Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }
	
	/*
     * Automatically creates a Keyspace if it doesn't exist
     */
    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification
                .createKeyspace(this.getKeyspaceName())
                .ifNotExists()
                .with(KeyspaceOption.DURABLE_WRITES, true)
                .withSimpleReplication();
        return Arrays.asList(specification);
    }
	
	@Override
	public String[] getEntityBasePackages() {
	    return new String[]{"com.huonix.simplechat.models"};
	}

	@Override
	@Bean
	public CassandraClusterFactoryBean cluster() {
		final CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
		cluster.setJmxReportingEnabled(false);
		cluster.setContactPoints(this.getContactPoints());
		cluster.setPort(this.getPort());
		cluster.setKeyspaceCreations(getKeyspaceCreations());
		LOGGER.info("Cluster created with contact points [" + this.getContactPoints() + "] " + "& port [" + this.getPort() + "].");
		return cluster;
	}

}