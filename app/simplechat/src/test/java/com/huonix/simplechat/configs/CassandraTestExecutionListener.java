package com.huonix.simplechat.configs;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;

public class CassandraTestExecutionListener extends AbstractTestExecutionListener {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraTestExecutionListener.class);
    
    private static List<String> tables;

    @Override
    public void afterTestMethod(final TestContext testContext) throws Exception {
    	String keyspace = (String) TestApplicationContext.getBean("dbKeyspace");
        LOGGER.debug("AfterTest: clean embedded Cassandra.");
        final Session session = (Session) TestApplicationContext.getBean("dbSession");
        for (final String table : tables(session)) {
            LOGGER.debug("Truncating table {}", table);
            session.execute(String.format("TRUNCATE %s.%s;", keyspace, table));
        }
        super.afterTestMethod(testContext);
    }

    public List<String> tables(final Session session) {
    	String keyspace = (String) TestApplicationContext.getBean("dbKeyspace");
        if (tables == null) {
            tables = new ArrayList<>();
            final Cluster cluster = session.getCluster();
            final Metadata meta = cluster.getMetadata();
            final KeyspaceMetadata keyspaceMeta = meta.getKeyspace(keyspace);
            for (final TableMetadata tableMeta : keyspaceMeta.getTables()) {
                tables.add(tableMeta.getName());
            }
        }
        return tables;
    }

}