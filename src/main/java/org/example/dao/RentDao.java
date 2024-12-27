package org.example.dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.model.Rent;

import java.util.UUID;

@Dao
public interface RentDao {

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Insert
    void create(Rent rent);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Select
    Rent getById(UUID id);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Update
    void update(Rent rent);

    @StatementAttributes(consistencyLevel = "ONE")
    @Delete
    void delete(Rent rent);
}
