package org.example.repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import org.example.dao.RentDao;
import org.example.model.Rent;
import org.example.mappers.RepositoryMapper;
import org.example.mappers.RepositoryMapperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RentRepository implements EntityRepository<Rent> {

    private final RentDao rentDao;
    private final CqlSession session;

    public RentRepository(CqlSession session, CqlIdentifier keyspace) {
        setTables(session, keyspace);

        RepositoryMapper builder = new RepositoryMapperBuilder(session).build();
        this.rentDao = builder.rentDao(keyspace);
        this.session = session;
    }

    @Override
    public void create(Rent rent) {
        rentDao.create(rent);
    }

    @Override
    public Rent getById(UUID id) {
        return rentDao.getById(id);
    }

    @Override
    public List<Rent> getAll() {
        SimpleStatement stmt = SimpleStatement.builder("SELECT * FROM rents")
                .setPageSize(100)
                .build();
        ResultSet rs = session.execute(stmt);

        List<Rent> rents = new ArrayList<>();

        rs.forEach(row -> {
            Rent rent = new Rent(
                    row.getLocalDate("start_time"),
                    row.getLocalDate("end_time"),
                    row.getUuid("guest"),
                    row.getUuid("room"),
                    row.getUuid("id")
            );
            rents.add(rent);
        });

        return rents;
    }

    @Override
    public void update(Rent rent) {
        rentDao.update(rent);
    }

    @Override
    public void delete(Rent rent) {
        rentDao.delete(rent);
    }

    private static void setTables(CqlSession session, CqlIdentifier keyspace) {
        session.execute(SchemaBuilder.createTable(keyspace, CqlIdentifier.fromCql("rents"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql("start_time"), DataTypes.DATE)
                .withColumn(CqlIdentifier.fromCql("end_time"), DataTypes.DATE)
                .withColumn(CqlIdentifier.fromCql("guest"), DataTypes.UUID)
                .withColumn(CqlIdentifier.fromCql("room"), DataTypes.UUID)
                .build());
    }
}