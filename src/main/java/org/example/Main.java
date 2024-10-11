package org.example;

import jakarta.persistence.*;
public class Main {
        public static void main(String[] args) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
            EntityManager em = emf.createEntityManager();
            EntityTransaction transaction = em.getTransaction();

            try {
                transaction.begin();
                // operacje na bazie danych
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                e.printStackTrace();
            } finally {
                em.close();
                emf.close();
            }



//            RentService rentService = new RentService();
//
//            // Przykład tworzenia obiektów
//            Guest guest = new Guest();
//            guest.setName("Jan Kowalski");
//
//            Room room = new Room();
//            room.setType("Studio");
//
//            Rent rent = new Rent();
//            rent.setGuest(guest);
//            rent.setRoom(room);
//
//            // Zapis wynajmu
//            rentService.saveRent(rent);
//
//            // Zamknij serwis
//            rentService.close();



        }

}
