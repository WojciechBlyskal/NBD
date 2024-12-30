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
                .withSimpleStrategy(2) //poziom replikacji, oznacza na ilu wezlach powinny byc przechowaywane dane
                .withDurableWrites(true) //mamy 2 wezly wiec danie poziomu wiekszego od 2 nie ma sensu, a warto miec redundancje
                .build(); //wiec pojedynczy tez odpada, zatem droga eliminacji wybieramy 2.

        session.execute(keyspaceStatement);
    }
    public CqlSession getSession() {
        return session;
    }

    public void close() {
        session.close();
    }
}
