package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RentRepository {
//    ArrayList<Rent> collection;

//    public RentRepository() {
//        this.collection = new ArrayList<>();
//    }

    public void addRent(Rent rent, EntityManager em) {
        Room room = rent.getRoom();
        Rent managedRent = em.find(Rent.class, rent.getId(), LockModeType.PESSIMISTIC_WRITE);
        em.getTransaction().begin();
        String queryString = "SELECT r FROM Rent r WHERE r.room = :room AND r.endTime IS NULL";
        List<Rent> existingRents = em.createQuery(queryString, Rent.class)
                .setParameter("room", room)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList();
        if (!existingRents.isEmpty()) {
            em.getTransaction().rollback();
            throw new IllegalStateException("Room is currently rented and cannot be rented to anybody else.");
        }
        em.persist(rent);
        em.getTransaction().commit();
        em.close();
    }
        // MOŻE DODAĆ TRY CATCH


    /*public void removeRent(Rent rent, EntityManager em) {
        if (rent.getEndTime() != null) {
            try {
                Rent managedRent = em.find(Rent.class, rent.getId(), LockModeType.PESSIMISTIC_WRITE);
                em.getTransaction().begin();
                em.remove(managedRent);
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            }
        }
    }*/

    public List<Rent> getRentByRentNumber(EntityManager em, String rentNumber) {
        String selectQuery = "SELECT rn FROM Rent rn where rn.rentNumber =:rentNumber";
        TypedQuery<Rent> query = em.createQuery(selectQuery, Rent.class);
        query.setParameter("rentNumber", rentNumber);
        query.setLockMode(LockModeType.OPTIMISTIC);
        return query.getResultList();
    }

    public List<Rent> getAllRents(EntityManager em) {
        String selectQuery = "SELECT rn FROM Rent rn";
        em.getTransaction().begin();
        Query query = em.createQuery(selectQuery);
        List<Rent> rents = query.getResultList();
        em.getTransaction().commit();
        return rents;
    }

    public List<Rent> getAllRentsByGuest(EntityManager entityManager, long Id) {
        String selectQuery = "SELECT rn FROM Rent rn where rn.guest.Id =:Id";
        TypedQuery<Rent> query = entityManager.createQuery(selectQuery, Rent.class);
        query.setParameter("Id", Id);
        query.setLockMode(LockModeType.OPTIMISTIC);
        return query.getResultList();
    }
/*
    public boolean hasActiveRents(EntityManager em, long guestId) {
        String selectQuery = "SELECT COUNT(rn) FROM Rent rn WHERE rn.guest.id = :guestId AND rn.endTime IS NULL";
        Query query = em.createQuery(selectQuery);
        query.setParameter("guestId", guestId);
        query.setLockMode(LockModeType.OPTIMISTIC);
        Long count = (Long) query.getSingleResult();
        return count > 0;
    }*/

//    public Rent get (int index) {
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
