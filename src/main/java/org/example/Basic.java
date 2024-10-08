package org.example;

public class Basic extends Standard{
    public Basic(int number, int floor, double surface, boolean balcony, double price, int forHowManyPeople, boolean renewed) {
        super(number, floor, surface, balcony, price, forHowManyPeople, renewed);
    }

    public String getInfo() {
        return super.getInfo() + "This is a Basic room.";
    }
}
