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
        // MOŻE DODAĆ TRY CATCH I JAKIES WARUNKI

    }


    /*public void removeRoom(Room room, EntityManager em) {
        try {
            Room managedRoom = em.find(Room.class, room.getId(), LockModeType.PESSIMISTIC_WRITE);
            em.getTransaction().begin();
            em.remove(managedRoom);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }*/

    public List<Room> getRoomByNumber(EntityManager em, int number) {
        String selectQuery = "SELECT r FROM Room r where r.number =:number";
        TypedQuery<Room> query = em.createQuery(selectQuery, Room.class);
        query.setParameter("number", number);
        query.setLockMode(LockModeType.OPTIMISTIC);
        return query.getResultList();
    }

    public List<Room> getRoomById(EntityManager em, long Id) {
        String selectQuery = "SELECT g FROM Room g where g.Id =:Id";
        TypedQuery<Room> query = em.createQuery(selectQuery, Room.class);
        query.setParameter("Id", Id);
        query.setLockMode(LockModeType.OPTIMISTIC);
        return query.getResultList();
    }

    public List<Room> getAllRooms(EntityManager em) {
        String selectQuery = "SELECT r FROM Room r";
        em.getTransaction().begin();
        Query query = em.createQuery(selectQuery);
        query.setLockMode(LockModeType.OPTIMISTIC);
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
