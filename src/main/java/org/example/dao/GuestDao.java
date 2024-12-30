package org.example.dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import org.example.model.Guest;

import java.util.UUID;

@Dao
public interface GuestDao {

    @StatementAttributes(consistencyLevel = "QUORUM") //Quorum-wiekszosc replik musi sie zgodzic. W przypadku 2 replik jest
    @Insert //rownowazne ALL, ale tak na wypadek, gdyby ktos pozniej zwiekszyl liczbe wezlow,
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
