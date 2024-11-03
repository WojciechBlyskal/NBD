package org.example.simpleMgdTypes;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.avro.annotation.AvroNamespace;
import org.apache.avro.reflect.AvroIgnore;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@AvroNamespace("avro.simpleMgdTypes")
public class UniqueIdMgd {
    @AvroIgnore
    @BsonProperty("uuid")
    private UUID uuid;

    public UniqueIdMgd() {
    }

    @BsonCreator
    public UniqueIdMgd(@BsonProperty("uuid") UUID uniqieId) {
        this.uuid = uniqieId;
    }

    public UUID getUuid() {
        return uuid;
    }
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
