import java.util.*;

public class Pit {

    private static int pos;
    private static Random random = new Random();

    // Setter and getter methods for the pit's position
    public static void setPitPos(int pos) {Pit.pos = pos;}
    public static int getPitPos() {return Pit.pos;} 

    public static Map<Integer, Room> createPit(Map<Integer, Room> rooms) {
        int chosenRoom = 0; // Needs to be initialised - note that the initial value assigned is irrelevant
        boolean emptyRoom = false;
        while (emptyRoom == false) {
            chosenRoom = random.nextInt(20) + 1;
            if (rooms.get(chosenRoom).isEmpty()) {emptyRoom = true;}
        }

        Pit.setPitPos(chosenRoom); // Set pit's room to the randomly selected room
        rooms.get(chosenRoom).setPit();
        return rooms;
    }

    public static Map<Integer, Room> pitPresence(Map<Integer, Room> rooms, int pos) {
        rooms.get(rooms.get(pos).getRoomA()).setPitPresence(true);
        rooms.get(rooms.get(pos).getRoomB()).setPitPresence(true); 
        rooms.get(rooms.get(pos).getRoomC()).setPitPresence(true); 
        return rooms;
    }
}