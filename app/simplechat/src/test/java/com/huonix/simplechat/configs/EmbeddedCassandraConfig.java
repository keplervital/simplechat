package com.huonix.simplechat.configs;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.datastax.driver.core.Session;
import com.huonix.simplechat.containers.CassandraContainer;

/**
 * 
 * @author Kepler Vital
 *
 */
@TestConfiguration
@ComponentScan(basePackages={"com.huonix.simplechat.containers"})
@PropertySource(value = { "classpath:application.properties" })
@EnableCassandraRepositories(basePackages = "com.huonix.simplechat.repositories")
@Profile(value = {"test"})
public class EmbeddedCassandraConfig extends AbstractCassandraConfiguration {

	private static final Log LOGGER = LogFactory.getLog(EmbeddedCassandraConfig.class);
	
	@Autowired
	private Environment environment;
	
	@Autowired
	CassandraContainer container;

	@Override
	protected String getKeyspaceName() {
		return environment.getProperty("test.cassandra.keyspace");
	}
	
	@Override
	protected String getContactPoints() {
		return environment.getProperty("test.cassandra.contactpoints");
	}

	@Override
	protected int getPort() {
		return container.getPort();
	}
	
	@Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.RECREATE_DROP_UNUSED;
    }
	
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
	
	@Override
	@Bean
	public CassandraClusterFactoryBean cluster() {
		this.startContainerServer();
		final CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
		cluster.setJmxReportingEnabled(false);
		cluster.setContactPoints(this.getContactPoints());
		cluster.setPort(this.getPort());
		cluster.setKeyspaceCreations(getKeyspaceCreations());
		LOGGER.info("Test cluster created with contact points [" + this.getContactPoints() + "] " + "& port [" + this.getPort() + "].");
		return cluster;
	}
	
	@Bean
    public TestApplicationContext testApplicationContext() {
        return new TestApplicationContext();
    }
	
	@Bean
    public Session dbSession() throws Exception {
		return this.getRequiredSession();
    }
	
	@Bean
    public String dbKeyspace() {
		return this.getKeyspaceName();
    }

}