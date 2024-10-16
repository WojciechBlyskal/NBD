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

    public void updateGuestName(EntityManager em, long guestId, String newName) {
        try {
            if (newName.isBlank()) {
                throw new RuntimeException("New name cannot be blank.");
            }
            em.getTransaction().begin();
            Guest guest = em.find(Guest.class, guestId, LockModeType.PESSIMISTIC_WRITE);
            guest.setName(newName);
            em.merge(guest);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update guest's name: " + e.getMessage(), e);
        }
    }

    public String getGuestName(EntityManager em, long Id) {
        String selectQuery = "SELECT g FROM Guest g WHERE g.Id = :Id";
        em.getTransaction().begin();
        TypedQuery<Guest> query = em.createQuery(selectQuery, Guest.class);
        query.setParameter("Id", Id);
        query.setLockMode(LockModeType.OPTIMISTIC);
        Guest guest = query.getSingleResult();
        em.getTransaction().commit();
        return guest.getName();
    }

    public void updateGuestLastName(EntityManager em, long guestId, String newLastName) {
        try {
            if (newLastName.isBlank()) {
                throw new RuntimeException("New last name cannot be blank.");
            }
            em.getTransaction().begin();
            Guest guest = em.find(Guest.class, guestId, LockModeType.PESSIMISTIC_WRITE);
            guest.setLastName(newLastName);
            em.merge(guest);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update guest's last name: " + e.getMessage(), e);
        }
    }

    public String getGuestLastName(EntityManager em, long Id) {
        String selectQuery = "SELECT g FROM Guest g WHERE g.Id = :Id";
        em.getTransaction().begin();
        TypedQuery<Guest> query = em.createQuery(selectQuery, Guest.class);
        query.setParameter("Id", Id);
        query.setLockMode(LockModeType.OPTIMISTIC);
        Guest guest = query.getSingleResult();
        em.getTransaction().commit();
        return guest.getLastName();
    }

    public void updateGuestPhoneNumber(EntityManager em, long guestId, String newPhoneNumber) {
        try {
            if (newPhoneNumber.isBlank()) {
                throw new RuntimeException("New phone number cannot be blank.");
            }
            em.getTransaction().begin();
            Guest guest = em.find(Guest.class, guestId, LockModeType.PESSIMISTIC_WRITE);
            guest.setPhoneNumber(newPhoneNumber);
            em.merge(guest);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update guest's phone number: " + e.getMessage(), e);
        }
    }

    public String getGuestPhoneNumber(EntityManager em, long Id) {
        String selectQuery = "SELECT g FROM Guest g WHERE g.Id = :Id";
        em.getTransaction().begin();
        TypedQuery<Guest> query = em.createQuery(selectQuery, Guest.class);
        query.setParameter("Id", Id);
        query.setLockMode(LockModeType.OPTIMISTIC);
        Guest guest = query.getSingleResult();
        em.getTransaction().commit();
        return guest.getPhoneNumber();
    }
}
