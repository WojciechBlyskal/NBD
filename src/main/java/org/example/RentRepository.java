package org.example;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.function.Predicate;

public class RentRepository<Rent> {
    ArrayList<Rent> collection;

    public RentRepository() {
        this.collection = new ArrayList<>();
    }

    public void add(Rent obj) {
        if (obj != null) {
            collection.add(obj);
        }
    }

    public void remove(Rent obj) {
        if (obj != null) {
            collection.remove(obj);
        }
    }

    public int size() {
        return collection.size();
    }

    public String report() {
        return new ToStringBuilder(this)
                .append("collection", collection)
                .toString();
    }

    public ArrayList<Rent> findBy(Predicate<Rent> predicate) {
        ArrayList<Rent> found = new ArrayList<>();
        found.clear();
        for (int i = 0; i < size(); i++) {
            Rent obj = get(i);
            if (predicate.test(obj)) {
                found.add(obj);
            }
        }
        return found;
    }

    public ArrayList<Rent> findAll() {
        Predicate<Rent> temp = obj -> true;
        return findBy(temp);
    }

    public Rent get (int index) {
        return this.collection.get(index);
    }
}
