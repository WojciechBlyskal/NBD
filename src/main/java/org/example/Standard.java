package org.example;

public abstract class Standard extends MultipleRoom {
    private boolean renewed;
    public Standard(int number, int floor, double surface, boolean balcony, double price, int forHowManyPeople, boolean renewed) {
        super(number, floor, surface, balcony, price, forHowManyPeople);
        this.renewed = renewed;
    }

    public boolean isRenewed() {
        return renewed;
    }

    public void setRenewed(boolean renewed) {
        this.renewed = renewed;
    }

    public String getInfo() {
        if (this.isRenewed()) {
            return super.getInfo() + "It is renewed.";
        } else {
            return super.getInfo() + "It is not renewed.";
        }
    }
}
