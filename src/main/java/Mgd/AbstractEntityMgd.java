package Mgd;

import org.bson.codecs.pojo.annotations.BsonProperty;
import simpleMgdTypes.UniqueIdMgd;
import java.io.Serializable;

public abstract class AbstractEntityMgd implements Serializable {

    @BsonProperty("_id")
    private final UniqueIdMgd entityId;

    public UniqueIdMgd getEntityId() {
        return entityId;
    }

    public AbstractEntityMgd(UniqueIdMgd entityId) {
        this.entityId = entityId;
    }
}