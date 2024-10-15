package org.example;

import org.apache.commons.lang3.builder.ToStringBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import jakarta.persistence.*;


public class GuestRepository {

    public void addGuest(Guest guest, EntityManager em) {
//            String lastName = guest.getLastName();
//            List<Guest> guestByLastName = this.getGuestByLastName(em, lastName);
//            if (guestByLastName.isEmpty()) { ani lastName ani phoneNumber nie sa unikalne wiec czemu mam wykluczac gdy ise pokrywaja?/
        em.getTransaction().begin();
        em.persist(guest);
        em.getTransaction().commit();
        em.close();
//            }
        // MOŻE DODAĆ TRY CATCH

    }


    //public void removeGuest(Guest guest, EntityManager em) { 1.Przykazanie:Nie usuwac
        //prevent deleting a renting Guest
        /*if (getAllRentsByGuest (guest.getId()) )
        try {
            Guest managedGuest = em.find(Guest.class, guest.getId(), LockModeType.PESSIMISTIC_WRITE);
            em.getTransaction().begin();
            em.remove(managedGuest);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }*/

    public List<Guest> getGuestByLastName(EntityManager em, String lastName) {
        String selectQuery = "SELECT g FROM Guest g where g.lastName =:lastName";
        TypedQuery<Guest> query = em.createQuery(selectQuery, Guest.class);
        query.setParameter("lastName", lastName);
        query.setLockMode(LockModeType.OPTIMISTIC); //Sprawdz czy jak bedzie usuwamy to czy nie bedzie potrzebny Pessimistic Read
        return query.getResultList();
    }

    public List<Guest> getGuestById(EntityManager em, long Id) {
        String selectQuery = "SELECT g FROM Guest g where g.Id =:Id";
        TypedQuery<Guest> query = em.createQuery(selectQuery, Guest.class);
        query.setParameter("Id", Id);
        query.setLockMode(LockModeType.OPTIMISTIC); //Sprawdz czy jak bedzie usuwamy to czy nie bedzie potrzebny Pessimistic Read
        return query.getResultList();
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
