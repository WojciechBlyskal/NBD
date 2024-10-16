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
    }

    public List<Room> getRoomByNumber(EntityManager em, int number) {
        String selectQuery = "SELECT r FROM Room r where r.number =:number";
        em.getTransaction().begin();
        TypedQuery<Room> query = em.createQuery(selectQuery, Room.class);
        query.setParameter("number", number);
        query.setLockMode(LockModeType.OPTIMISTIC);
        List<Room> rooms = query.getResultList();
        em.getTransaction().commit();
        return rooms;
    }

    public List<Room> getRoomById(EntityManager em, long Id) {
        String selectQuery = "SELECT g FROM Room g where g.Id =:Id";
        em.getTransaction().begin();
        TypedQuery<Room> query = em.createQuery(selectQuery, Room.class);
        query.setParameter("Id", Id);
        query.setLockMode(LockModeType.OPTIMISTIC);
        List<Room> rooms = query.getResultList();
        em.getTransaction().commit();
        return rooms;
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

    public void updateRoomNumber(EntityManager em, long roomId, int newNumber) {
        try {
            if (newNumber < 0) {
                throw new RuntimeException("New room number cannot be negative.");
            }
            em.getTransaction().begin();
            Room room = em.find(Room.class, roomId, LockModeType.PESSIMISTIC_WRITE);
            room.setNumber(newNumber);
            em.merge(room);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update room number: " + e.getMessage(), e);
        }
    }

    public double getRoomPrice(EntityManager em, long Id) {
        String selectQuery = "SELECT r FROM Room r WHERE r.Id = :Id";
        em.getTransaction().begin();
        TypedQuery<Room> query = em.createQuery(selectQuery, Room.class);
        query.setParameter("Id", Id);
        query.setLockMode(LockModeType.OPTIMISTIC);
        Room room = query.getSingleResult();
        em.getTransaction().commit();
        return room.getPrice();
    }

    public void updateRoomPrice(EntityManager em, long roomId, double newPrice) {
        try {
            if (newPrice < 0) {
                throw new RuntimeException("New price cannot be negative.");
            }
            em.getTransaction().begin();
            Room room = em.find(Room.class, roomId, LockModeType.PESSIMISTIC_WRITE);
            room.setPrice(newPrice);
            em.merge(room);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update room's price: " + e.getMessage(), e);
        }
    }

    public int getRoomFloor(EntityManager em, long Id) {
        String selectQuery = "SELECT r FROM Room r WHERE r.Id = :Id";
        em.getTransaction().begin();
        TypedQuery<Room> query = em.createQuery(selectQuery, Room.class);
        query.setParameter("Id", Id);
        query.setLockMode(LockModeType.OPTIMISTIC);
        Room room = query.getSingleResult();
        em.getTransaction().commit();
        return room.getFloor();
    }

    public double getRoomSurface(EntityManager em, long Id) {
        String selectQuery = "SELECT r FROM Room r WHERE r.Id = :Id";
        em.getTransaction().begin();
        TypedQuery<Room> query = em.createQuery(selectQuery, Room.class);
        query.setParameter("Id", Id);
        query.setLockMode(LockModeType.OPTIMISTIC);
        Room room = query.getSingleResult();
        em.getTransaction().commit();
        return room.getSurface();
    }

    public boolean isRoomBalcony(EntityManager em, long Id) {
        String selectQuery = "SELECT r FROM Room r WHERE r.Id = :Id";
        em.getTransaction().begin();
        TypedQuery<Room> query = em.createQuery(selectQuery, Room.class);
        query.setParameter("Id", Id);
        query.setLockMode(LockModeType.OPTIMISTIC);
        Room room = query.getSingleResult();
        em.getTransaction().commit();
        return room.isBalcony();
    }
}
