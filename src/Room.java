import java.util.*;

public class Room {
    private int roomNumber;
    private int roomA, roomB, roomC;
    private boolean wumpus, pit, bat;
    private boolean wumpusPresence, pitPresence, batPresence;

    // Constructor
    public Room(int roomNumber, int roomA, int roomB, int roomC) {
        this.roomNumber = roomNumber;
        this.roomA = roomA;
        this.roomB = roomB;
        this.roomC = roomC;
    }

    // Getter methods for self room number and adjacent room numbers
    public int getRoomNum() {return this.roomNumber;}
    public int getRoomA() {return this.roomA;}
    public int getRoomB() {return this.roomB;}
    public int getRoomC() {return this.roomC;}
    public String getRooms() {return (this.roomA + ", " + this.roomB + ", or " + this.roomC);}

    // Getter methods for if Wumpus, Bat, or Pit is in the room
    public boolean getWumpusR() {return this.wumpus;}
    public boolean getPitR() {return this.pit;}
    public boolean getBatR() {return this.bat;}

    public boolean isEmpty() {return (!this.wumpus && !this.pit && !this.bat);}

    // Getter methods for presence of hazards
    public boolean getWumpusPresence() {return this.wumpusPresence;}
    public boolean getBatPresence() {return this.batPresence;}
    public boolean getPitPresence() {return this.pitPresence;}

    // Methods to set the status of hazards in the room
    public void setWumpus() {this.wumpus = (this.roomNumber == Wumpus.getWumpusPos()) ? true : false;}
    public void setPit() {this.pit = (this.roomNumber == Pit.getPitPos()) ? true : false;}
    public void setBat(Bat bat) { // Made this an actual if-else block instead because there was some bug & I was getting confused lol
        if (this.roomNumber == bat.getBatPos()) {
            this.bat = true;
        } else {
            this.bat = false;
        }
    }
    // Methods to set the presence of hazards in the room
    public void setWumpusPresence(boolean setter) {this.wumpusPresence = setter;}
    public void setPitPresence(boolean setter) {this.pitPresence = setter;}
    public void setBatPresence(boolean setter) {this.batPresence = setter;}

    // Checks what is in the room after the player moves into it
    public static boolean movementStatus(Map<Integer, Room> rooms, Bat[] bats, Room room, int choice) {

        if (room.getWumpusR()) {
            System.out.println("You stumbled into the Wumpus! He munches you up and throws your remains in the trash. Game over."); 
            return true;

        } else if (room.getPitR()) {System.out.println("You stumbled down a pit! Your crushed bones reverberate around the caves. Game over."); 
            return true;

        } else if (room.bat) {
            int newRoom = bats[0].batMovePlayer(rooms, room); // The object bat[0] is simply used to access the batMovePlayer() method in the Bat class
            System.out.println("Despite the laws of physics, a bat is able to pick you up and place you in room " + newRoom + ".");

            // Move bat to a random empty room after it moves the player
            for (Bat bat : bats) {if (bat.getBatPos() == choice) {bat.moveBat(rooms);}}
            for (Bat bat : bats) rooms = bat.batPresence(rooms); // Updates bat presence after moving bat(s) around
            roomPresence(rooms.get(newRoom), newRoom);
            return false;

        } else {
            roomPresence(room, choice);
            return false;
        }
    }
    
    public static void roomPresence(Room room, int choice) {
        if (room.getWumpusPresence()) {
            System.out.println("You smell the fabled wretched smell of the Wumpus, it is rancid, and it is unbearable."); 
            Player.movePlayer(choice);
        }
        
        if (room.getPitPresence()) {
            System.out.println("A cold breeze envelops you."); 
            Player.movePlayer(choice);
        }

        if (room.getBatPresence()) {
            System.out.println("Shrieks envelop the cave."); 
            Player.movePlayer(choice);
        }

        if (!room.batPresence && !room.pitPresence && !room.wumpusPresence) {
            System.out.println("The room is empty.");
            Player.movePlayer(choice); 
        }
    }

    public static Map<Integer, Room> roomAssignment() {
        Map <Integer, Room> rooms = new HashMap<>();
        Room roomOne = new Room(1, 5, 8, 2); rooms.put(1, roomOne);
        Room roomTwo = new Room(2, 1, 10, 3); rooms.put(2, roomTwo);
        Room roomThree = new Room(3, 2, 12, 4); rooms.put(3, roomThree);
        Room roomFour = new Room(4, 3, 14, 5); rooms.put(4, roomFour);
        Room roomFive = new Room(5, 4, 6, 1); rooms.put(5, roomFive);
        Room roomSix = new Room(6, 15, 5, 7); rooms.put(6, roomSix);
        Room roomSeven = new Room(7, 6, 17, 8); rooms.put(7, roomSeven);
        Room roomEight = new Room(8, 7, 1, 9); rooms.put(8, roomEight);
        Room roomNine = new Room(9, 8, 18, 10); rooms.put(9, roomNine);
        Room roomTen = new Room(10, 9, 2, 11); rooms.put(10, roomTen);
        Room roomEleven = new Room(11, 10, 19, 12); rooms.put(11, roomEleven);
        Room roomTwelve = new Room(12, 11, 3, 13); rooms.put(12, roomTwelve);
        Room roomThirteen = new Room(13, 12, 20, 14); rooms.put(13, roomThirteen);  
        Room roomFourteen = new Room(14, 13, 4, 15); rooms.put(14, roomFourteen);
        Room roomFifteen = new Room(15, 14, 16, 6); rooms.put(15, roomFifteen);
        Room roomSixteen = new Room(16, 20, 15, 17); rooms.put(16, roomSixteen);
        Room roomSeventeen = new Room(17, 16, 7, 18); rooms.put(17, roomSeventeen);
        Room roomEighteen = new Room(18, 17, 9, 19); rooms.put(18, roomEighteen);
        Room roomNineteen = new Room(19, 18, 11, 20); rooms.put(19, roomNineteen);
        Room roomTwenty = new Room(20, 19, 13, 16); rooms.put(20, roomTwenty);
        return rooms;
    }
}