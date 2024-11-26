package org.example.Mgd;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.simpleMgdTypes.UniqueIdMgd;

@BsonDiscriminator(key="_guest", value="guest")
public class GuestMgd extends AbstractEntityMgd {

    @BsonProperty("id")
    private long id;
    @BsonProperty("name")
    private String name;
    @BsonProperty("lastName")
    private String lastName;
    @BsonProperty("phoneNumber")
    private String phoneNumber;

    @BsonCreator
    public GuestMgd(@BsonProperty("_id") UniqueIdMgd entityId,
                    @BsonProperty("id") long id,
                    @BsonProperty("name") String name,
                    @BsonProperty("lastName") String lastName,
                    @BsonProperty("phoneNumber") String phoneNumber) {
        super(entityId);
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public GuestMgd() {
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
