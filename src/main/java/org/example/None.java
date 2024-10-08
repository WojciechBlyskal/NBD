package org.example;

public class None extends Catering {
     public String getCateringInfo() {
        return "Catering:None. Catering cost per day: " + String.valueOf(getSupplementPerDay());
    }
     public double getSupplementPerDay() {
        return 0;
    }
}
