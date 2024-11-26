package org.example.mongoRepositories;

import com.mongodb.client.ClientSession;
import com.mongodb.client.model.*;
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
        if (foundRooms.isEmpty()) {
            roomCollection.insertOne(clientSession, object,
                    new InsertOneOptions().bypassDocumentValidation(false));
        }
    }

    public ArrayList<RoomMgd> findRemote(Bson filter) {
        ArrayList<RoomMgd> foundRooms = roomCollection
                .find(filter)
                .into(new ArrayList<>());

        return foundRooms;
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
        roomCollection
                .updateOne(filter,
                        update,
                        new UpdateOptions().bypassDocumentValidation(false));
    }

    public void updateRemote(Bson filter, Bson update, ClientSession clientSession) {

        roomCollection.updateOne(clientSession, filter, update);
    }

    public void dropCollection(){
        this.roomCollection.drop();
    }
}
