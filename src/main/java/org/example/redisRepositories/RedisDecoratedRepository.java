package org.example.redisRepositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.Mgd.IEntity;
import org.bson.conversions.Bson;
import org.example.mongoRepositories.IMongoRepository;
import redis.clients.jedis.exceptions.JedisConnectionException;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.NoSuchElementException;
import java.util.UUID;

public class RedisDecoratedRepository
        implements IRedisRepository, IMongoRepository {

    protected IRedisRepository iRedisRepositorywrapper;
    protected IMongoRepository iMongoRepositorywrapper;

    public RedisDecoratedRepository(IRedisRepository iRedisRepository) {
        this.iRedisRepositorywrapper = iRedisRepository;
    }
    public RedisDecoratedRepository(IRedisRepository iRedisRepositorywrapper,
                                    IMongoRepository iMongoRepositorywrapper) {
        this.iRedisRepositorywrapper = iRedisRepositorywrapper;
        this.iMongoRepositorywrapper = iMongoRepositorywrapper;
    }

    public IMongoRepository getiMongoRepositorywrapper() {

        return this.iMongoRepositorywrapper;
    }
    public void setiMongoRepositorywrapper(IMongoRepository iMongoRepositorywrapper) {

        this.iMongoRepositorywrapper = iMongoRepositorywrapper;
    }

    @Override
    public void addToCache(IEntity object)
            throws JedisConnectionException
            ,JsonProcessingException {
            iRedisRepositorywrapper.addToCache(object);
    }
    @Override
    public IEntity findInCache(UniqueIdMgd uniqueIdMgd) throws JedisConnectionException
            ,NoSuchElementException
            ,JsonProcessingException {
            return iRedisRepositorywrapper.findInCache(uniqueIdMgd);
    }
    @Override
    public void deleteFromCache(UniqueIdMgd uniqueIdMgd)
            throws JedisConnectionException {
        iRedisRepositorywrapper.deleteFromCache(uniqueIdMgd);
    }


    @Override
    public void addRemote(IEntity object)
            throws Exception{
        try {
            addToCache(object);
        } catch (JedisConnectionException e){
            System.out.println("Could not connect to Redis");
        }
        iMongoRepositorywrapper.addRemote(object);
    }

    public IEntity findRemote(UniqueIdMgd uniqueIdMgd)
            throws JsonProcessingException{

        IEntity foundEntity = null;
        try {
            foundEntity = findInCache(uniqueIdMgd);
        }
        catch (JedisConnectionException e){
            foundEntity = iMongoRepositorywrapper.findRemote(uniqueIdMgd);
        }
        catch (NoSuchElementException e){
            foundEntity = iMongoRepositorywrapper.findRemote(uniqueIdMgd);

            if (foundEntity != null) {
                addToCache(foundEntity);
            }
        }
        return foundEntity;
    }

    @Override
    public void removeRemote(UniqueIdMgd uniqueIdMgd) {

        try {
            deleteFromCache(uniqueIdMgd);
        } catch (JedisConnectionException e) {
            System.out.println("Could not connect to Redis");
        }
        iMongoRepositorywrapper.removeRemote(uniqueIdMgd);
    }

    @Override
    public void updateRemote(Bson filter, Bson update) {
        iMongoRepositorywrapper.updateRemote(filter, update);

        String uniqueIdString = filter.toBsonDocument().getString("_id").getValue();
        UniqueIdMgd uniqueId = new UniqueIdMgd(UUID.fromString(uniqueIdString));

        if (uniqueId != null) {
            try {
                deleteFromCache(uniqueId);
                IEntity updatedEntity = iMongoRepositorywrapper.findRemote(uniqueId);

                if (updatedEntity != null) {
                    addToCache(updatedEntity);
                }
            } catch (JedisConnectionException e) {
                System.out.println("Could not connect to Redis for cache update");
            } catch (JsonProcessingException e) {
                System.err.println("Failed to serialize the updated object for caching");
            }
        }
    }

    public void cleanCache() {
        try {
            iRedisRepositorywrapper.cleanCache();
        } catch (JedisConnectionException e) {
            System.out.println("Could not connect to Redis");
        }
    }
}

/*@Override
    public void updateRemote(Bson filter, Bson update) {
        iMongoRepositorywrapper.updateRemote(filter, update);

        String uniqueIdString = filter.toBsonDocument().getString("_id").getValue();
        UniqueIdMgd uniqueId = new UniqueIdMgd(UUID.fromString(uniqueIdString));
        if (uniqueId != null) {
            try {
                deleteFromCache(uniqueId);
            } catch (JedisConnectionException e) {
                System.out.println("Could not connect to Redis");
            }
        }
    }*/
