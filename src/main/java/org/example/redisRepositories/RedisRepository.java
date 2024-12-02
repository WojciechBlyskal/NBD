package org.example.redisRepositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.example.Mgd.*;
import redis.clients.jedis.*;
import redis.clients.jedis.args.ExpiryOption;
import redis.clients.jedis.exceptions.JedisConnectionException;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class RedisRepository
        implements IRedisRepository, AutoCloseable{

    private String hashPrefix;
    private Class<IEntity> aClass;
    private JedisPooled jedisPooled;
    private Jsonb jsonb;
    private PolymorphicTypeValidator ptv;
    private ObjectMapper mapper;

    public RedisRepository(String filePath) {
        this.jsonb = JsonbBuilder.create();

        ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("org.example.Mgd.StudioMgd")
                .allowIfSubType("org.example.Mgd.MicroSuiteMgd")
                .allowIfSubType("org.example.simpleMgdTypes.UniqueIdMgd")
                .build();
        mapper = new ObjectMapper().activateDefaultTyping(
                ptv,
                ObjectMapper.DefaultTyping.NON_FINAL);

        initRedisConnection(filePath);
    }

    private void initRedisConnection(String filePath) {

        ArrayList<String> hostAndPort = new ArrayList<>();
        try {
            hostAndPort = readHostAndPortFromStream(filePath);
        } catch (IOException ioException) {
            hostAndPort.add("localhost");
            hostAndPort.add("6379");
        }

        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().build();
        this.jedisPooled = new JedisPooled(new HostAndPort(
                hostAndPort.get(0),
                Integer.parseInt(hostAndPort.get(1))),
                clientConfig);
    }

    private ArrayList<String> readHostAndPortFromStream(String filePath) throws IOException {

        ArrayList<String> fileContent = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(
                new FileReader(filePath))){

            String line;
            for (int i = 0; i < 2; i++){
                line = bufferedReader.readLine();
                fileContent.add(line);
            }
        }

        return fileContent;
    }

    @Override
    public void addToCache(IEntity object)
            throws JedisConnectionException, JsonProcessingException {

        try {
            Connection connection = jedisPooled.getPool().getResource();
        } catch (JedisConnectionException jedisConnectionException){
            throw jedisConnectionException;
        }

        String ptvJson = null;

        ptvJson = mapper.writeValueAsString(object);

        jedisPooled.jsonSet(hashPrefix + object
                        .getEntityId()
                        .getUuid()
                        .toString(),
                ptvJson);
        jedisPooled.expire(hashPrefix + object
                .getEntityId()
                .getUuid()
                .toString(),
                60); //na jak dlugo sa cachowane
    }

    @Override
    public IEntity findInCache(UniqueIdMgd uniqueIdMgd)
            throws JedisConnectionException
            ,NoSuchElementException
            ,JsonProcessingException {
        try {
            Connection connection = jedisPooled.getPool().getResource();
        } catch (JedisConnectionException jedisConnectionException){
        }

        Object foundObject = jedisPooled.jsonGet(hashPrefix + uniqueIdMgd
                .getUuid()
                .toString());
        jedisPooled.expire(hashPrefix + uniqueIdMgd
                        .getUuid()
                        .toString(),
                60, //przedluzenie czasu przechowywania w cache
                ExpiryOption.GT);

        if (foundObject == null){
            throw new NoSuchElementException();
        }

        String json = jsonb.toJson(foundObject);
        IEntity foundEntity = null;

        foundEntity = mapper.readValue(json,
                aClass);
        return foundEntity;
    }

    @Override
    public void deleteFromCache(UniqueIdMgd uniqueIdMgd)
            throws JedisConnectionException {

        try {
            Connection connection = jedisPooled.getPool().getResource();
        } catch (JedisConnectionException jedisConnectionException){
            throw jedisConnectionException;
        }

        jedisPooled.del(hashPrefix + uniqueIdMgd.getUuid().toString());
    }

    public void setHashPrefix(String hashPrefix) {
        this.hashPrefix = hashPrefix;
    }

    public void setAClass(Class aClass) {
        this.aClass = aClass;
    }

    public void cleanCache() throws JedisConnectionException{
        try {
            Connection connection = jedisPooled.getPool().getResource();
            jedisPooled.flushAll();
        } catch (JedisConnectionException jedisConnectionException){
            throw jedisConnectionException;
        }
    }

    @Override
    public void close() {
        try {
            cleanCache();
        } catch (JedisConnectionException e){
        }
        jedisPooled.close();
    }
}
