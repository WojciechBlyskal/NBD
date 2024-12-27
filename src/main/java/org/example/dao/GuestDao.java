package org.example.dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.model.Guest;

import java.util.UUID;

@Dao
public interface GuestDao {

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Insert
    void create(Guest guest);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Select
    Guest getById(UUID id);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Update
    void update(Guest guest);

    @StatementAttributes(consistencyLevel = "ONE")
    @Delete
    void delete(Guest guest);
}
