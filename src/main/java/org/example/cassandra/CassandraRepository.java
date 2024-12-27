package org.example.cassandra;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;

import java.net.InetSocketAddress;

public class CassandraRepository {
    private static CqlSession session;
    public void initSession() {
        this.session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                .addContactPoint(new InetSocketAddress("cassandra2", 9043))
                .withLocalDatacenter("dc1")
                .withAuthCredentials("cassandra", "cassandrapassword")
                //.withKeyspace(CqlIdentifier.fromCql("rent_a_room"))
                .build();

        createKeyspaceIfNotExists();
        session.close();

        this.session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                .addContactPoint(new InetSocketAddress("cassandra2", 9043))
                .withLocalDatacenter("dc1")
                .withKeyspace(CqlIdentifier.fromCql("rent_a_car"))
                .build();
    }

    private void createKeyspaceIfNotExists() {
        SimpleStatement keyspaceStatement = SchemaBuilder.createKeyspace(CqlIdentifier.fromCql("rent_a_room"))
                .ifNotExists()
                .withSimpleStrategy(2)
                .withDurableWrites(true)
                .build();

        session.execute(keyspaceStatement);
    }
    public CqlSession getSession() {
        return session;
    }

    public void close() {
        session.close();
    }
}
