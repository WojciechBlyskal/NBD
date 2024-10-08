package org.example;

import org.apache.commons.lang3.builder.ToStringBuilder;

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

    public T get (int index) {
        return this.collection.get(index);
    }
}
