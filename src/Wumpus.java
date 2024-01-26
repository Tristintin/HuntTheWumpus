import java.util.*;

public class Wumpus {
    private static Random random = new Random();
    private static int pos;

    // Getter method for Wumpus' position
    public static int getWumpusPos() {return Wumpus.pos;}
    
    // Checks for a random room and references the isEmpty() method which checks if there is no Wumpus, bat, or pit currently in the selected room
    public static Map<Integer, Room> spawnWumpus(Map<Integer, Room> rooms) {
        int chosenRoom = 0; // Needs to be initialised
        boolean emptyRoom = false;
        
        while (emptyRoom == false) {
            chosenRoom = random.nextInt(20) + 1;
            if (rooms.get(chosenRoom).isEmpty()) {emptyRoom = true;}
        }

        Wumpus.pos = chosenRoom;
        return rooms;  
    }

    // Method to move Wumpus + remove wumpusPresence from original rooms
    public static void moveWumpus(Map<Integer, Room> rooms) {
        int currentPos = Wumpus.pos;

        // Array of possible rooms that the Wumpus can move into
        int[] possiblePos = new int[]{currentPos, rooms.get(currentPos).getRoomA(), rooms.get(currentPos).getRoomB(), rooms.get(currentPos).getRoomC()};

        // Select a random room from the array of possible rooms
        int randomPos = possiblePos[new Random().nextInt(possiblePos.length)];

        // Reset Wumpus presence for the previous rooms adjacent to the Wumpus to false
        for (int position : possiblePos) {
            rooms.get(position).setWumpusPresence(false);
        }

        Wumpus.pos = randomPos; // Assign the wumpus the randomly generated room based on its adjacent rooms
        rooms.get(currentPos).setWumpus(); // Set Wumpus status in original room to false
        rooms.get(Wumpus.pos).setWumpus(); // Set Wumpus status in the new room to true

        rooms = wumpusPresence(rooms); // Set the Wumpus presence in the adjacent rooms of the Wumpus' new room to true
    }

    public static Map<Integer, Room> wumpusPresence(Map<Integer, Room> rooms) {
        // Use the position property to match the key-value pair, and then update that object linked with key to have wumpus presence in room{A,B,C}
        rooms.get(rooms.get(Wumpus.pos).getRoomA()).setWumpusPresence(true); 
        rooms.get(rooms.get(Wumpus.pos).getRoomB()).setWumpusPresence(true); 
        rooms.get(rooms.get(Wumpus.pos).getRoomC()).setWumpusPresence(true); 
        return rooms;
    }

}