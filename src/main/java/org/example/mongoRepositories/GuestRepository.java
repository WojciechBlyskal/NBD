package org.example.mongoRepositories;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneOptions;
import org.example.Mgd.GuestMgd;
import org.bson.conversions.Bson;
import org.example.Mgd.IEntity;
import org.example.simpleMgdTypes.UniqueIdMgd;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class GuestRepository extends AbstractMongoRepository implements IMongoRepository {
    private ConnectionManager connectionManager;
    private MongoCollection<GuestMgd> guestCollection;
    private List<GuestMgd> list = new ArrayList<>();

    public GuestRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;

        if (!(connectionManager.getMongoDB().listCollectionNames().into(new ArrayList<String>()).
                contains(getGuestCollectionName()))) {
            connectionManager.getMongoDB().createCollection(getGuestCollectionName());
        }
        guestCollection = connectionManager.getMongoDB().getCollection(
                getGuestCollectionName(),
                GuestMgd.class
        );
    }

    @Override
    public void addRemote(IEntity object) {
        guestCollection.insertOne((GuestMgd) object);
    }

    public void addRemote(GuestMgd object, ClientSession clientSession) {

        Bson uuidFilter = Filters.eq("_id", object.getEntityId().getUuid());
        ArrayList<GuestMgd> foundClients = findRemote(uuidFilter);

        if(foundClients.isEmpty()) {
            guestCollection.insertOne(clientSession,
                    object,
                    new InsertOneOptions().bypassDocumentValidation(false));
        }
    }

    public ArrayList<GuestMgd> findRemote(Bson filter) {
        return guestCollection.find(filter).into(new ArrayList<>());
    }

    public GuestMgd findRemote(UniqueIdMgd uniqueIdMgd){

        GuestMgd foundGuest = null;

        Bson filter = Filters.eq("_id", uniqueIdMgd.getUuid());
        try {
            foundGuest =
                    guestCollection
                            .find(filter)
                            .into(new ArrayList<>()).getFirst();
        } catch (NoSuchElementException e){
        }

        return foundGuest;
    }

    public void removeRemote(UniqueIdMgd uniqueIdMgd) {

        Bson filter = Filters.eq("_id", uniqueIdMgd.getUuid());
        guestCollection.findOneAndDelete(filter);
    }

    public void removeRemote(Bson filter) {
        guestCollection.findOneAndDelete(filter);
    }

    public void updateRemote(Bson filter, Bson update) {
        guestCollection.updateOne(filter, update);
    }

    public void dropCollection(){
        this.guestCollection.drop();
    }
}
