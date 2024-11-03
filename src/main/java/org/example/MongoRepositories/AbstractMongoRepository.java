package org.example.MongoRepositories;

//import codecs.CodecProviders.UniqueIdCodecProvider;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
//import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;

public abstract class AbstractMongoRepository implements AutoCloseable {
    private static ConnectionString connectionString = new ConnectionString("mongodb://mongodb1:27017, " +
            "mongodb2:27018, mongodb3:27019/?replicaSet=replica_set_single");
    private MongoCredential credential = MongoCredential.createCredential("admin", "admin",
            "adminpassword".toCharArray());
    private CodecRegistry pojoCodecRegistry =
            CodecRegistries.fromProviders(PojoCodecProvider.builder()
            .automatic(true)
            .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
            .build());
    private MongoClient mongoClient;
    private MongoDatabase mongoDB;
    protected void initDbConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        //CodecRegistries.fromProviders(new UniqueIdCodecProvider()),
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                ))
                .build();
        mongoClient = MongoClients.create(settings);
        mongoDB = mongoClient.getDatabase("hotel");
    }
    public MongoDatabase getMongoDB() {
        return mongoDB;
    }

    public String getClientCollectionName() {
        return "clientCollection";
    }
    public String getTrainsCollectionName() {
        return "trainCollection";
    }
    public String getReservationCollectionName() {
        return "reservationCollection";
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void close(){
    }
}