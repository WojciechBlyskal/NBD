package org.example;

import jakarta.persistence.EntityManager;
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
            em.getTransaction().begin();
            em.persist(rent);
            em.getTransaction().commit();
            em.close();
        }
        // MOŻE DODAĆ TRY CATCH


    public void removeRent(Rent rent, EntityManager em) {
        try {
            em.getTransaction().begin();
            em.remove(rent);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    public List<Rent> getRentByRentNumber(EntityManager em, String rentNumber) {
        String selectQuery = "SELECT rn FROM Rent rn where rn.rentNumber =:rentNumber";
        TypedQuery<Rent> query = em.createQuery(selectQuery, Rent.class);
        query.setParameter("rentNumber", rentNumber);
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

    public List<Rent> getAllRentsByGuest(EntityManager entityManager, long ID) {
        String selectQuery = "SELECT rn FROM Rent rn where rn.guest.ID =:ID";
        TypedQuery<Rent> query = entityManager.createQuery(selectQuery, Rent.class);
        query.setParameter("ID", ID);
        return query.getResultList();
    }

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
