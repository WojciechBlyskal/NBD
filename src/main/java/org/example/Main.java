package org.example;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hotelPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        Guest guest1 = new Guest("Jan", "Kowalski", "123456789");
        Guest guest2 = new Guest("Marek", "Nowak", "987654321");
        Guest guest3 = new Guest("Zofia", "Nowak", "456123789");

        Studio studio1 = new Studio(12, 3, 30.13, true, 244.99);
        MultipleRoom multipleRoom1 = new MultipleRoom(8, 1, 50.78, true, 380.5, 2);
        MicroSuite microSuite1 = new MicroSuite(56, 6, 18.28, false, 59.9);

        LocalDateTime startTime1 = LocalDateTime.of(2024, 10, 15, 12, 30); // 15 października 2024, 12:30
        LocalDateTime startTime2 = LocalDateTime.of(2022, 5, 19, 20, 12);
        Rent rent1 = new Rent("r1", startTime1, microSuite1, guest1);
        Rent rent2 = new Rent("r2", startTime2, multipleRoom1, guest2);

        GuestRepository guestRepository = new GuestRepository();
        RoomRepository roomRepository = new RoomRepository();
        RentRepository rentRepository = new RentRepository();

        guestRepository.addGuest(guest1, em);
        guestRepository.addGuest(guest2, em);
        guestRepository.addGuest(guest3, em);

        roomRepository.addRoom(studio1, em);
        roomRepository.addRoom(multipleRoom1, em);
        roomRepository.addRoom(microSuite1, em);

        rentRepository.addRent(rent1, em);
        rentRepository.addRent(rent2, em);


        List<Guest> guests = guestRepository.getAllGuests(em);

        for (Guest guest : guests) {
            System.out.println("Guest ID: " + guest.getId());
            System.out.println("Name: " + guest.getName());
            System.out.println("Last Name: " + guest.getLastName());
            System.out.println("Phone Number: " + guest.getPhoneNumber());
            System.out.println("---------------------------------");
        }


        List<Guest> guests2 = guestRepository.getGuestByLastName(em, "Kowalski");
        for (Guest guest : guests2) {
            System.out.println("Guest ID: " + guest.getId());
            System.out.println("Name: " + guest.getName());
            System.out.println("Last Name: " + guest.getLastName());
            System.out.println("Phone Number: " + guest.getPhoneNumber());
            System.out.println("---------------------------------");
        }
        guest1.setName("Piotr");

        List<Guest> guests3 = guestRepository.getGuestByLastName(em, "Kowalski");

        for (Guest guest : guests3) {
            System.out.println("Guest ID: " + guest.getId());
            System.out.println("Name: " + guest.getName());
            System.out.println("Last Name: " + guest.getLastName());
            System.out.println("Phone Number: " + guest.getPhoneNumber());
            System.out.println("---------------------------------");
        }

//            System.out.println(guestRepository.getGuestByLastName(em, "Kowalski"));





//            roomRepository.getAllRooms(em);
//            rentRepository.getAllRents(em);





        em.close();
        emf.close();




//            try {
//                transaction.begin();
//                // operacje na bazie danych
//                transaction.commit();
//            } catch (Exception e) {
//                if (transaction.isActive()) {
//                    transaction.rollback();
//                }
//                e.printStackTrace();
//            } finally {
//                em.close();
//                emf.close();
//            }



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
