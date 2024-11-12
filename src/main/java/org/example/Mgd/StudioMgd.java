package org.example.Mgd;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.simpleMgdTypes.UniqueIdMgd;

@BsonDiscriminator(key="_room", value="studiomgd")
public class StudioMgd extends RoomMgd {

    @BsonProperty("balcony")
    private boolean balcony;

    @BsonCreator
    public StudioMgd(@BsonProperty("_id") UniqueIdMgd entityId,
                   @BsonProperty("number") int number,
                   @BsonProperty("floor") int floor,
                   @BsonProperty("surface") double surface,
                   @BsonProperty("price") double price,
                   @BsonProperty("rented") int rented,
                   @BsonProperty("balcony") boolean balcony) {
        super(entityId, number, floor, surface, price, rented);
        this.balcony = balcony;
    }

    public StudioMgd() {
    }

    public boolean isBalcony() {
        return balcony;
    }

    public void setBalcony(boolean balcony) {
        this.balcony = balcony;
    }
}
