package org.example.Mgd;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.simpleMgdTypes.UniqueIdMgd;

@BsonDiscriminator(key="_room")
public abstract class RoomMgd extends AbstractEntityMgd {

    @BsonProperty("number")
    private int number;
    @BsonProperty("floor")
    private int floor;
    @BsonProperty("surface")
    private double surface;
    @BsonProperty("price")
    private double price;
    @BsonProperty("rented")
    private int rented = 0;

    public RoomMgd() {
    }

    @BsonCreator
    public RoomMgd(@BsonProperty("_id") UniqueIdMgd entityId,
                    @BsonProperty("number") int number,
                    @BsonProperty("floor") int floor,
                    @BsonProperty("surface") double surface,
                    @BsonProperty("price") double price,//,
                    @BsonProperty("rented") int rented){
        super(entityId);
        this.number = number;
        this.floor = floor;
        this.surface = surface;
        this.price = price;
        this.rented = rented;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRented() {
        return rented;
    }

    public void setRented(int rented) {
        this.rented = rented;
    }



    /*@Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("number", number)
                .append("floor", floor)
                .append("surface", surface)
                .append("price", price)
                .append("rented", rented)
                .toString();
    }*/
}
