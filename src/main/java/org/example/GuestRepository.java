package org.example;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.function.Predicate;

public class GuestRepository<Guest> {
    ArrayList<Guest> collection;

    public GuestRepository() {
        this.collection = new ArrayList<>();
    }

    public void add(Guest obj) {
        if (obj != null) {
            collection.add(obj);
        }
    }

    public void remove(Guest obj) {
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

    public ArrayList<Guest> findBy(Predicate<Guest> predicate) {
        ArrayList<Guest> found = new ArrayList<>();
        found.clear();
        for (int i = 0; i < size(); i++) {
            Guest obj = get(i);
            if (predicate.test(obj)) {
                found.add(obj);
            }
        }
        return found;
    }

    public ArrayList<Guest> findAll() {
        Predicate<Guest> temp = obj -> true;
        return findBy(temp);
    }

    public Guest get (int index) {
        return this.collection.get(index);
    }
}
