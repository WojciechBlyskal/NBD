package org.example.Mgd;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.example.simpleMgdTypes.UniqueIdMgd;
import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GuestMgd.class, name = "guest"),
        @JsonSubTypes.Type(value = StudioMgd.class, name = "studio"),
        @JsonSubTypes.Type(value = MicroSuiteMgd.class, name = "microsuite"),
        @JsonSubTypes.Type(value = RentMgd.class, name = "rent")
})
public abstract class AbstractEntityMgd implements Serializable, IEntity {
    @BsonProperty("_id")
    private UniqueIdMgd entityId;

    public AbstractEntityMgd(){
    }

    public AbstractEntityMgd(UniqueIdMgd uniqueIdMgd) {
        this.entityId = uniqueIdMgd;
    }

    @Override
    public UniqueIdMgd getEntityId() {
        return entityId;
    }

    public void setEntityId(UniqueIdMgd entityId) {
        this.entityId = entityId;
    }

}