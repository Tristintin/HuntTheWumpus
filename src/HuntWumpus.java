import java.util.*;


public class HuntWumpus {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String choice;
        boolean gameOver = false;
        boolean movementVerified, arrowVerified;

        System.out.println();

        // Initialize our cave system
        Map<Integer, Room> rooms = Room.roomAssignment();

        // Spawn pit
        rooms = Pit.createPit(rooms); 
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
        
        // Set hearing and smell loss effects using static method in Room class
        rooms = Room.setEffects(rooms);

        // Spawn arrow
        rooms = Room.placeArrow(rooms);

        // Spawn player
        rooms = Player.spawnPlayer(rooms);

        // Set presence for pit, Wumpus and bats
        rooms = Pit.pitPresence(rooms, Pit.getPitPos());
        rooms = Wumpus.wumpusPresence(rooms);
        for (Bat bat : bats) rooms = bat.batPresence(rooms);

        
        // Debugging lines
        System.out.println();
        System.out.println("Player position: " + Player.getPos());
        System.out.println("Wumpus position: " + Wumpus.getWumpusPos());
        System.out.println("Pit position: " + Pit.getPitPos());
        for (int i = 1; i <= bats.length; i++) {System.out.println("Bat " + i + " position: " + bats[i-1].getBatPos());}
        for (Map.Entry<Integer, Room> entry : rooms.entrySet()) {
            if (entry.getValue().getHearingLoss()) System.out.println("Hearing loss room: " + entry.getKey());
            if (entry.getValue().getSmellLoss()) System.out.println("Smell loss room: " + entry.getKey());
            if (entry.getValue().getArrow()) System.out.println("Arrow room: " + entry.getKey());
        }
        System.out.println();


        // Prints out the initial presence detection of the player
        Room.movementStatus(rooms, bats, rooms.get(Player.getPos()), Player.getPos());

        // Loop that continues to let player make choices until he wins or loses
        int hearingLossMoveCounter = 0; 
        int smellLossMoveCounter = 0; 
        while (gameOver == false) {

            System.out.println("You are in room " + Player.getPos() + ". Do you want to |move| or |shoot| into the adjacent rooms " + rooms.get(Player.getPos()).getRooms() + "?");
            System.out.println(""); 
            choice = scanner.nextLine().strip();

            if (choice.equals("move")) {
                System.out.println("Which room would you like to move into?");
                System.out.println("");

                // Exception handling for NumberFormatException
                int choiceNum = Player.getPos();
                do {
                    movementVerified = true;
                    choice = scanner.nextLine().strip();

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

                if (Player.getHearingLoss()) {
                    hearingLossMoveCounter++;
                    if (hearingLossMoveCounter == 3) {
                        Player.setHearingLoss(false);
                        System.out.println("You can hear again!");
                        hearingLossMoveCounter = 0;
                    }
                }
                
                if (Player.getSmellLoss()) {
                    smellLossMoveCounter++;
                    if (smellLossMoveCounter == 3) {
                        Player.setSmellLoss(false);
                        System.out.println("You can smell again!");
                        smellLossMoveCounter = 0;
                    }
                }

            } else if (choice.equals("shoot")) {
                System.out.println("Which room would you like to shoot an arrow into?");
                System.out.println("");

                // Exception handling for NumberFormatException
                int choiceNum = Player.getPos();
                do {
                    arrowVerified = true;
                    choice = scanner.nextLine().strip();

                    try {
                        choiceNum = Integer.parseInt(choice); // Converts string to int, so we can check if its invalid as a number or not
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input - please key in the rooms with appropriate numerical values as displayed!");
                        System.out.println("You are in room " + Player.getPos() + ". Which rooms out of the adjacent rooms: " + rooms.get(Player.getPos()).getRooms() + " do you want to shoot into?");
                        System.out.println(); 
                        arrowVerified = false;
                    }

                    if (!Player.verifyMovement(rooms.get(Player.getPos()), choiceNum) && arrowVerified != false) {
                        System.out.println("Invalid input - please choose one of the three adjacent roooms presented!"); 
                        System.out.println("You are in room " + Player.getPos() + ". Which rooms out of the adjacent rooms: " + rooms.get(Player.getPos()).getRooms() + " do you want to shoot into?"); 
                        System.out.println();
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


        // Debugging lines
        System.out.println();
        System.out.println("Player position: " + Player.getPos());
        System.out.println("Wumpus position: " + Wumpus.getWumpusPos());
        System.out.println("Pit position: " + Pit.getPitPos());
        for (int i = 1; i <= bats.length; i++) {System.out.println("Bat " + i + " position: " + bats[i-1].getBatPos());}
        for (Map.Entry<Integer, Room> entry : rooms.entrySet()) {
            if (entry.getValue().getHearingLoss()) System.out.println("Hearing loss room: " + entry.getKey());
            if (entry.getValue().getSmellLoss()) System.out.println("Smell loss room: " + entry.getKey());
            if (entry.getValue().getArrow()) System.out.println("Arrow room: " + entry.getKey());
        }
        System.out.println();


        }
        scanner.close();
    }
}
