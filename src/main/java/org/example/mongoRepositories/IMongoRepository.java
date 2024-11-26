package org.example.mongoRepositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.bson.conversions.Bson;
import org.example.Mgd.IEntity;
import org.example.simpleMgdTypes.UniqueIdMgd;

public interface IMongoRepository {

    void addRemote(IEntity object) throws Exception;
    void removeRemote(UniqueIdMgd uniqueIdMgd);
    IEntity findRemote(UniqueIdMgd uniqueIdMgd) throws JsonProcessingException;
    void updateRemote(Bson filter, Bson update);
}
