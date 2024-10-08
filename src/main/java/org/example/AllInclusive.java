package org.example;

public class AllInclusive extends Catering {
    public String getCateringInfo() {
        return "Catering:AllInclusive. Catering cost per day: " + String.valueOf(getSupplementPerDay());
    }
    public double getSupplementPerDay() {
        return 150;
    }
}
