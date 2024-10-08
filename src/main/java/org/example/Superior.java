package org.example;

public class Superior extends Standard{
    public Superior(int number, int floor, double surface, boolean balcony, double price, int forHowManyPeople, boolean renewed) {
        super(number, floor, surface, balcony, price, forHowManyPeople, renewed);
    }

    public String getInfo() {
        return super.getInfo() + "This is a Superior room.";
    }
}
