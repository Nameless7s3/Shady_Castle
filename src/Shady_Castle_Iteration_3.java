import java.io.IOException;
import java.util.Scanner;
import java.util.Date;
import java.io.*;

public class Shady_Castle_Iteration_3
{
    public static void main (String [] a) throws IOException
    {
        greeting();
    }

    //NEW CODE 29/11/22
    public static void saveGameState (Player player) throws IOException
    {
        //Setting up file: creating it and naming it.
        final String SAVE_FILE_NAME = "shady_castle_save.csv";
        PrintWriter saveFile = new PrintWriter(new FileWriter(SAVE_FILE_NAME));

        //Getting player data
        String playerName = getPlayerName(player);
        int currentHealth = getPlayerHealth(player);
        int currentGold = getPlayerGold(player);
        int currentProtectionItems = getPlayerNumProtectionItems(player);

        //Write player data to file
        saveFile.println(playerName + "," + currentHealth + "," + currentGold + "," + currentProtectionItems);
        saveFile.close();

        System.out.println("You have saved your game successfully.");
    }

    public static Player loadGameState () throws IOException
    {
        //Setting up file
        final String LOAD_FILE_NAME = "shady_castle_save.csv";
        BufferedReader inputStream = new BufferedReader(new FileReader(LOAD_FILE_NAME));

        String playerData = inputStream.readLine();
        System.out.println(playerData);
        Player player = new Player();

        if(playerData != null)
        {
            String[] playerSplitData = playerData.split(",");

            String playerName = playerSplitData[0];
            int currentHealth = Integer.parseInt(playerSplitData[1]);
            int currentGold = Integer.parseInt(playerSplitData[2]);
            int currentProtectionItems = Integer.parseInt(playerSplitData[3]);

            player.playerName = playerName;
            player.health = currentHealth;
            player.currentGold = currentGold;
            player.numProtectionItems = currentProtectionItems;
        }
        else
        {
            System.out.println("No save state found.");
        }

        inputStream.close();

        return player;
    }
    //NEW CODE END
    public static String getUserInput (String question)
    {
        //Create a scanner variable to collect user input and store it inside of a string variable.
        System.out.println(question);
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        return userInput;
    }

    //CODE UPDATED 29/11/22
    public static void greeting () throws IOException
    {
        //Greets the user and asks for the user's name, which they input through a separate method.
        //The user is then greeted with their name concatenated in the string.
        //Call the startGame procedure and pass the username as a parameter.
        Player player;
        Boolean loadPreviousSaveChoice = dilemmaPrompt("Would you like to load a previous save? (Y/N)");

        if (loadPreviousSaveChoice)
        {
            player = loadGameState();
        }
        else
        {
            String username = getUserInput("Hello traveller, what is your name?");
            player = createPlayer(username);
            System.out.println("\nWelcome to the castle " + getPlayerName(player));
        }
        gameLoop(player);
        return;
    }

    public static void gameLoop (Player player) throws IOException
    {
        //Set up and loop the main game, generating new rooms, traps by calling methods.
        final int minNumRooms = 5;
        final int maxNumRooms = 20;

        boolean contGame = true;
        boolean exploreRoom;

        String playerName = getPlayerName(player);

        int playerGold = getPlayerGold(player);
        int playerHealth = getPlayerHealth(player);
        int numRoomsInCastle = randomInt(maxNumRooms) + minNumRooms;
        int roomNumber = 0;

        while ((contGame == true && roomNumber < numRoomsInCastle) && getPlayerHealth(player) > 0)
        {
            //Print player details to the screen if the player chooses to
            boolean viewPlayerDetails = dilemmaPrompt("Would you like to view your current status and inventory? (Y/N)");
            if(viewPlayerDetails)
            {
                printPlayerDetails(player);
            }

            System.out.println(playerName + " is searching the castle for a new room...");
            Room room = generateRoom();
            room = addTrapToRoom(room);
            printRoomDetails(room);

            exploreRoom = dilemmaPrompt("Would you like to explore this room? (Y/N)");
            if(exploreRoom)
            {
                player = exploreRoom(player, room);
            }
            if(getPlayerHealth(player) > 0)
            {
                contGame = dilemmaPrompt("Would you like to continue exploring the castle? (Y/N)");
            }
            else
            {
                roomNumber = roomNumber + 1;
            }

        }

        //Two different outcomes depending on how the game is ended.
        if(!contGame)
        {
            leaveCastle(player);
        }
        else
        {
            System.out.println("You have died in the castle to a dangerous trap. GAME OVER");
        }
    }
    //CODE UPDATE END

    //UPDATED 22/11/2022
    public static void printPlayerDetails (Player player)
    {
        //Output all the details of the player to the screen.
        String playerName = getPlayerName(player);

        int currentHealth = getPlayerHealth(player);
        int playerGold = getPlayerGold(player);
        int protectionItems = getPlayerNumProtectionItems(player);

        System.out.println("---------------------------");
        System.out.println("PLAYER NAME: " + playerName);
        System.out.println("NUMBER OF GOLD: " + playerGold);
        System.out.println("REMAINING HEALTH: " + currentHealth);
        System.out.println("NUMBER OF PROTECTION ITEMS: " + protectionItems);
        System.out.println("---------------------------");
    }

    public static Room generateRoom()
    {
        System.out.println("New room found!");

        String newRoomName = generateRoomName();

        int randomNumTraps = 0;
        Boolean isTrapped = true;

        if(isTrapped)
        {
            randomNumTraps = randomInt(4);
        }

        Room newRoom = createRoom(newRoomName, isTrapped, randomNumTraps);
        return newRoom;
    }

    //CODE UPDATED 29/11/2022
    public static void leaveCastle(Player player) throws IOException
    {
        Boolean saveGameChoice = dilemmaPrompt("Would you like to save your current game? (Y/N)");

        if (saveGameChoice)
        {
            saveGameState(player);
        }

        String playerName = getPlayerName(player);

        int numGold = getPlayerGold(player);
        int currentHealth = getPlayerHealth(player);

        String[] castleLeaveEndings = {"sprinted hastily out of the castle.", "jogged clumsily, exiting the castle whilst gripping a wound.",
         "walked slowly, wincing with each step as they exited the castle.", "hobbled sluggishly, gripping a wound and groaning with each step as they exited the castle.",
        "was practically half dead and gravely injured. They limped with great pain, groaning loudly with each step, bleeding profusely as they exited the castle."};

        if(currentHealth > 20)
        {
            System.out.println(playerName + " " + castleLeaveEndings[0]);
        }
        else if(currentHealth > 15)
        {
            System.out.println(playerName + " " + castleLeaveEndings[1]);
        }
        else if (currentHealth > 10)
        {
            System.out.println(playerName + " " + castleLeaveEndings[2]);
        }
        else if(currentHealth > 5)
        {
            System.out.println(playerName + " " + castleLeaveEndings[3]);
        }
        else
        {
            System.out.println(playerName + " " + castleLeaveEndings[4]);
        }

        System.out.println(playerName + " has survived the castle, leaving with " + numGold + " gold");
    }
    //CODE UPDATE END

    public static void printRoomDetails (Room room)
    {
        String roomName = getRoomName(room);
        System.out.println("You have entered the " + roomName);
    }

    public static Player createPlayer (String name)
    {
        Player player = new Player();
        player.playerName = name;
        player.health = 30;
        return player;
    }

    public static int getPlayerHealth (Player player)
    {
        return player.health;
    }

    public static int getPlayerGold (Player player)
    {
        return player.currentGold;
    }

    //NEW CODE 22/11/22
    public static int getPlayerNumProtectionItems(Player player)
    {
        return player.numProtectionItems;
    }

    public static Player changePlayerProtectionItems(Player player, int amount)
    {
        player.numProtectionItems += amount;
        return player;
    }
    //NEW CODE END 22/11/22
    public static Player changePlayerHealth(Player player, int amount)
    {
        player.health += amount;
        return player;
    }

    public static Player changePlayerGold(Player player, int amount)
    {
        player.currentGold += amount;
        return player;
    }

    public static Trap createTrap (String name, int baseDamage)
    {
        Trap newTrap = new Trap();

        newTrap.trapName = name;
        newTrap.trapBaseDamage = baseDamage;

        return newTrap;
    }

    public static String getTrapName (Trap trap)
    {
        return trap.trapName;
    }
    public static int getTrapDamage (Trap trap)
    {
        return trap.trapBaseDamage;
    }

    public static String getRoomName (Room room)
    {
        return room.roomName;
    }

    public static Room createRoom (String name, boolean isTrapped, int numTraps)
    {
        Room newRoom = new Room();
        Trap[] emptyTrapArray = new Trap[numTraps];

        newRoom.roomName = name;
        newRoom.isTrapped = isTrapped;

        newRoom.numTraps = numTraps;

        newRoom.roomTraps = emptyTrapArray;

        return newRoom;
    }

    public static boolean getRoomTrappedStatus (Room room)
    {
        return room.isTrapped;
    }

    public static int getNumTrapsInRoom (Room room)
    {
        return room.numTraps;
    }

    public static Trap[] getTrapsInRoom (Room room)
    {
        return room.roomTraps;
    }

    public static Room changeTrapArray (Room room, Trap[] newTrapArray)
    {
        room.roomTraps = newTrapArray;
        return room;
    }

    public static Room addTrapToRoom (Room room)
    {
        int numTraps = getNumTrapsInRoom(room);
        Trap[] trapsInRoom = getTrapsInRoom(room);

        if(numTraps > 0)
        {
            for (int i = 0; i < numTraps; i++)
            {
                int randomDamage = randomInt(15);
                trapsInRoom[i] = createTrap("Trap " + i, randomDamage);
            }
        }
        room = changeTrapArray(room, trapsInRoom);

        return room;
    }

    public static String getPlayerName (Player player)
    {
        return player.playerName;
    }

    //UPDATED 29/11/2022
    public static Player exploreRoom (Player player, Room currentRoom)
    {
        //Generate a random number, which will represent how much gold has been found in this room, and add it to the player's previous number.
        //Loop this 3 times, so that the player gets a different amount of gold each time.
        //Return this value.
        int numTraps = getNumTrapsInRoom(currentRoom);
        Trap[] roomTraps = getTrapsInRoom(currentRoom);
        boolean roomIsTrapped = getRoomTrappedStatus(currentRoom);

        for (int i=0; i<=3; i++)
        {
            int numGold = randomInt(20);
            int trapActivationChance = randomInt(8);
            int protectionItemChance = randomInt(10);

            if(protectionItemChance == 7)
            {
                System.out.println("You found a trap de-activator!");
                player = changePlayerProtectionItems(player, 1);
            }

            if(trapActivationChance < 4 && roomIsTrapped == true)
            {
                for (int j =0; i < numTraps; i++)
                {
                    int currentTrapDamage = getTrapDamage(roomTraps[i]);

                    //Get the player's current protection items and prevent damage if this value > 0.
                    int numProtectionItems = getPlayerNumProtectionItems(player);

                    if(numProtectionItems > 0)
                    {
                        player = changePlayerProtectionItems(player, -1);
                        System.out.println("You disabled a trap using an anti-trap trinket, avoiding taking damage!");
                    }
                    else //Otherwise, take damage
                    {
                        player = changePlayerHealth(player, -currentTrapDamage);
                        System.out.println("You stepped into a trap and took " + currentTrapDamage + " damage!");
                    }

                    if(checkPlayerStatus(player))
                    {
                        i = 4;
                        j = numTraps;
                    }
                }
                roomIsTrapped = false;
            }
            else
            {
                player = changePlayerGold(player, numGold);
                System.out.println("You have found " + numGold + " gold pieces!");
            }
        }
        return player;
    }
    //END UPDATE

    public static boolean checkPlayerStatus (Player player)
    {
        int currentHealth = getPlayerHealth(player);

        if(currentHealth <= 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public static int randomInt(int highestValue)
    {
        int rand01 = (int)(Math.random() * highestValue);
        return rand01;
    }
    public static boolean dilemmaPrompt(String prompt)
    {
        //Get keyboard input from another method. Store the result in a string variable.
        //Return either true or false depending on the value of the string data that has been stored.
        String choice = getUserInput(prompt);
        if(choice.equals("Y"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static String generateRoomName ()
    {
        //Create 2 arrays, one for room types and one for room adjectives.
        //Create 2 random integers and map them to each array to get one index from each array.
        //Combine these two string values and return the new string.
        String [] roomAdjectives =  {"Decrepid", "Dilapidated", "Rusted", "Quiet", "Ghostly", "Gloomy", "Grand", "White", "Ashen"};
        String [] roomNouns = {"Room", "Chamber", "Temple", "Bedroom", "Quarters", "Hall", "Chapel", "Cathedral", "Smithy"};
        final int NUMBER_IN_ARRAY = 9;
        int rand01 = randomInt(NUMBER_IN_ARRAY);
        int rand02 = randomInt(NUMBER_IN_ARRAY);

        String roomName = roomAdjectives[rand01] + " " + roomNouns[rand02];

        return roomName;
    }
}

class Player
{
    String playerName;

    int currentGold;
    int health;
    int numProtectionItems;

    String[] weaponItems;
}

class Room
{
    String roomName;

    boolean isTrapped;

    int numTraps;

    Trap[] roomTraps;
}

class Trap
{
    String trapName;

    int trapBaseDamage;
}