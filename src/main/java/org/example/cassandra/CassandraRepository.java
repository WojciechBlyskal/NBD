package org.example.cassandra;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.auth.AuthProvider;
import com.datastax.oss.driver.api.core.auth.ProgrammaticPlainTextAuthProvider;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;

import java.net.InetSocketAddress;

public class CassandraRepository {
    private CqlSession session;
    public CassandraRepository() {
        AuthProvider authProvider = new ProgrammaticPlainTextAuthProvider("cassandra", "cassandra");
        this.session = CqlSession.builder()
                .withAuthProvider(authProvider)
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                .addContactPoint(new InetSocketAddress("cassandra2", 9043))
                .withLocalDatacenter("dc1")
                .withAuthCredentials("cassandra", "cassandra")
                .withKeyspace(CqlIdentifier.fromCql("site"))
                .build();

        createKeyspaceIfNotExists();
        session.close();

        this.session = CqlSession.builder()
                .withAuthProvider(authProvider)
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                .addContactPoint(new InetSocketAddress("cassandra2", 9043))
                .withLocalDatacenter("dc1")
                .withKeyspace(CqlIdentifier.fromCql("site"))
                .build();
    }

    private void createKeyspaceIfNotExists() {
        SimpleStatement keyspaceStatement = SchemaBuilder.createKeyspace(CqlIdentifier.fromCql("site"))
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
