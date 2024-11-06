package org.example.MongoRepositories;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import org.example.Mgd.IEntity;
import org.example.Mgd.RentMgd;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.example.Mgd.RoomMgd;
import org.example.simpleMgdTypes.UniqueIdMgd;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RentRepository extends AbstractMongoRepository implements IMongoRepository {
    private List<RentMgd> list = new ArrayList<>();
    private ConnectionManager connectionManager;
    private MongoCollection<RentMgd> rentCollection;

    /*public RentRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;

        if(!(connectionManager.getMongoDB().listCollectionNames().into(new ArrayList<String>()).
                contains(getRentCollectionName()))) {
            connectionManager.getMongoDB().createCollection(getRentCollectionName());
        }
        rentCollection = connectionManager.getMongoDB().getCollection(
                getRentCollectionName(),
                RentMgd.class
        );
    }*/

    public RentRepository(ConnectionManager connectionManager,
                                      WriteConcern writeConcern,
                                      ReadPreference readPreference) {
        this.connectionManager = connectionManager;

        if(!(connectionManager.getMongoDB().listCollectionNames().into(new ArrayList<String>()).contains(getRentCollectionName()))) {
            connectionManager.getMongoDB().createCollection(getRentCollectionName());
        }

        rentCollection = connectionManager.getMongoDB().getCollection(
                        getRentCollectionName(),
                        RentMgd.class)
                .withWriteConcern(writeConcern)
                .withReadPreference(readPreference);
        /*this.connectionManager = connectionManager;

        if (!connectionManager.getMongoDB().listCollectionNames()
                .into(new ArrayList<>()).contains(getRentCollectionName())) {
            connectionManager.getMongoDB().createCollection(getRentCollectionName());
        }

        rentCollection = connectionManager.getMongoDB().getCollection(
                        getRentCollectionName(),
                        RentMgd.class)
                .withWriteConcern(writeConcern)
                .withReadPreference(readPreference);*/
    }

    @Override
    public void addRemote(IEntity object) {
        //RentMgd rent = (RentMgd) object;
        //if (rent.getRoom().isRented() == false) {
          //  rent.getRoom().setRented(true);
            rentCollection.insertOne((RentMgd) object);
        //} else {
          //  throw new RuntimeException("Room is rented");
        //}
    }

    public ArrayList<RentMgd> findRemote(Bson filter){
        return rentCollection.find(filter).into(new ArrayList<>());
    }

    public void removeRemote(UniqueIdMgd uniqueIdMgd) {
        Bson filter = Filters.and(
                Filters.eq("_id", uniqueIdMgd.getUuid()), // Match on unique ID
                Filters.ne("endTime", null)               // Ensure endTime is not null
        );
        rentCollection.findOneAndDelete(filter);
    }

    public void removeRemote(Bson filter) {
        rentCollection.findOneAndDelete(filter);
    }

    public void updateRemote(Bson filter, Bson update) {
        BsonDocument updateDoc = update.toBsonDocument(BsonDocument.class, rentCollection.getCodecRegistry());
        if (containsRestrictedField(updateDoc, "roomMgd") || containsRestrictedField(updateDoc, "guestMgd")
                || containsRestrictedField(updateDoc, "startTime")) {
            throw new IllegalArgumentException("Updating startTime, 'roomMgd', 'guestMgd' fields is not allowed.");
        }
        /*if (containsRestrictedField(updateDoc, "endTime")) {
            // Retrieve the associated RoomMgd object
            Bson filter2 = Filters.eq("number", ),
                            RoomMgd room = findRemote(filter2);

            if (room != null) {
                // Set the desired field in RoomMgd
                room.setSpecialField(true); // replace 'setSpecialField' with the actual field and method name
                updateRoom(room); // save changes in RoomMgd collection
            }
        }*/

        rentCollection.updateOne(filter, update);
    }

    private boolean containsRestrictedField(BsonDocument updateDoc, String fieldName) {
        for (String key : updateDoc.keySet()) {
            BsonValue value = updateDoc.get(key);
            if (value.isDocument()) {
                BsonDocument nestedDoc = value.asDocument();
                if (nestedDoc.containsKey(fieldName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void dropCollection(){
        this.rentCollection.drop();
    }
}
