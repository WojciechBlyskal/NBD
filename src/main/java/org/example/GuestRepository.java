package org.example;

import java.util.List;
import jakarta.persistence.*;


public class GuestRepository {

    public void addGuest(Guest guest, EntityManager em) {
        em.getTransaction().begin();
        em.persist(guest);
        em.getTransaction().commit();
        // MOŻE DODAĆ TRY CATCH
    }

    public List<Guest> getGuestByLastName(EntityManager em, String lastName) {
        String selectQuery = "SELECT g FROM Guest g where g.lastName =:lastName";
        em.getTransaction().begin();
        TypedQuery<Guest> query = em.createQuery(selectQuery, Guest.class);
        query.setParameter("lastName", lastName);
        query.setLockMode(LockModeType.OPTIMISTIC); //Sprawdz czy jak bedzie usuwamy to czy nie bedzie potrzebny Pessimistic Read
        List<Guest> guests = query.getResultList();
        em.getTransaction().commit();
        return guests;
    }

    public List<Guest> getGuestById(EntityManager em, long Id) {
        String selectQuery = "SELECT g FROM Guest g where g.Id =:Id";
        em.getTransaction().begin();
        TypedQuery<Guest> query = em.createQuery(selectQuery, Guest.class);
        query.setParameter("Id", Id);
        query.setLockMode(LockModeType.OPTIMISTIC); //Sprawdz czy jak bedzie usuwamy to czy nie bedzie potrzebny Pessimistic Read
        List<Guest> guests = query.getResultList();
        em.getTransaction().commit();
        return guests;
    }

    public List<Guest> getAllGuests(EntityManager em) {
        String selectQuery = "SELECT g FROM Guest g";
        em.getTransaction().begin();
        Query query = em.createQuery(selectQuery);
        query.setLockMode(LockModeType.OPTIMISTIC);
        List<Guest> guests = query.getResultList();
        em.getTransaction().commit();
        return guests;
    }
}
