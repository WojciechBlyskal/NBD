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

    public void addRent(Rent rent, EntityManager em) {
        Room room = rent.getRoom();
        em.getTransaction().begin();
        //Rent managedRent = em.find(Rent.class, rent.getId(), LockModeType.PESSIMISTIC_WRITE);

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
        // MOŻE DODAĆ TRY CATCH


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
}
