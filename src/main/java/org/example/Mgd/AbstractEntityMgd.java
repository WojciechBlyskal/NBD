package org.example.Mgd;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.simpleMgdTypes.UniqueIdMgd;
import java.io.Serializable;

public abstract class AbstractEntityMgd implements Serializable, IEntity {

    @BsonProperty("_id")
    private UniqueIdMgd entityId;

    @Override
    public UniqueIdMgd getEntityId() {
        return entityId;
    }

    public AbstractEntityMgd(UniqueIdMgd entityId) {
        this.entityId = entityId;
    }

    public void setEntityId(UniqueIdMgd entityId) {
        this.entityId = entityId;
    }
}