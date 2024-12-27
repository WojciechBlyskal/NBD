package org.example.dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.model.Room;

import java.util.UUID;

@Dao
public interface RoomDao {

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Insert
    void create(Room room);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Select
    Room getById(UUID id);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Update
    void update(Room room);

    @StatementAttributes(consistencyLevel = "ONE")
    @Delete
    void delete(Room room);
}
