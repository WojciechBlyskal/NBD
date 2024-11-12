package org.example.Mgd;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.simpleMgdTypes.UniqueIdMgd;

@BsonDiscriminator(key="_room", value="microsuitemgd")
public class MicroSuiteMgd extends RoomMgd {
    @BsonCreator
    public MicroSuiteMgd(@BsonProperty("_id") UniqueIdMgd entityId,
                     @BsonProperty("number") int number,
                     @BsonProperty("floor") int floor,
                     @BsonProperty("surface") double surface,
                     @BsonProperty("price") double price,
                     @BsonProperty("rented") int rented) {
        super(entityId, number, floor, surface, price, rented);
    }

    public MicroSuiteMgd() {
    }
}
