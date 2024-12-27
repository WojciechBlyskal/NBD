package org.example.mappers;

import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import org.example.dao.GuestDao;
import org.example.dao.RoomDao;
import org.example.dao.RentDao;

@Mapper
public interface RepositoryMapper {

    @DaoFactory
    GuestDao accountDao(@DaoKeyspace CqlIdentifier keyspace);

    @DaoFactory
    RoomDao boardDao(@DaoKeyspace CqlIdentifier keyspace);

    @DaoFactory
    RentDao commentDao(@DaoKeyspace CqlIdentifier keyspace);
}
