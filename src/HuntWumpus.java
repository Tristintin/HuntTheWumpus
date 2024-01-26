import java.util.*;

public class HuntWumpus {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String choice;
        boolean gameOver = false;
        boolean movementVerified, arrowVerified;

        System.out.println("");

        // Initialize our cave system
        Map<Integer, Room> rooms = Room.roomAssignment();

        // Spawn pit
        rooms = Pit.createPit(rooms); // Not sure what adding the result of creating a pit adds to rooms
        rooms.get(Pit.getPitPos()).setPit();

        // Spawn Wumpus
        rooms = Wumpus.spawnWumpus(rooms);
        rooms.get(Wumpus.getWumpusPos()).setWumpus();

        // Spawn bats
        Bat bats[] = new Bat[4];
        for (int i = 0; i < 4; i++) {
            bats[i] = new Bat();
            rooms = bats[i].spawnBat(rooms);
            rooms.get(bats[i].getBatPos()).setBat(bats[i]);
        }
        
        // Spawn player
        rooms = Player.spawnPlayer(rooms);

        // Set presence for pit, Wumpus and bats
        rooms = Pit.pitPresence(rooms, Pit.getPitPos());
        rooms = Wumpus.wumpusPresence(rooms);
        for (Bat bat : bats) rooms = bat.batPresence(rooms);

        // Prints out the initial presence detection of the player
        Room.movementStatus(rooms, bats, rooms.get(Player.getPos()), Player.getPos());

        // Loop that continues to let player make choices until he wins or loses
        while (gameOver == false) {

            System.out.println("You are in room " + Player.getPos() + ". Do you want to |move| or |shoot| into the adjacent rooms " + rooms.get(Player.getPos()).getRooms() + "?");
            System.out.println(""); 
            choice = scanner.nextLine();

            if (choice.equals("move")) {
                System.out.println("Which room would you like to move into?");
                System.out.println("");

                // Exception handling for NumberFormatException
                int choiceNum = Player.getPos();
                do {
                    movementVerified = true;
                    choice = scanner.nextLine();

                    try {
                        choiceNum = Integer.parseInt(choice);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input - please key in the rooms with appropriate numerical values as displayed!");
                        System.out.println("You are in room " + Player.getPos() + ". Which rooms out of the adjacent rooms: " + rooms.get(Player.getPos()).getRooms() + " do you want to move into?"); 
                        System.out.println(""); 
                        movementVerified = false;
                    }

                    if (!Player.verifyMovement(rooms.get(Player.getPos()), choiceNum) && movementVerified != false) {
                        System.out.println("Make sure your entry is valid by choosing one of the three rooms presented!"); 
                        System.out.println("You are in room " + Player.getPos() + ". Which rooms out of the adjacent rooms: " + rooms.get(Player.getPos()).getRooms() + " do you want to move into?"); 
                        System.out.println(""); 
                        movementVerified = false;
                    }
                } while (!movementVerified);

                System.out.println();
                gameOver = Room.movementStatus(rooms, bats, rooms.get(choiceNum), choiceNum); 

            } else if (choice.equals("shoot")) {
                System.out.println("Which room would you like to shoot an arrow into?");
                System.out.println("");

                // Exception handling for NumberFormatException
                int choiceNum = Player.getPos();
                do {
                    arrowVerified = true;
                    choice = scanner.nextLine();

                    try {
                        choiceNum = Integer.parseInt(choice);   //Converts string to int, so we can check if its invalid as a number or not
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input - please key in the rooms with appropriate numerical values as displayed!");
                        System.out.println("You are in room " + Player.getPos() + ". Which rooms out of the adjacent rooms: " + rooms.get(Player.getPos()).getRooms() + " do you want to shoot into?");
                        System.out.println(""); 
                        arrowVerified = false;
                    }

                    if (!Player.verifyMovement(rooms.get(Player.getPos()), choiceNum) && arrowVerified != false) {
                        System.out.println("Invalid input - please choose one of the three adjacent roooms presented!"); 
                        System.out.println("You are in room " + Player.getPos() + ". Which rooms out of the adjacent rooms: " + rooms.get(Player.getPos()).getRooms() + " do you want to shoot into?"); 
                        System.out.println("");
                        arrowVerified = false;
                    }

                } while (!arrowVerified);

                System.out.println();
                gameOver = Player.shootSomething(rooms, bats, rooms.get(choiceNum)); 

                if (Player.getArrow() == 0) {
                    gameOver = true;
                    System.out.println("All arrows have been used up! Game over.");
                }

            } else System.out.println("Invalid input - Please enter either \"move\" or \"shoot\"");
        }
        scanner.close();
    }
}