package org.example.MongoRepositories;

import org.example.Mgd.GuestMgd;
import org.example.Mgd.RentMgd;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneOptions;
import org.bson.conversions.Bson;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class RentRepository<Rent> extends AbstractMongoRepository{
    private List<RentMgd> list = new ArrayList<>();
    private ConnectionManager connectionManager;
    private MongoCollection<RentMgd> rentCollection;

    public RentRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;

        if(!(connectionManager.getMongoDB().listCollectionNames().into(new ArrayList<String>()).
                contains(getClientCollectionName()))) {
            connectionManager.getMongoDB().createCollection(getClientCollectionName());
        }
        rentCollection = connectionManager.getMongoDB().getCollection(
                getClientCollectionName(),
                RentMgd.class
        );
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

    public void removeRemote(UniqueIdMgd uniqueIdMgd) {

        Bson filter = Filters.eq("_id", uniqueIdMgd.getUuid());
        rentCollection.findOneAndDelete(filter);
    }

    public void removeRemote(Bson filter) {
        rentCollection.findOneAndDelete(filter);
    }

    public void updateRemote(Bson filter, Bson update) {
        rentCollection.updateOne(filter, update);
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
