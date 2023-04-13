// NAME Luke Liew Mouawad
// DATE 17/10/22
// STUDENT ID: 220267214
// VERSION 1
// BRIEF OVERVIEW OF PURPOSE Explore a booby trapped castle game. Write a game that allows you to explore the castle, collecting objects and disabling traps.

import java.util.Scanner; // Needed to make Scanner available
import java.util.Random;

class TheShadyCastle // change the name to something appropriate
{
    public static void main (String [] a)
    {
        greeting();
    }

    public static String getUserInput (String question)
    {
        //Create a scanner variable to collect user input and store it inside of a string variable.
        System.out.println(question);
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        return userInput;
    }

    public static void greeting ()
    {
        //Greets the user and asks for the user's name, which they input through a separate method.
        //The user is then greeted with their name concatenated in the string.
        //Call the startGame procedure and pass the username as a parameter.
        String username = getUserInput("Hello traveller, what is your name?");
        System.out.println("\nWelcome to the castle " + username);
        generateRoom();
        return;
    }

    public static void generateRoom()
    {
        //Open up a loop, continue the loop until the player decides to stop exploring (stop playing the game).
        //Random room name is created through a separate method, and the player can choose to explore the room or not.
        //Player can find a random number of gold pieces in each room.
        boolean contExploring = true;
        int gold = 0;

        while (contExploring == true)
        {
            String newRoomName = generateRoomName();
            System.out.println("You have entered the " + newRoomName);
            contExploring = leaveOrContinue();
            if(contExploring == true)
            {
                System.out.println("You have decided to explore the " + newRoomName);
                gold = generateRandomRoomObjects(gold);
            }
            else
            {
                System.out.println("You have decided to leave the castle with " + gold + " gold pieces.");
            }
        }
    }

    public static int generateRandomRoomObjects (int currentGold)
    {
        //Generate a random number, which will represent how much gold has been found in this room, and add it to the player's previous number.
        //Loop this 3 times, so that the player gets a different amount of gold each time.
        //Return this value.
        for (int i=0; i<=3; i++)
        {
            int rand01 = (int)(Math.random() * 4);
            int rand02 = (int)(Math.random() * 10);
            if(rand02 == 1)
            {
                System.out.println("You stepped into a trap and died!");
                i = 4;
            }
            else
            {
                currentGold += rand01;
                System.out.println("You have found " + rand01 + " gold pieces!");
            }
        }
        return currentGold;
    }

    public static boolean leaveOrContinue()
    {
        //Get keyboard input from another method. Store the result in a string variable.
        //Return either true or false depending on the value of the string data that has been stored.
        String choice = getUserInput("Would you like to continue exploring or leave the castle? (Y/N)");
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
        String [] roomNouns = {"Room", "Chamber", "Temple", "Bedroom", "Quarters", "Hall", "Chapel", "Cathedral", "Bathroom"};
        final int NUMBER_IN_ARRAY = 9;
        int rand01 = (int)(Math.random() * NUMBER_IN_ARRAY);
        int rand02 = (int)(Math.random() * NUMBER_IN_ARRAY);

        String roomName = roomAdjectives[rand01] + " " + roomNouns[rand02];

        return roomName;
    }

}