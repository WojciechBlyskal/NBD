package org.example.MongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.example.codec.RoomTypeCodec;
import org.example.codec.UniqueIdCodecProvider;

import java.util.List;

public abstract class AbstractMongoRepository implements AutoCloseable {
    private static ConnectionString connectionString = new ConnectionString("mongodb://mongodb1:27017," +
            "mongodb2:27018,mongodb3:27019/?replicaSet=replica_set_single"); //"mongodb://localhost:27017,localhost:27018,localhost:27019/?replicaSet=replica_set_single");
    private MongoCredential credential = MongoCredential.createCredential("admin", "admin",
            "adminpassword".toCharArray());
    /*private CodecRegistry pojoCodecRegistry =
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
                        CodecRegistries.fromProviders(new UniqueIdCodecProvider()),
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                ))
                .build();
        mongoClient = MongoClients.create(settings);
        mongoDB = mongoClient.getDatabase("db");
    }
*/
    private CodecRegistry pojoCodecRegistry =
            CodecRegistries.fromProviders(PojoCodecProvider.builder()
                    .automatic(true)
                    .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                    .build());
    private MongoClient mongoClient;
    private MongoDatabase mongoDB;

    /*protected void initDbConnection() {
        // Add custom codecs
        CodecRegistry customCodecs = CodecRegistries.fromCodecs(
                new RoomTypeCodec(pojoCodecRegistry) // Register RoomTypeCodec
        );

        // Combine all codec registries
        CodecRegistry combinedCodecRegistry = CodecRegistries.fromRegistries(
                customCodecs, // Add custom codecs
                CodecRegistries.fromProviders(new UniqueIdCodecProvider()), // Existing custom codec provider
                MongoClientSettings.getDefaultCodecRegistry(), // Default MongoDB codecs
                pojoCodecRegistry // Automatic POJO codec registry
        );

        // Build MongoClientSettings
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(combinedCodecRegistry) // Use the combined codec registry
                .build();

        // Initialize the MongoClient and MongoDatabase
        mongoClient = MongoClients.create(settings);
        mongoDB = mongoClient.getDatabase("db");
    }*/

    protected void initDbConnection() {
        CodecRegistry customCodecs = CodecRegistries.fromCodecs(new RoomTypeCodec(pojoCodecRegistry));

        CodecRegistry combinedCodecRegistry = CodecRegistries.fromRegistries(
                customCodecs,
                CodecRegistries.fromProviders(new UniqueIdCodecProvider()),
                MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(combinedCodecRegistry)
                .build();

        mongoClient = MongoClients.create(settings);
        mongoDB = mongoClient.getDatabase("db");
    }

    public MongoDatabase getMongoDB() {
        return mongoDB;
    }

    public String getGuestCollectionName() {
        return "guestCollection";
    }
    public String getRoomCollectionName() {
        return "roomCollection";
    }
    public String getRentCollectionName() {
        return "rentCollection";
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void close() {
    }
}