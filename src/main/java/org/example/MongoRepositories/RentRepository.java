package org.example.MongoRepositories;

import org.example.Mgd.GuestMgd;
import org.example.Mgd.IEntity;
import org.example.Mgd.RentMgd;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneOptions;
import org.bson.conversions.Bson;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class RentRepository<Rent> extends AbstractMongoRepository implements IMongoRepository {
    private List<RentMgd> list = new ArrayList<>();
    private ConnectionManager connectionManager;
    private MongoCollection<RentMgd> rentCollection;

    public RentRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;

        if(!(connectionManager.getMongoDB().listCollectionNames().into(new ArrayList<String>()).
                contains(getRentCollectionName()))) {
            connectionManager.getMongoDB().createCollection(getRentCollectionName());
        }
        rentCollection = connectionManager.getMongoDB().getCollection(
                getRentCollectionName(),
                RentMgd.class
        );
    }

    @Override
    public void addRemote(IEntity object) {
        rentCollection.insertOne((RentMgd) object);
    }

    public void addRemote(RentMgd obj, ClientSession clientSession) {
        Bson uuidFilter = Filters.eq("_id",
                obj.getEntityId().getUuid());
        ArrayList<RentMgd> foundRent = findRemote(uuidFilter);

        if (foundRent.isEmpty()){
            rentCollection.insertOne(clientSession, obj,
                    new InsertOneOptions().bypassDocumentValidation(false));
        }
    }

    public ArrayList<RentMgd> findRemote(Bson filter){
        return rentCollection.find(filter).into(new ArrayList<>());
    }

    public RentMgd findRemote(UniqueIdMgd uniqueIdMgd){

        RentMgd foundRent = null;

        Bson filter = Filters.eq("_id", uniqueIdMgd.getUuid());
        try {
            foundRent =
                    rentCollection.find(filter).into(new ArrayList<>()).getFirst();
        } catch (NoSuchElementException e) {
        }
        return foundRent;
    }

    /*public void removeRemote(UniqueIdMgd uniqueIdMgd) {
        Bson filter = Filters.eq("_id", uniqueIdMgd.getUuid());
        rentCollection.findOneAndDelete(filter);
    }

    public void removeRemote(Bson filter) {
        rentCollection.findOneAndDelete(filter);
    }*/
    public void removeRemote(UniqueIdMgd uniqueIdMgd) {
        Bson filter = Filters.and(
                Filters.eq("_id", uniqueIdMgd.getUuid()), // Match on unique ID
                Filters.ne("endTime", null)               // Ensure endTime is not null
        );
        rentCollection.findOneAndDelete(filter);
    }

    public void removeRemote(Bson filter) {
        Bson updatedFilter = Filters.and(
                filter,                                  // Original filter
                Filters.ne("endTime", null)              // Ensure endTime is not null
        );
        rentCollection.findOneAndDelete(updatedFilter);
    }

    /*public void updateRemote(Bson filter, Bson update) {
        rentCollection.updateOne(filter, update);
    }*/

    public void updateRemote(Bson filter, Bson update) {
        BsonDocument updateDoc = update.toBsonDocument(BsonDocument.class, rentCollection.getCodecRegistry());
        if (containsRestrictedField(updateDoc, "roomMgd") || containsRestrictedField(updateDoc, "guestMgd")) {
            throw new IllegalArgumentException("Updating 'roomMgd', 'guestMgd' fields is not allowed.");
        }
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

    public void addLocal(RentMgd obj) {
        list.add(obj);
    }

    public void removeLocal(RentMgd obj) {
        list.remove(obj);
    }

    public List<RentMgd> getLocal() {
        return list;
    }

    public void clearLocal() {
        while(!list.isEmpty()){
            list.removeFirst();
        }
    }
    public void dropCollection(){
        this.rentCollection.drop();
    }
}
