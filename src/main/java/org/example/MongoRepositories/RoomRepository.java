package org.example.MongoRepositories;

import org.example.Mgd.RoomMgd;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneOptions;
import org.bson.conversions.Bson;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayList;

public class RoomRepository<Room> extends AbstractMongoRepository {
    private List<RoomMgd> list = new ArrayList<>();
    private ConnectionManager connectionManager;
    private MongoCollection<RoomMgd> roomCollection;

    public RoomRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;

        if(!(connectionManager.getMongoDB().listCollectionNames().into(new ArrayList<String>()).
                contains(getClientCollectionName()))) {
            connectionManager.getMongoDB().createCollection(getClientCollectionName());
        }
        roomCollection = connectionManager.getMongoDB().getCollection(
                getClientCollectionName(),
                RoomMgd.class
        );
    }

    public RoomMgd findRemote(UniqueIdMgd uniqueIdMgd){

        RoomMgd foundRoom = null;

        Bson filter = Filters.eq("_id", uniqueIdMgd.getUuid());
        try {
            foundRoom =
                    roomCollection.find(filter).into(new ArrayList<>()).getFirst();
        } catch (NoSuchElementException e) {
        }
        return foundRoom;
    }

    public void addRemote(RoomMgd obj, ClientSession clientSession) {
        Bson uuidFilter = Filters.eq("_id",
                obj.getEntityId().getUuid());
        ArrayList<RoomMgd> foundRoom = findRemote(uuidFilter);

        if (foundRoom.isEmpty()){
            roomCollection.insertOne(clientSession, obj,
                    new InsertOneOptions().bypassDocumentValidation(false));
        }
    }

    public ArrayList<RoomMgd> findRemote(Bson filter){
        return roomCollection.find(filter).into(new ArrayList<>());
    }

    public void removeRemote(UniqueIdMgd uniqueIdMgd) {

        Bson filter = Filters.eq("_id", uniqueIdMgd.getUuid());
        roomCollection.findOneAndDelete(filter);
    }

    public void updateRemote(Bson filter, Bson update) {
        roomCollection.updateOne(filter, update);
    }

    public void addLocal(RoomMgd obj) {
        list.add(obj);
    }

    public void removeLocal(RoomMgd obj) {
        list.remove(obj);
    }

    public List<RoomMgd> getLocal() {
        return list;
    }

    public void clearLocal() {
        while(!list.isEmpty()){
            list.removeFirst();
        }
    }
    public void dropCollection(){
        this.roomCollection.drop();
    }
}
