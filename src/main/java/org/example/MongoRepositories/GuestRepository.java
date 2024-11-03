package org.example.MongoRepositories;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneOptions;
import Mgd.GuestMgd;
import org.bson.conversions.Bson;
import simpleMgdTypes.UniqueIdMgd;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class GuestRepository<Guest> extends AbstractMongoRepository {
    private List<GuestMgd> list = new ArrayList<>();
    private ConnectionManager connectionManager;
    private MongoCollection<GuestMgd> guestCollection;

    public GuestRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;

        if(!(connectionManager.getMongoDB().listCollectionNames().into(new ArrayList<String>()).
                contains(getClientCollectionName()))) {
            connectionManager.getMongoDB().createCollection(getClientCollectionName());
        }
        guestCollection = connectionManager.getMongoDB().getCollection(
                getClientCollectionName(),
                GuestMgd.class
        );
    }

    public GuestMgd findRemote(UniqueIdMgd uniqueIdMgd){

        GuestMgd foundGuest = null;

        Bson filter = Filters.eq("_id", uniqueIdMgd.getUuid());
        try {
            foundGuest =
                    guestCollection.find(filter).into(new ArrayList<>()).getFirst();
        } catch (NoSuchElementException e) {
        }
        return foundGuest;
    }

    public void addRemote(GuestMgd obj, ClientSession clientSession) {
        Bson uuidFilter = Filters.eq("_id",
                obj.getEntityId().getUuid());
        ArrayList<GuestMgd> foundGuest = findRemote(uuidFilter);

        if (foundGuest.isEmpty()){
            guestCollection.insertOne(clientSession, obj,
                    new InsertOneOptions().bypassDocumentValidation(false));
        }
    }

    public ArrayList<GuestMgd> findRemote(Bson filter){
        return guestCollection.find(filter).into(new ArrayList<>());
    }

    public void removeRemote(UniqueIdMgd uniqueIdMgd) {

        Bson filter = Filters.eq("_id", uniqueIdMgd.getUuid());
        guestCollection.findOneAndDelete(filter);
    }

    public void updateRemote(Bson filter, Bson update) {
        guestCollection.updateOne(filter, update);
    }

    public void addLocal(GuestMgd obj) {
        list.add(obj);
    }

    public void removeLocal(GuestMgd obj) {
        list.remove(obj);
    }

    public List<GuestMgd> getLocal() {
        return list;
    }

    public void clearLocal() {
        while(!list.isEmpty()){
            list.removeFirst();
        }
    }
    public void dropCollection(){
        this.guestCollection.drop();
    }
}
