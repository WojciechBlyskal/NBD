package org.example;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.function.Predicate;
import java.util.ArrayList;
import jakarta.persistence.*;


public class RoomRepository {


    public void addRoom(Room room, EntityManager em) {
            em.getTransaction().begin();
            em.persist(room);
            em.getTransaction().commit();
            em.close();
        // MOŻE DODAĆ TRY CATCH I JAKIES WARUNKI

    }


    public void removeRoom(Room room, EntityManager em) {
        try {
            em.getTransaction().begin();
            em.remove(room);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    public List<Room> getRoomByNumber(EntityManager em, int number) {
        String selectQuery = "SELECT r FROM Room r where r.number =:number";
        TypedQuery<Room> query = em.createQuery(selectQuery, Room.class);
        query.setParameter("number", number);
        return query.getResultList();
    }

    public List<Room> getAllRooms(EntityManager em) {
        String selectQuery = "SELECT r FROM Room r";
        em.getTransaction().begin();
        Query query = em.createQuery(selectQuery);
        List<Room> rooms = query.getResultList();
        em.getTransaction().commit();
        return rooms;
    }

//    public Room get (int index) {
//        return this.collection.get(index);
//    }

    //    public int size() {
//        return collection.size();
//    }
//
//    public String report() {
//        return new ToStringBuilder(this)
//                .append("collection", collection)
//                .toString();
//    }


}
