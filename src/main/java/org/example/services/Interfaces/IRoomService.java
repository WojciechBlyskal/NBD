package org.example.services.Interfaces;

import org.example.model.Room;

import java.util.List;
import java.util.UUID;

public interface IRoomService {

    Room createRoom(int number, int floor, double surface, double price, boolean isStudio);
    boolean deleteRoom(UUID roomId);
    boolean updateRoom(Room room);
    Room getRoomById(UUID roomId);
    List<Room> getAllRooms();
}