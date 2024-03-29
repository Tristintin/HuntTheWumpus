import java.util.*;


public class Room {
    private int roomNumber;
    private int roomA, roomB, roomC;
    private boolean wumpusOcc, pitOcc, batOcc;
    private boolean wumpusPresence, pitPresence, batPresence;
    private boolean hearingLoss, smellLoss;
    private boolean arrow;

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

    // Setter and getter methods for hearing and smell loss effects
    public void setHearingLoss(boolean setter) {this.hearingLoss = setter;}
    public void setSmellLoss(boolean setter) {this.smellLoss = setter;}
    public boolean getHearingLoss() {return this.hearingLoss;}
    public boolean getSmellLoss() {return this.smellLoss;}

    // Setter and getter methods for arrow in room
    public void setArrow(boolean setter) {this.arrow = setter;}
    public boolean getArrow() {return this.arrow;}

    // Method to spawn arrow in random empty room
    public static Map<Integer, Room> placeArrow(Map<Integer, Room> rooms) {
        Random randomArrowRoom = new Random();
        boolean placeableRoom = false;
        int arrowRoom = 0;
        
        while (!placeableRoom) {
            arrowRoom = randomArrowRoom.nextInt(20) + 1;
            if (rooms.get(arrowRoom).isEmptyPhysically()) placeableRoom = true;
        }

        rooms.get(arrowRoom).setArrow(true);
        return rooms;
    }

    // Method to check if the room is empty (ie. free of any hazards)
    public boolean isThreatEmpty() {
        return (!this.wumpusOcc && !this.pitOcc && !this.batOcc);
    }
    public boolean isEmptyPhysically() {
        return (isThreatEmpty() && !this.hearingLoss && !this.smellLoss && !this.arrow);
    }
    public boolean isEmptyAll() {
        return (isEmptyPhysically() && !this.batPresence && !this.pitPresence && !this.wumpusPresence);
    }

    // Getter methods for if Wumpus, Bat, or Pit is in the room
    public boolean getWumpusOccupation() {return this.wumpusOcc;}
    public boolean getPitOccupation() {return this.pitOcc;}
    public boolean getBatOccupation() {return this.batOcc;}
    
    // Methods to set the status of hazards in the room
    public void setWumpusOccupation() {this.wumpusOcc = (this.roomNumber == Wumpus.getWumpusPos()) ? true : false;}
    public void setPitOccupation() {this.pitOcc = (this.roomNumber == Pit.getPitPos()) ? true : false;}
    public void setBatOccupation(Bat bat) {
        if (this.roomNumber == bat.getBatPos()) {this.batOcc = true;} 
        else {this.batOcc = false;}
    }

    // Getter methods for presence of hazards
    public boolean getWumpusPresence() {return this.wumpusPresence;}
    public boolean getBatPresence() {return this.batPresence;}
    public boolean getPitPresence() {return this.pitPresence;}

    // Methods to set the presence of hazards in the room
    public void setWumpusPresence(boolean setter) {this.wumpusPresence = setter;}
    public void setPitPresence(boolean setter) {this.pitPresence = setter;}
    public void setBatPresence(boolean setter) {this.batPresence = setter;}

    // Method to set hearing/smell loss effects
    public static Map<Integer, Room> setEffects(Map<Integer, Room> rooms) {
        Random randomRoom = new Random();

        int hearingLossRoom = 0;
        int smellLossRoom = 0;
        boolean roomIsEmpty = false;

        while (!roomIsEmpty) {
            hearingLossRoom = randomRoom.nextInt(20) + 1;
            if (rooms.get(hearingLossRoom).isEmptyAll()) {roomIsEmpty = true;}
        }

        rooms.get(hearingLossRoom).setHearingLoss(true);
        roomIsEmpty = false;

        while (!roomIsEmpty) {
            smellLossRoom = randomRoom.nextInt(20) + 1;
            if (rooms.get(smellLossRoom).isEmptyAll()) {roomIsEmpty = true;}
        }
        rooms.get(smellLossRoom).setSmellLoss(true);

        return rooms;
    }


    // Checks what is in the room after the player moves into it
    public static boolean roomStatus(Map<Integer, Room> rooms, Bat[] bats, Room room, int choice) {

        System.out.println(); // For formatting purposes

        if (room.getWumpusOccupation()) {
            System.out.println("You stumbled into the Wumpus! He munches you up and throws your remains in the trash. Game over."); 
            return true;

        } else if (room.getPitOccupation()) {
            System.out.println("You stumbled down a pit! Your crushed bones reverberate around the caves. Game over."); 
            return true;

        } else if (room.getBatOccupation()) {
            int newRoom = bats[0].batMovePlayer(rooms, room); // The object bat[0] is simply used to access the batMovePlayer() method in the Bat class
            System.out.println("Despite the laws of physics, a bat is able to pick you up and place you in room " + newRoom + ".");

            // Move bat to a random empty room after it moves the player
            for (Bat bat : bats) {if (bat.getBatPos() == choice) {bat.moveBat(rooms);}}
            for (Bat bat : bats) rooms = bat.batPresence(rooms);                         // Updates bat presence after moving bat(s) around

            roomPresence(rooms.get(newRoom), newRoom);
            return false;

        } else if (room.getHearingLoss() && !Player.getHearingLoss()) {
            System.out.println("Oh no! You have been affected by the HEARING LOSS effect! You can no longer hear bats!");
            Player.setHearingLoss(true);                // Applies effect on player
            rooms.get(choice).setHearingLoss(false);    // Removes the effect from the room

            boolean newHearingLoss = false;
            int newRoom = 0;
            Random random = new Random();

            while (!newHearingLoss) {
                newRoom = random.nextInt(20) + 1;
                if (rooms.get(newRoom).isEmptyAll()) {newHearingLoss = true;}
            }

            rooms.get(newRoom).setHearingLoss(true);
            roomPresence(room, choice);
            return false;

        } else if (room.getSmellLoss() && !Player.getSmellLoss()) {
            System.out.println("Oh no! You have been affected by the SMELL LOSS effect! You can no longer smell the Wumpus!");
            Player.setSmellLoss(true);
            rooms.get(choice).setSmellLoss(false);
            boolean newHearingLoss = false;
            int newRoom = 0;
            Random random = new Random();

            while (!newHearingLoss) {
                newRoom = random.nextInt(20) + 1;
                if (rooms.get(newRoom).isEmptyAll()) {newHearingLoss = true;}
            }

            rooms.get(newRoom).setSmellLoss(true);
            roomPresence(room, choice);
            return false;

        } else {
            roomPresence(room, choice);
            return false;

        }
    }
    

    // Check the room for the presence of any hazards (ie. if any hazards are in the adjacent rooms)
    public static void roomPresence(Room room, int choice) {
        if (room.getWumpusPresence()) {
            if (!Player.getSmellLoss()) {
                System.out.println("You smell the fabled wretched smell of the Wumpus, it is rancid, and it is unbearable."); 
            }
        }
        
        if (room.getPitPresence()) {System.out.println("A cold breeze envelops you."); }

        if (room.getBatPresence()) {
            if (!Player.getHearingLoss()) {
                System.out.println("Shrieks envelop the cave."); 
            }
        }

        if ((!room.getBatPresence() && !room.getPitPresence() && !room.getWumpusPresence()) ||
            (!room.getPitPresence() && !room.getWumpusPresence() && Player.getHearingLoss()) ||
            (!room.getPitPresence() && !room.getBatPresence() && Player.getSmellLoss()) ||
            (!room.getPitPresence() && Player.getHearingLoss() && Player.getSmellLoss())) {
            System.out.println("The room is empty.");
        }

        if (room.getArrow()) {
            System.out.println("You found an arrow!");
            Player.addArrow();
            room.setArrow(false);
        }

        Player.movePlayer(choice);
    }


    // Build the cave system using a HashMap
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
