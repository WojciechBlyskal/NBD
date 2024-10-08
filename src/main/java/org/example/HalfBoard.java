package org.example;

public class HalfBoard extends Catering {
    public String getCateringInfo() {
        return "Catering:HalfBoard. Catering cost per day: " + String.valueOf(getSupplementPerDay());
    }
    public double getSupplementPerDay() {
        return 80;
    }
}
