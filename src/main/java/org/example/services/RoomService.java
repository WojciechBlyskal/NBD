package org.example.services;

import org.example.model.Room;
import org.example.repositories.RoomRepository;
//import org.example.services.Interfaces.IRoomService;

import java.util.List;
import java.util.UUID;

public class RoomService /*implements IRoomService*/ {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    //@Override
    public Room createRoom(int number, int floor, double surface, double price, boolean isStudio) {
        try {
            Room.RoomType roomType;
            if (isStudio) {
                roomType = Room.RoomType.Studio;
            } else {
                roomType = Room.RoomType.MicroSuite;
            }
            Room room = new Room(number, floor, surface, price, roomType);
            roomRepository.create(room);
            return room;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //@Override
    public boolean deleteRoom(UUID roomId) {
        try {
            Room room = roomRepository.getById(roomId);
            if (room == null) {
                return false;
            }

            roomRepository.delete(room);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //@Override
    public boolean updateRoom(Room room) {
        try {
            Room existingRoom = roomRepository.getById(room.getId());

            if (existingRoom == null) {
                return false;
            }

            roomRepository.update(room);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //@Override
    public Room getRoomById(UUID roomId) {
        try {
            return roomRepository.getById(roomId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //@Override
    public List<Room> getAllRooms() {
        try {
            return roomRepository.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
