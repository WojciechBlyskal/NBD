package Mgd;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import simpleMgdTypes.UniqueIdMgd;

public class MicroSuiteMgd extends RoomMgd {
    @BsonCreator
    public MicroSuiteMgd(@BsonProperty("_id") UniqueIdMgd entityId,
                     @BsonProperty("number") int number,
                     @BsonProperty("floor") int floor,
                     @BsonProperty("surface") double surface,
                     @BsonProperty("price") double price) {
        super(entityId, number, floor, surface, price);
    }
}
