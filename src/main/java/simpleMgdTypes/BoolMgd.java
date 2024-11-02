package simpleMgdTypes;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.avro.annotation.AvroNamespace;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@AvroNamespace("avro.simpleMgdTypes")
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
