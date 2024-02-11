import java.util.*;

public class Bat {
    private Random random = new Random();
    private int pos; 

    // Getter method for the bat's position
    public int getBatPos() {return this.pos;}

    // Spawner method for each bat
    public Map<Integer, Room> spawnBat(Map<Integer, Room> rooms) { 
        int chosenRoom = 0; // Needs to be initialised
        boolean emptyRoom = false;

        while (emptyRoom == false) {
            chosenRoom = random.nextInt(20) + 1;
            if (rooms.get(chosenRoom).isEmptyPhysically()) {emptyRoom = true;}
        }

        this.pos = chosenRoom;
        return rooms;
    }

    // Method to move bat into a random room - called only if the player interacts with a bat
    public Map<Integer, Room> moveBat(Map<Integer, Room> rooms) { 
        int chosenRoom = 0;
        int currentPos = this.pos;
        boolean emptyRoom = false;

        int[] surroundingPos = new int[]{currentPos, rooms.get(currentPos).getRoomA(), rooms.get(currentPos).getRoomB(), rooms.get(currentPos).getRoomC()};
        for (int position : surroundingPos) {
            rooms.get(position).setBatPresence(false);
        }

        while (emptyRoom == false) {
            chosenRoom = random.nextInt(20) + 1;
            if (rooms.get(chosenRoom).isEmptyPhysically()) emptyRoom = true;
        }

        rooms.get(currentPos).setBatOccupation(this); 
        this.pos = chosenRoom;
        rooms.get(this.pos).setBatOccupation(this);
        
        rooms = this.batPresence(rooms);
        
        return rooms;
    }

    // Method to move the player after they enter a room with a bat
    public int batMovePlayer(Map<Integer, Room> rooms, Room room) { 
        boolean emptyRoom = false;
        while (emptyRoom == false) {
            int newPlayerPos = random.nextInt(20) + 1;
            Player.setPos(newPlayerPos);
            if (rooms.get(Player.getPlayerPos()).isEmptyPhysically()) emptyRoom = true;
        }
        return Player.getPlayerPos();
    }

    // Method to set the bat presence of rooms that a adjacent to one containing a bat to true
    public Map<Integer, Room> batPresence(Map<Integer, Room> rooms) {
        rooms.get(rooms.get(this.pos).getRoomA()).setBatPresence(true);
        rooms.get(rooms.get(this.pos).getRoomB()).setBatPresence(true);
        rooms.get(rooms.get(this.pos).getRoomC()).setBatPresence(true);
        return rooms;
    }

}