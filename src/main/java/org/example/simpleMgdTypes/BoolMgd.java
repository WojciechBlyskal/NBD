package org.example.simpleMgdTypes;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;


public class BoolMgd {

    @BsonProperty("boolean")
    private Boolean aboolean;

    public BoolMgd() {
    }

    @BsonCreator
    public BoolMgd(@BsonProperty("boolean") Boolean aboolean) {
        this.aboolean = aboolean;
    }

    public Boolean getABoolean() {
        return aboolean;
    }
    public void setAboolean(Boolean aboolean) {
        this.aboolean = aboolean;
    }
}
