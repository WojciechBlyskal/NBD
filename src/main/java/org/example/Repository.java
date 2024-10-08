package org.example;

import org.apache.commons.lang3.builder.ToStringBuilder;
import java.util.function.Predicate;
import java.util.ArrayList;

public class Repository <T> {
    ArrayList<T> collection;

    public Repository() {
        this.collection = new ArrayList<>();
    }

    public void add(T obj) {
        if (obj != null) {
            collection.add(obj);
        }
    }

    public void remove(T obj) {
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

    public ArrayList<T> findBy(Predicate<T> predicate) {
        ArrayList<T> found = new ArrayList<>();
        found.clear();
        for (int i = 0; i < size(); i++) {
            T obj = get(i);
            if (predicate.test(obj)) {
                found.add(obj);
            }
        }
        return found;
    }

    public ArrayList<T> findAll() {
        Predicate<T> temp = obj -> true;
        return findBy(temp);
    }

    public T get (int index) {
        return this.collection.get(index);
    }
}
