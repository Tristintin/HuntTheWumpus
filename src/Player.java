import java.util.*;


public class Player {
    static Random random = new Random();

    private static int pos;
    private static int arrows = 5;

    // Setter and getter methods for Player position and arrow count
    public static int getPlayerPos() {return pos;}
    public static void setPos(int pos) {Player.pos = pos;}
    public static int getArrow() {return arrows;}
    public static void addArrow() {Player.arrows++;}

    private static boolean hearingLoss, smellLoss;
    private static int hearingLossMoveTimer = 3; // This count decrements once the player has hearing loss, and the hearing loss effect is lost after it reaches 0, then it reset to 3 for the next time the player gets it
    private static int smellLossMoveTimer = 3;   // same proccess applies here
    public static int getHearingLossMoveTimer() {return hearingLossMoveTimer;}
    public static int getSmellLossMoveTimer() {return smellLossMoveTimer;}
    public static void decreaseHearingLossMoveTimer() {hearingLossMoveTimer -= 1;}
    public static void decreaseSmellLossMoveTimer() {smellLossMoveTimer -= 1;}
    public static void resetHearingLossMoveTimer() {hearingLossMoveTimer = 3;}
    public static void resetSmellLossMoveTimer() {smellLossMoveTimer = 3;}

    // Setter and getter methods for hearingLoss and smellLoss
    public static void setHearingLoss(boolean setter) {hearingLoss = setter;}
    public static void setSmellLoss(boolean setter) {smellLoss = setter;}
    public static boolean getHearingLoss() {return hearingLoss;}
    public static boolean getSmellLoss() {return smellLoss;}

    
    // Spawn method for player
    public static Map<Integer, Room> spawnPlayer(Map<Integer, Room> rooms) {
        boolean emptyRoom = false;
        while (emptyRoom == false) {
            Player.pos = random.nextInt(20) + 1;
            if (rooms.get(Player.pos).isEmptyPhysically()) {emptyRoom = true;}
        }
        return rooms;
    }

    // Method to move the player to a room specified by the user
    public static void movePlayer(int choice) {Player.pos = choice;}

    // Verification method to check that the room specified by the user is indeed one of the player's current adjacent rooms
    public static boolean verifyMovement(Room room, int enteredRoom) {
        return ((room.getRoomA() == enteredRoom) || (room.getRoomB()  == enteredRoom) || (room.getRoomC() == enteredRoom));}
    

    public static boolean shootSomething(Map<Integer, Room> rooms, Bat[] bats, Room room) {

        if (room.getWumpusOccupation()) { 
           if (Wumpus.getWumpusHealth() == 2) {
                System.out.println("As the arrow pierces the Wumpus and leaves him close to death, he roars and scurries away to a nearby room");
                Wumpus.lowerWumpusHealth();
                Wumpus.moveWumpus(rooms);
                Player.arrows -= 1;

                return Room.roomStatus(rooms, bats, rooms.get(Player.pos), Player.pos);
           } 
           
           else {
                System.out.println("With just two arrows drawn, slay you a beast that has bested numerous great hunters. You win!"); 
                return true;
           }
        }

        // Check if room has wumpusPresence toggled to true, which will decide if the Wumpus moves
        if (rooms.get(Player.pos).getWumpusPresence()) {
            System.out.println("You missed!");
            Wumpus.moveWumpus(rooms);

            if (Wumpus.getWumpusPos() != Player.pos) {
                checkArrowResult(rooms, bats, room);
                Player.arrows -= 1;
                return Room.roomStatus(rooms, bats, rooms.get(Player.pos), Player.pos);

            } else {return Room.roomStatus(rooms, bats, rooms.get(Player.pos), Player.pos);}

        } else {
            checkArrowResult(rooms, bats, room); // Normal miss
            Player.arrows -= 1;
            return Room.roomStatus(rooms, bats, rooms.get(Player.pos), Player.pos);
        }
    }        


    public static void checkArrowResult(Map<Integer, Room> rooms, Bat[] bats, Room room) {
        if (room.getPitOccupation() || Player.getHearingLoss())  System.out.println("You don't hear the arrow clink against the floor."); 
        else if (room.getBatOccupation() && !Player.getHearingLoss()) System.out.println("You hear the annoyed shrieks of a bat.");
        else if (room.isThreatEmpty() && !Player.getHearingLoss()) System.out.println("You hear the arrow clink against the floor."); 
    }
}
