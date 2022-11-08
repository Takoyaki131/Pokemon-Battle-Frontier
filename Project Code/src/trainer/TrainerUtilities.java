package trainer;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import move.move_tree.Move;
import pokemon.Pokemon;

public class TrainerUtilities {

	
	/* Updates trainer_names ArrayList based on names.txt
	 * Input: 	none
	 * Returns: ArrayList<String> - updated ArrayList of trainer names
	 */
	public static ArrayList<String> updateTrainerNames()
	{
		ArrayList<String> trainer_names = new ArrayList<>();
		BufferedReader reader;
		try
		{
			reader = new BufferedReader(new FileReader("Trainers\\names.txt"));
			String line = reader.readLine();
			while (line != null)
			{
				trainer_names.add(line);
				line = reader.readLine();
			}
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return trainer_names;
	}
	
	/* Creates a new trainer object
	 * Input:   kb			- input scanner stream
	 * 			poke_list	- list of pokemon to add to the party
	 * Returns: void
	 */
	public static void createTrainer(Scanner kb, ArrayList<Pokemon> poke_list, ArrayList<Move> move_list)
	{
		// Get Trainer name
		kb.nextLine();
		String name;
		System.out.print("Enter your trainers name: ");
		name = kb.nextLine();
		
		Trainer temp = new Trainer(name);
		
		// Display pokemon to add
		int choice = -1;
		boolean done = false;
		while (done == false)
		{
			// Add until party is full
			if(temp.getParty().size() >= 6) done = true;
			int num = 0;
			for (Pokemon poke : poke_list)
			{
				System.out.println((num+1) + ". " + poke.getName());
				num++;
			}
			
			System.out.print("Which pokemon would you like to add? (0 to exit) ");
			choice = kb.nextInt();
			// Add the pokemon to the party
			if (choice != 0)
			{	
				temp.addToParty(poke_list.get(choice-1).copy());
				System.out.println("Pokemon added!");
			}
			else
				done = true;
		}
		
		// Confirm Trainer and party
		temp.displayPartyBasic();
		System.out.print("Would you like to save your trainer? (1 = yes) ");
		choice = kb.nextInt();
		
		// Save the Trainer as a serialized object
		if (choice == 1)
		{
			try {
				//TODO: Give moves on save
				
				// Save trainer as a serialized object
				FileOutputStream fileOut = new FileOutputStream("trainers\\" + name + ".ser");
				ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
				objOut.writeObject(temp);
				objOut.close();
				fileOut.close();
				kb.nextLine();
			}
			catch (IOException i)
			{
				i.printStackTrace();
			}
			
			
			// Write Trainer name in names.txt
			try
			{
				FileWriter fw = new FileWriter("trainers\\names.txt", true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    bw.write(name);
			    bw.newLine();
			    bw.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			System.out.println("Trainer " + name + " saved! Returning to main menu");
		}
		else
		{
			// Dont save and return to menu
			System.out.println("Trainer not saved. returning to main menu");
		}
		return;
	} // End of createTrainer()
	
	/* Overwrites trainer data after change or fight
	 * Input:   trainer - trainer to overwrite data with
	 * Returns: void
	 */
	public static void saveTrainer(Trainer trainer)
	{
		// Delete old trainer serialized file
		String name = trainer.getName();
		File targetObj = new File(("Trainers\\" + name +".ser"));
		if (targetObj.delete())
		{
			System.out.println("CONSOLE: " + name + ".ser file deleted");
		} 
		else
		{
			System.out.println("Failed to delete trainer file");
		}
		
		// Create new trainer data
		try {
			FileOutputStream fileOut = new FileOutputStream("trainers\\" + name + ".ser");
			ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
			objOut.writeObject(trainer);
			objOut.close();
			fileOut.close();
			System.out.println("Overwrite save completed");
		}
		catch (IOException i)
		{
			i.printStackTrace();
		}
	} // End of saveTrainer
	
	/* Assists in selecting a trainer to edit or select for battle
	 * Input:   trainer_names 	- list of trainers in the game
	 * 			kb				- input scanner stream
	 * Returns: Trainer			- Selected trainer object
	 */
	public static Trainer getTrainer(ArrayList<String> trainer_names, Scanner kb)
	{
		// If no trainers, exit
		if (trainer_names == null)
		{
			System.out.println("No trainer data");
			return null;
		}
		
		Trainer temp = null;
		// Print all trainers in the game
		System.out.println("All trainers in the game");
		int trainer_count = 1;
		for (String name : trainer_names)
		{
			System.out.println( trainer_count  + " " + name);
			trainer_count++;
		}
		
		// Prompt user to select trainer
		System.out.print("Select trainer (Enter 0 to cancel): ");
		trainer_count = kb.nextInt();
		kb.nextLine();
		
		// Validate choice
		if (trainer_count <= 0 || trainer_count > trainer_names.size())
		{
			return null;
		}
		String trainer_name_to_load = trainer_names.get(trainer_count - 1);
		
		// Load up trainer using the seralized file
		try {
			FileInputStream fileIn = new FileInputStream("Trainers\\" + trainer_name_to_load + ".ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			temp = (Trainer) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
	         i.printStackTrace();
	         kb.close();
	    } catch (ClassNotFoundException c) {
	         System.out.println("Trainer class not found");
	         c.printStackTrace();
	         kb.close();
	    }
		return temp;
	} // End of getTrainer
}
