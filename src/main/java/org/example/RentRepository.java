package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RentRepository {

    public void addRent(Rent rent, EntityManager em) {
        Room room = rent.getRoom();
        em.getTransaction().begin();

        String queryString = "SELECT r FROM Rent r WHERE r.room = :room AND r.endTime IS NULL";
        List<Rent> existingRents = em.createQuery(queryString, Rent.class)
                .setParameter("room", room)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList();
        if (!existingRents.isEmpty()) {
            em.getTransaction().rollback();
            throw new IllegalStateException("This room is currently rented and cannot be rented to anybody else.");
        }
        em.persist(rent);
        em.getTransaction().commit();
    }


    public List<Rent> getRentByRentNumber(EntityManager em, String rentNumber) {
        String selectQuery = "SELECT rn FROM Rent rn where rn.rentNumber =:rentNumber";
        em.getTransaction().begin();
        TypedQuery<Rent> query = em.createQuery(selectQuery, Rent.class);
        query.setParameter("rentNumber", rentNumber);
        query.setLockMode(LockModeType.OPTIMISTIC);
        List<Rent> rents = query.getResultList();
        em.getTransaction().commit();
        return rents;
    }

    public List<Rent> getAllRents(EntityManager em) {
        String selectQuery = "SELECT rn FROM Rent rn";
        em.getTransaction().begin();
        Query query = em.createQuery(selectQuery);
        List<Rent> rents = query.getResultList();
        em.getTransaction().commit();
        return rents;
    }

    public List<Rent> getAllRentsByGuest(EntityManager em, long Id) {
        String selectQuery = "SELECT rn FROM Rent rn where rn.guest.Id =:Id";
        em.getTransaction().begin();
        TypedQuery<Rent> query = em.createQuery(selectQuery, Rent.class);
        query.setParameter("Id", Id);
        query.setLockMode(LockModeType.OPTIMISTIC);
        List<Rent> rents = query.getResultList();
        em.getTransaction().commit();
        return rents;
    }

    public String getRentNumber(EntityManager em, long Id) {
        String selectQuery = "SELECT r FROM Rent r WHERE r.Id = :Id";
        em.getTransaction().begin();
        TypedQuery<Rent> query = em.createQuery(selectQuery, Rent.class);
        query.setParameter("Id", Id);
        query.setLockMode(LockModeType.OPTIMISTIC);
        Rent rent = query.getSingleResult();
        em.getTransaction().commit();
        return rent.getRentNumber();
    }

    public LocalDateTime getRentStartTime(EntityManager em, long Id) {
        String selectQuery = "SELECT r FROM Rent r WHERE r.Id = :Id";
        em.getTransaction().begin();
        TypedQuery<Rent> query = em.createQuery(selectQuery, Rent.class);
        query.setParameter("Id", Id);
        query.setLockMode(LockModeType.OPTIMISTIC);
        Rent rent = query.getSingleResult();
        em.getTransaction().commit();
        return rent.getStartTime();
    }

    public void updateRentEndTime(EntityManager em, long Id, LocalDateTime endTime) {
        try {
            if (this.getRentEndTime(em, Id) != null) {
                throw new RuntimeException("End time once set cannot be changed.");
            } else if (endTime == null) {
                throw new RuntimeException("New end time cannot be null.");
            } else if (endTime.isBefore(this.getRentStartTime(em, Id))) {
                throw new RuntimeException("New end time cannot be before start time.");
            }
            em.getTransaction().begin();
            Rent rent = em.find(Rent.class, Id, LockModeType.PESSIMISTIC_WRITE);
            rent.setEndTime(endTime);
            em.merge(rent);
            em.getTransaction().commit();
        } catch(RuntimeException e){
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to set rent's end time: " + e.getMessage(), e);
        }
    }

    public LocalDateTime getRentEndTime(EntityManager em, long Id) {
        String selectQuery = "SELECT r FROM Rent r WHERE r.Id = :Id";
        em.getTransaction().begin();
        TypedQuery<Rent> query = em.createQuery(selectQuery, Rent.class);
        query.setParameter("Id", Id);
        query.setLockMode(LockModeType.OPTIMISTIC);
        Rent rent = query.getSingleResult();
        em.getTransaction().commit();
        return rent.getEndTime();
    }

    public Room getRentRoom(EntityManager em, long Id) {
        String selectQuery = "SELECT r FROM Rent r WHERE r.Id = :Id";
        em.getTransaction().begin();
        TypedQuery<Rent> query = em.createQuery(selectQuery, Rent.class);
        query.setParameter("Id", Id);
        query.setLockMode(LockModeType.OPTIMISTIC);
        Rent rent = query.getSingleResult();
        em.getTransaction().commit();
        return rent.getRoom();
    }

    public Guest getRentGuest(EntityManager em, long Id) {
        String selectQuery = "SELECT r FROM Rent r WHERE r.Id = :Id";
        em.getTransaction().begin();
        TypedQuery<Rent> query = em.createQuery(selectQuery, Rent.class);
        query.setParameter("Id", Id);
        query.setLockMode(LockModeType.OPTIMISTIC);
        Rent rent = query.getSingleResult();
        em.getTransaction().commit();
        return rent.getGuest();
    }
}
