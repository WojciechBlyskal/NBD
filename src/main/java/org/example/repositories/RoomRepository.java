package org.example.repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import org.example.dao.RoomDao;
import org.example.model.Room;
import org.example.mappers.RepositoryMapper;
import org.example.mappers.RepositoryMapperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RoomRepository implements EntityRepository<Room>{

    private final RoomDao roomDao;
    private final CqlSession session;

    public RoomRepository(CqlSession session, CqlIdentifier keyspace) {
        setTables(session, keyspace);

        RepositoryMapper builder = new RepositoryMapperBuilder(session).build();;
        this.roomDao = builder.roomDao(keyspace);
        this.session = session;
    }

    @Override
    public void create(Room room) {
        roomDao.create(room);
    }

    @Override
    public Room getById(UUID id) {
        return roomDao.getById(id);
    }

    @Override
    public List<Room> getAll() {
        SimpleStatement stmt = SimpleStatement.builder("SELECT * FROM rooms")
                .setPageSize(100)
                .build();
        ResultSet rs = session.execute(stmt);

        List<Room> rooms = new ArrayList<>();

        rs.forEach(row -> {
            Room room = new Room(
                    row.getInt("number"),
                    row.getInt("floor"),
                    row.getDouble("surface"),
                    row.getDouble("price"),
                    row.getString("type"),
                    row.getUuid("id"),
                    row.getSet("rent_ids", UUID.class)
            );
            rooms.add(room);
        });

        return rooms;
    }

    @Override
    public void update(Room room) {
        roomDao.update(room);
    }

    @Override
    public void delete(Room room) {
        roomDao.delete(room);
    }

    private static void setTables(CqlSession session, CqlIdentifier keyspace) {
        session.execute(SchemaBuilder.createTable(keyspace, CqlIdentifier.fromCql("rooms"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql("number"), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql("floor"), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql("surface"), DataTypes.DOUBLE)
                .withColumn(CqlIdentifier.fromCql("price"), DataTypes.DOUBLE)
                .withColumn(CqlIdentifier.fromCql("type"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("rent_ids"), DataTypes.setOf(DataTypes.UUID))
                .build());
    }
}
