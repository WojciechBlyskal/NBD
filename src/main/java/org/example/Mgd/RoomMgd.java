package org.example.Mgd;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.example.simpleMgdTypes.UniqueIdMgd;

public class RoomMgd extends AbstractEntityMgd {

    @BsonProperty("number")
    private int number;
    @BsonProperty("floor")
    private int floor;
    @BsonProperty("surface")
    private double surface;
    @BsonProperty("price")
    private double price;
    /*@BsonProperty("isRented")
    private boolean isRented = false;*/

    @BsonCreator
    public RoomMgd(@BsonProperty("_id") UniqueIdMgd entityId,
                    @BsonProperty("number") int number,
                    @BsonProperty("floor") int floor,
                    @BsonProperty("surface") double surface,
                    @BsonProperty("price") double price){//,
                    //@BsonProperty("isRented") boolean isRented){
        super(entityId);
        this.number = number;
        this.floor = floor;
        this.surface = surface;
        this.price = price;
        //this.isRented = isRented;
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

    /*public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }*/
}
