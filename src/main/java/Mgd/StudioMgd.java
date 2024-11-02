package Mgd;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import simpleMgdTypes.UniqueIdMgd;

public class StudioMgd extends RoomMgd {

    @BsonProperty("balcony")
    private boolean balcony;

    @BsonCreator
    public StudioMgd(@BsonProperty("_id") UniqueIdMgd entityId,
                   @BsonProperty("number") int number,
                   @BsonProperty("floor") int floor,
                   @BsonProperty("surface") double surface,
                   @BsonProperty("price") double price,
                   @BsonProperty("balcony") boolean balcony) {
        super(entityId, number, floor, surface, price);
        this.balcony = balcony;
    }

    public boolean isBalcony() {
        return balcony;
    }

    public void setBalcony(boolean balcony) {
        this.balcony = balcony;
    }
}
