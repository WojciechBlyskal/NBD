package org.example.MongoRepositories;

import com.mongodb.client.ClientSession;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.example.Mgd.GuestMgd;
import org.example.Mgd.IEntity;
import org.example.Mgd.RoomMgd;
import com.mongodb.client.MongoCollection;
import org.bson.conversions.Bson;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class RoomRepository<Room> extends AbstractMongoRepository implements IMongoRepository {

    private List<RoomMgd> list = new ArrayList<>();
    private ConnectionManager connectionManager;
    private MongoCollection<RoomMgd> roomCollection;
    private ValidationOptions validationOptions = new ValidationOptions()
            .validator(
                    Filters.lte("rented", 1))
            .validationLevel(ValidationLevel.STRICT)
            .validationAction(ValidationAction.ERROR);
    private CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions().validationOptions(validationOptions);

    public RoomRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        if(!(connectionManager.getMongoDB().listCollectionNames().into(new ArrayList<String>()).
                contains(getRoomCollectionName()))) {
            connectionManager.getMongoDB().createCollection(getRoomCollectionName());
        }
        /*ValidationOptions options = new ValidationOptions().validator(
                Document.parse("""
                        {
                            $jsonSchema:{
                                "bsonType": "object",
                                "properties": {
                                    "rented": {
                                    "bsonType": "int",
                                    "minimum": 0,
                                    "maximum": 1,
                                    "description": "must be 1 for rented and 0 for available"
                                    }
                                }
                            }
                        }
                        """)).validationAction(ValidationAction.ERROR);
        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions().validationOptions(options);
        getMongoDB().createCollection("rooms", createCollectionOptions);*/
        roomCollection = connectionManager.getMongoDB().getCollection(
                getRoomCollectionName(),
                RoomMgd.class
        );
    }

    @Override
    public void addRemote(IEntity object) {
        roomCollection.insertOne((RoomMgd) object, new InsertOneOptions().bypassDocumentValidation(false));
    }

    public void addRemote(RoomMgd object, ClientSession clientSession) {

        Bson uuidFilter = Filters.eq("_id", object.getEntityId().getUuid());
        ArrayList<RoomMgd> foundRooms = findRemote(uuidFilter);

        if(foundRooms.isEmpty()) {
            roomCollection.insertOne(clientSession, object,
                    new InsertOneOptions().bypassDocumentValidation(false));
        }
    }

    public ArrayList<RoomMgd> findRemote(Bson filter) {
        return roomCollection.find(filter).into(new ArrayList<>());
    }

    //@Override
    public RoomMgd findRemote(UniqueIdMgd uniqueIdMgd) {

        RoomMgd foundRoom = null;

        Bson filter = Filters.eq("_id", uniqueIdMgd.getUuid());
        try {
            foundRoom = roomCollection
                    .find(filter)
                    .into(new ArrayList<>()).getFirst();
        } catch (NoSuchElementException e){
        }

        return foundRoom;
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

    public void updateRemote(Bson filter, Bson update, ClientSession clientSession) {

        roomCollection.updateOne(clientSession, filter, update);
    }

    public void dropCollection(){
        this.roomCollection.drop();
    }
}
