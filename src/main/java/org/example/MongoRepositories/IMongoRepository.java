package org.example.MongoRepositories;

import com.fasterxml.jackson.core.JsonProcessingException;
//import org.example.Mgd.AbstractEntityMgd;
import org.example.Mgd.IEntity;
import org.bson.conversions.Bson;
import org.example.simpleMgdTypes.UniqueIdMgd;

public interface IMongoRepository {

    void addRemote(IEntity object)
            throws Exception;
    IEntity findRemote(UniqueIdMgd uniqueIdMgd)
            throws JsonProcessingException;
    void removeRemote(UniqueIdMgd uniqueIdMgd);
    void updateRemote(Bson filter, Bson update);
}
