package org.example.repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import org.example.dao.GuestDao;
import org.example.model.Guest;
import org.example.mappers.RepositoryMapper;
import org.example.mappers.RepositoryMapperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuestRepository implements EntityRepository<Guest> {

    private final GuestDao guestDao;
    private final CqlSession session;

    public GuestRepository(CqlSession session, CqlIdentifier keyspace) {
        setTables(session, keyspace);

        RepositoryMapper builder = new RepositoryMapperBuilder(session).build();;
        this.guestDao = builder.guestDao(keyspace);
        this.session = session;
    }

    @Override
    public void create(Guest guest) {
        guestDao.create(guest);
    }

    @Override
    public Guest getById(UUID id) {
        return guestDao.getById(id);
    }

    @Override
    public List<Guest> getAll() {
        SimpleStatement stmt = SimpleStatement.builder("SELECT * FROM guests")
                .setPageSize(100)
                .build();
        ResultSet rs = session.execute(stmt);

        List<Guest> guests = new ArrayList<>();

        rs.forEach(row -> {
            Guest guest = new Guest(
                    row.getString("name"),
                    row.getString("lastname"),
                    row.getString("phonenumber"),
                    row.getUuid("id"),
                    row.getSet("rent_ids", UUID.class)
            );
            guests.add(guest);
        });

        return guests;
    }

    @Override
    public void update(Guest guest) {
        guestDao.update(guest);
    }

    @Override
    public void delete(Guest guest) {
        guestDao.delete(guest);
    }

    private static void setTables(CqlSession session, CqlIdentifier keyspace) {
        session.execute(SchemaBuilder.createTable(keyspace, CqlIdentifier.fromCql("guests"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql("name"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("lastname"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("phonenumber"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("rent_ids"), DataTypes.setOf(DataTypes.UUID))
                .build());
    }
}
