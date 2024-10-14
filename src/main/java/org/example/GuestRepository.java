package org.example;

import org.apache.commons.lang3.builder.ToStringBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import jakarta.persistence.*;


public class GuestRepository {

    public void addGuest(Guest guest, EntityManager em) {
            String phoneNumber = guest.getPhoneNumber();
            List<Guest> guestByPhoneNumber = this.getGuestByPhoneNumber(em, phoneNumber);
            if (guestByPhoneNumber.isEmpty()) {
                em.getTransaction().begin();
                em.persist(guest);
                em.getTransaction().commit();
//                em.close();
            }
        // MOŻE DODAĆ TRY CATCH

    }


    public void removeGuest(Guest guest, EntityManager em) {
        try {
            em.getTransaction().begin();
            em.remove(guest);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    public List<Guest> getGuestByPhoneNumber(EntityManager em, String phoneNumber) {
        String selectQuery = "SELECT g FROM Guest g where g.phoneNumber =:phoneNumber";
        TypedQuery<Guest> query = em.createQuery(selectQuery, Guest.class);
        query.setParameter("phoneNumber", phoneNumber);
        return query.getResultList();
    }

    public List<Guest> getAllGuests(EntityManager em) {
        String selectQuery = "SELECT g FROM Guest g";
        em.getTransaction().begin();
        Query query = em.createQuery(selectQuery);
        List<Guest> guests = query.getResultList();
        em.getTransaction().commit();
        return guests;
    }


}
