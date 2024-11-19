package org.example.redisRepositories.Decorators;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.Mgd.IEntity;
import org.example.Mgd.RoomMgd;
import redis.clients.jedis.exceptions.JedisConnectionException;
import org.example.redisRepositories.IRedisRepository;
import org.example.redisRepositories.RedisDecoratedRepository;
import org.example.redisRepositories.RedisRepository;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.NoSuchElementException;

public class
RoomDecorator extends RedisDecoratedRepository {

    final String hashPrefix = "room:";

    public RoomDecorator(IRedisRepository iRedisRepository) {
        super(iRedisRepository);
    }

    private void decorate(){
        ((RedisRepository)iRedisRepositorywrapper).setHashPrefix(hashPrefix);
        ((RedisRepository)iRedisRepositorywrapper).setAClass(RoomMgd.class);
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
            throws JedisConnectionException{

        decorate();
        super.deleteFromCache(uniqueIdMgd);
    }
}
