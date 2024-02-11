import java.util.*;


public class HuntWumpus {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String choice;
        int choiceNum;
        boolean gameOver = false;
        int healCount = 0;

        // GENERAL SETUP

        // Initialize our cave system, this will assign each int room_number (key) an object representing its further details (value) such as adjacent rooms, and what is in the room currently (physically or nearby)
        Map<Integer, Room> rooms = Room.roomAssignment();

        // Spawn pit - create pit will choose a random |physically empty| room (nearby presence can still occupy it), 
        // and then update that room's pitOccupation status to true, so that if a player walks in, the program knows they should lose
        Pit.createPit(rooms); 
        rooms.get(Pit.getPitPos()).setPitOccupation();

        // Spawn Wumpus - etc
        Wumpus.spawnWumpus(rooms);
        rooms.get(Wumpus.getWumpusPos()).setWumpusOccupation();

        // Spawn bats - spawns 3 bats in random empty rooms
        Bat bats[] = new Bat[3];
        for (int i = 0; i < 3; i++) {
            bats[i] = new Bat();
            bats[i].spawnBat(rooms);
            rooms.get(bats[i].getBatPos()).setBatOccupation(bats[i]);
        }
        
        // Set presence for pit, Wumpus and bats, with presence being the alerts given when your room is adjacent to a room containing one of these entities.
        Pit.pitPresence(rooms);                             // Rooms array is passed to update the randomly selected room to the board mapping
        Wumpus.wumpusPresence(rooms);                       // and so forth 
        for (Bat bat : bats) bat.batPresence(rooms);

        Room.setEffects(rooms);     // Set hearing and smell loss effects through room class
        Room.placeArrow(rooms);     // Spawn arrow through room class

        Player.spawnPlayer(rooms);  // Will select a physically empty room

    
        // Prints out the initial presence detection of the player 
        Room.roomStatus(rooms, bats, rooms.get(Player.getPlayerPos()), Player.getPlayerPos());

        // Loop that continues to let player make choices until he wins or loses
        while (gameOver == false) {

            System.out.println("You are in room " + Player.getPlayerPos() + ". You have " + Player.getArrow() + " arrow(s). Do you want to |move| or |shoot| into the adjacent rooms " + rooms.get(Player.getPlayerPos()).getRooms() + "?\n");
            choice = scanner.nextLine().strip();

            // MOVEMENT COMPONENTS OF THE GAME
            if (choice.equals("move")) {
                System.out.println("Which room would you like to move into?\n");
                choiceNum = verifySelection(rooms, scanner);    // Input validation

                // This method will return false if the game does not end, true if it should. It is able to update the position of entities (player, player&bat, wumpus), game states, and so forth
                gameOver = Room.roomStatus(rooms, bats, rooms.get(choiceNum), choiceNum);  

                lostSense(Player.getHearingLossMoveTimer(), Player.getSmellLossMoveTimer());

            // START OF SHOOT COMPONENTS OF THE GAME
            } else if (choice.equals("shoot")) {
                System.out.println("Which room would you like to shoot an arrow into?\n");;
                choiceNum = verifySelection(rooms, scanner);
                
                //ShootSomething method returns the results of shooting an arrow in a room, i.e. that it is either: empty, has a pit, has a bat, or has the wumpus
                gameOver = Player.shootSomething(rooms, bats, rooms.get(choiceNum)); 

                if (Player.getArrow() == 0) {
                    gameOver = true;
                    System.out.println("All arrows have been used up! Game over.");
                }

            } else System.out.println("Invalid input - Please enter either \"move\" or \"shoot\".");


            //Additional Turn-by-turn proccess
            if (Wumpus.getWumpusHealth() == 1 && !gameOver) {
                if (healCount == 5) {
                    System.out.println("Through unworldly means the Wumpus has regained his health.");
                    Wumpus.resetWumpusHealth();
                    healCount = 0;
                }

                else {
                    healCount++;
                    System.out.println(6 - healCount + " turn(s) remain until the Wumpus heals his wounds.");
                }   

            }

        }
        scanner.close();
    }

    public static int verifySelection(Map<Integer,Room> rooms, Scanner scanner) {
        boolean choiceVerified = true;
        String choice;
        int choiceNum = -1;

        do {
            choiceVerified = true;
            choice = scanner.nextLine().strip();

            try {choiceNum = Integer.parseInt(choice);} 
            
            catch (NumberFormatException e) {
                System.out.println("Invalid input - Please key in the rooms with appropriate numerical values as displayed!");
                System.out.println("You are in room " + Player.getPlayerPos() + ". Which rooms out of the adjacent rooms: " + rooms.get(Player.getPlayerPos()).getRooms() + " do you want to move/shoot into?"); 
                System.out.println(""); 
                choiceVerified = false;
            }

            if (!Player.verifyMovement(rooms.get(Player.getPlayerPos()), choiceNum) && choiceVerified != false) {
                System.out.println("Make sure your entry is valid by choosing one of the three rooms presented!"); 
                System.out.println("You are in room " + Player.getPlayerPos() + ". Which rooms out of the adjacent rooms: " + rooms.get(Player.getPlayerPos()).getRooms() + " do you want to move/shoot into?"); 
                System.out.println(""); 
                choiceVerified = false;
            }
        } while (!choiceVerified);

        return choiceNum;
    }
    
    public static void lostSense(int hearingLossMoveTimer, int smellLossMoveTimer) {
        if (Player.getHearingLoss()) {
            Player.decreaseHearingLossMoveTimer();
            if (Player.getHearingLossMoveTimer() == 0) {
                Player.setHearingLoss(false);
                System.out.println("You can hear again!");
                Player.resetHearingLossMoveTimer();
            }
        }
        
        if (Player.getSmellLoss()) {
            Player.decreaseSmellLossMoveTimer(); 
            if (Player.getSmellLossMoveTimer() == 0) {
                Player.setSmellLoss(false);
                System.out.println("You can smell again!");
                Player.resetSmellLossMoveTimer();
            }
        }
    }
}
