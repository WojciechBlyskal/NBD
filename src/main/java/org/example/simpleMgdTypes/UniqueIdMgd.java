package org.example.simpleMgdTypes;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public class UniqueIdMgd {
    @BsonProperty("uuid")
    private UUID uuid;

    public UniqueIdMgd() {
    }

    @BsonCreator
    public UniqueIdMgd(@BsonProperty("uuid") UUID uniqueId) {
        this.uuid = uniqueId;
    }

    public UUID getUuid() {
        return uuid;
    }
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
