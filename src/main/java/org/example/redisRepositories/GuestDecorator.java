package org.example.redisRepositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.Mgd.GuestMgd;
import org.example.Mgd.IEntity;
import redis.clients.jedis.exceptions.JedisConnectionException;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.NoSuchElementException;

public class GuestDecorator extends RedisDecoratedRepository {

    final String hashPrefix = "guest:";

    public GuestDecorator(IRedisRepository iRedisRepository) {

        super(iRedisRepository);
    }

    private void decorate(){
        ((RedisRepository)iRedisRepositorywrapper).setHashPrefix(hashPrefix);
        ((RedisRepository)iRedisRepositorywrapper).setAClass(GuestMgd.class);
    }

    @Override
    public void addToCache(IEntity object)
            throws JedisConnectionException
            ,JsonProcessingException {

        decorate();
        super.addToCache(object);
    }
    @Override
    public IEntity findInCache(UniqueIdMgd uniqueIdMgd)
            throws JedisConnectionException
            ,NoSuchElementException
            ,JsonProcessingException {

        decorate();
        return super.findInCache(uniqueIdMgd);
    }
    @Override
    public void deleteFromCache(UniqueIdMgd uniqueIdMgd)
            throws JedisConnectionException {

        decorate();
        super.deleteFromCache(uniqueIdMgd);
    }
}
