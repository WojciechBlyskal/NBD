package org.example.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import java.util.function.Predicate;
import java.util.ArrayList;

public class RoomRepository<Room> {
    ArrayList<Room> collection;

    public RoomRepository() {
        this.collection = new ArrayList<>();
    }

    public void add(Room obj) {
        if (obj != null) {
            collection.add(obj);
        }
    }

    public void remove(Room obj) {
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

    public ArrayList<Room> findBy(Predicate<Room> predicate) {
        ArrayList<Room> found = new ArrayList<>();
        found.clear();
        for (int i = 0; i < size(); i++) {
            Room obj = get(i);
            if (predicate.test(obj)) {
                found.add(obj);
            }
        }
        return found;
    }

    public ArrayList<Room> findAll() {
        Predicate<Room> temp = obj -> true;
        return findBy(temp);
    }

    public Room get (int index) {
        return this.collection.get(index);
    }
}
