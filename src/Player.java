import java.util.*;


public class Player {
    static Random random = new Random();

    private static int pos;
    private static int arrows = 5;

    // Setter and getter methods for Player position and arrow count
    public static int getPos() {return pos;}
    public static void setPos(int pos) {Player.pos = pos;}
    public static int getArrow() {return arrows;}
    public static void addArrow() {Player.arrows++;}

    private static boolean hearingLoss, smellLoss;

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
            if (rooms.get(Player.pos).isEmpty()) {emptyRoom = true;}
        }
        return rooms;
    }

    // Method to move the player to a room specified by the user
    public static void movePlayer(int choice) {Player.pos = choice;}

    // Verification method to check that the room specified by the user is indeed one of the player's current adjacent rooms
    public static boolean verifyMovement(Room room, int enteredRoom) {
        return ((room.getRoomA() == enteredRoom) || (room.getRoomB()  == enteredRoom) || (room.getRoomC() == enteredRoom));}
    
    // Method to check where the arrow lands after the player shoots it    
    public static boolean shootSomething(Map<Integer, Room> rooms, Bat[] bats, Room room) {

        if (room.getWumpusR()) { // If the Wumpus is in the selected room, it is game over immediately
            System.out.println("With just one arrow drawn, slay you a beast that has bested innumerous great hunters. You win!"); 
            return true;
        }

        // Check if room has wumpusPresence toggled to true, which will decide if the Wumpus moves
        if (rooms.get(Player.pos).getWumpusPresence()) {
            System.out.println("You missed!");
            Wumpus.moveWumpus(rooms);
            if (Wumpus.getWumpusPos() != Player.pos) {
                
                if (room.getPitR() || Player.getHearingLoss()) {
                    System.out.println("You don't hear the arrow clink against the floor."); 
                    arrows -= 1;
                }

                if (room.getBatR() && !Player.getHearingLoss()) {
                    System.out.println("You hear the annoyed shrieks of a bat.");
                    arrows -= 1;
                }

                if (room.isEmpty() && !Player.getHearingLoss()) {
                    System.out.println("You hear the arrow clink against the floor."); 
                    arrows -= 1;
                }
                
                return Room.movementStatus(rooms, bats, rooms.get(Player.pos), Player.pos);

            } else {

                return Room.movementStatus(rooms, bats, rooms.get(Player.pos), Player.pos);
            }

        } else {

            if (room.getPitR() || Player.getHearingLoss()) { // For if the player shoots an arrow into the pit's room OR if the player is deaf
                System.out.println("You don't hear the arrow clink against the floor."); 
                arrows -= 1; 
                return false;
            }

            if (room.getBatR() && !Player.getHearingLoss()) {
                System.out.println("You hear the annoyed shrieks of a bat.");
                arrows -= 1;
                return false;
            }

            if (room.isEmpty() && !Player.getHearingLoss()) { // Normal miss 
                System.out.println("You hear the arrow clink against the floor."); 
                arrows -= 1; 
                return false;
            }
        }

        return false;
    }        
}
