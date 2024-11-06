package org.example.MongoRepositories;

import org.example.Mgd.IEntity;
import org.example.Mgd.RoomMgd;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneOptions;
import org.bson.conversions.Bson;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.List;
import java.util.ArrayList;

public class RoomRepository<Room> extends AbstractMongoRepository implements IMongoRepository {
    private List<RoomMgd> list = new ArrayList<>();
    private ConnectionManager connectionManager;
    private MongoCollection<RoomMgd> roomCollection;

    public RoomRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;

        if(!(connectionManager.getMongoDB().listCollectionNames().into(new ArrayList<String>()).
                contains(getRoomCollectionName()))) {
            connectionManager.getMongoDB().createCollection(getRoomCollectionName());
        }
        roomCollection = connectionManager.getMongoDB().getCollection(
                getRoomCollectionName(),
                RoomMgd.class
        );
    }

    @Override
    public void addRemote(IEntity object) {
        roomCollection.insertOne((RoomMgd) object, new InsertOneOptions().bypassDocumentValidation(false));
    }

    public ArrayList<RoomMgd> findRemote(Bson filter){
        return roomCollection.find(filter).into(new ArrayList<>());
    }

    public void removeRemote(UniqueIdMgd uniqueIdMgd) {

        Bson filter = Filters.eq("_id", uniqueIdMgd.getUuid());
        roomCollection.findOneAndDelete(filter);
    }

    public void removeRemote(Bson filter) {
        roomCollection.findOneAndDelete(filter);
    }

    public void updateRemote(Bson filter, Bson update) {
        roomCollection.updateOne(filter, update);
    }

    public void dropCollection(){
        this.roomCollection.drop();
    }
}
