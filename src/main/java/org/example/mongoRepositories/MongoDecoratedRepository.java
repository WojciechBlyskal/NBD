package org.example.mongoRepositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.Mgd.AbstractEntityMgd;
import org.example.Mgd.IEntity;
import org.bson.conversions.Bson;
import org.example.simpleMgdTypes.UniqueIdMgd;

public class MongoDecoratedRepository
        implements IMongoRepository {

    protected IMongoRepository iMongoRepositorywrapper;

    public MongoDecoratedRepository(IMongoRepository iMongoRepositorywrapper) {
        this.iMongoRepositorywrapper = iMongoRepositorywrapper;
    }

    public IMongoRepository getiMongoRepositorywrapper() {
        return iMongoRepositorywrapper;
    }

    @Override
    public void addRemote(IEntity object)
            throws Exception {

            iMongoRepositorywrapper.addRemote(object);
    }

    @Override
    public IEntity findRemote(UniqueIdMgd uniqueIdMgd)
            throws JsonProcessingException {

        return iMongoRepositorywrapper.findRemote(uniqueIdMgd);
    }

    @Override
    public void removeRemote(UniqueIdMgd uniqueIdMgd) {
        iMongoRepositorywrapper.removeRemote(uniqueIdMgd);
    }

    @Override
    public void updateRemote(Bson filter, Bson update) {
        iMongoRepositorywrapper.updateRemote(filter,
                update);
    }
}
