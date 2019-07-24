package com.huonix.simplechat.configs;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.datastax.driver.core.Session;

/**
 * 
 * @author Kepler Vital
 *
 */
@Configuration
@PropertySource(value = { "classpath:application.properties" })
@EnableCassandraRepositories(basePackages = "com.huonix.simplechat.repositories")
public class EmbeddedCassandraConfig extends AbstractCassandraConfiguration {

	private static final Log LOGGER = LogFactory.getLog(CassandraConfig.class);

	@Autowired
	private Environment environment;

	@Override
	protected String getKeyspaceName() {
		return environment.getProperty("cassandra.test.keyspace");
	}
	
	@Override
	protected String getContactPoints() {
		return environment.getProperty("cassandra.test.contactpoints");
	}

	@Override
	protected int getPort() {
		return Integer.parseInt(environment.getProperty("cassandra.test.port"));
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
	 * Starts an embedded cassandra server
	 * 
	 * @return void
	 */
	private void startEmbeddedServer() {
		try {
			EmbeddedCassandraServerHelper.startEmbeddedCassandra();
		} catch(Exception e) {
			//LOGGER.error(e.getMessage());
		}
	}
	
	@Override
	@Bean
	public CassandraClusterFactoryBean cluster() {
		this.startEmbeddedServer();
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