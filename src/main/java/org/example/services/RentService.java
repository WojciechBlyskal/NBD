package org.example.services;

import org.example.model.Guest;
import org.example.model.Rent;
import org.example.model.Room;
import org.example.repositories.EntityRepository;
import org.example.services.Interfaces.IRentService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class RentService implements IRentService {
    private final EntityRepository<Rent> rentRepository;

    public RentService(EntityRepository<Rent> rentRepository) {
        this.rentRepository = rentRepository;
    }

    @Override
    public Rent addRent(LocalDate startTime, Guest guest, Room room) {
        try {
            Rent rent = new Rent(startTime, guest, room);
            rentRepository.create(rent);
            return rent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteRent(UUID rentId, UUID userId) {
        try {
            Rent rent = rentRepository.getById(rentId);
            if (rent == null) {
                return false;
            }

            rentRepository.delete(rent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateRent(Rent rent, UUID userId) {
        try {
            Rent existingRent = rentRepository.getById(rent.getId());
            if (existingRent == null) {
                return false;
            }
            rentRepository.update(rent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Rent getRentById(UUID rentId) {
        return rentRepository.getById(rentId);
    }

    @Override
    public List<Rent> getAllRents() {
        try {
            return rentRepository.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
