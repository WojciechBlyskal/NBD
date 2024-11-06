package org.example.MongoRepositories;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
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

    public ArrayList<GuestMgd> findRemote(Bson filter) {
        return guestCollection.find(filter).into(new ArrayList<>());
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
