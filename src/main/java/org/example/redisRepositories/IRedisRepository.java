package org.example.redisRepositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.Mgd.IEntity;
import redis.clients.jedis.exceptions.JedisConnectionException;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.NoSuchElementException;

public interface IRedisRepository {

    public void addToCache (IEntity object)
            throws JedisConnectionException, JsonProcessingException;
    public IEntity findInCache (UniqueIdMgd uniqueIdMgd)
            throws JedisConnectionException, NoSuchElementException, JsonProcessingException;
    public void deleteFromCache(UniqueIdMgd uniqueIdMgd)
            throws JedisConnectionException;
}
