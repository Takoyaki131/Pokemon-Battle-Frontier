package main;
import java.util.ArrayList;
import java.util.Scanner;

import battle.*;
import main.utility.initialize;
import move.move_tree.*;
import pokemon.*;
import trainer.*;

public class Main {

	public static void main(String[] args) {
		ArrayList<Pokemon> Pokedex = initialize.initializePokedex(); 	// list holding all pokemon
		ArrayList<Move> Movedex = initialize.initializeMovedex();		// List holding all moves
		ArrayList<String> trainer_names = new ArrayList<>();			// List holding all trainer_names, used for file management
		Scanner kb= new Scanner(System.in);								// Scanner for keyboard input
		Trainer left_trainer, right_trainer, edit_trainer;				// Trainer objects
		
		Trainer Ash = initialize.createAsh(Pokedex, Movedex);
		Trainer Ritchie = initialize.createRitchie(Pokedex, Movedex);
		
		new TrainerHandler(Ash, kb).runEditor();
	
		//new Single_Battle(Ash, Ritchie, kb, false).Battle();
		/*
		// Main Menu
		int input = 0; // Variable tracking user input
		boolean end_prog = false; // Flag tracking for program
		
		while(end_prog == false)
		{
			//TODO: Add code for updating save data
			trainer_names = TrainerUtilities.updateTrainerNames();
			MainMenu.displayMainMenuOptions();
			input = kb.nextInt();
			switch(input)
			{
			// Exit
			case 0:
				end_prog = true;
				break;
				
			// Battle
			case 1:
				System.out.println("Select trainer 1.");
				left_trainer = TrainerUtilities.getTrainer(trainer_names, kb);
				System.out.println("Select trainer 2.");
				right_trainer = TrainerUtilities.getTrainer(trainer_names, kb);
				
				// TODO: Add code for selecting battle level
				
				// Setup and start battle
				boolean console = false;
				System.out.print("Enter 1 to enable console information: ");
				input = kb.nextInt();
				if (input == 1) console = true;
				new Single_Battle(left_trainer, right_trainer, kb, console).Battle();
				break;
				
			// View/Edit Trainer
			case 2:
				if (trainer_names.isEmpty())
				{
					System.out.println("No Trainer Data");
					break;
				}
				edit_trainer = TrainerUtilities.getTrainer(trainer_names, kb);
				// Cancel case, edit_trainer == null
				if (edit_trainer == null)
				{
					break;
				}
				// Setup and Start trainerHandler
				edit_trainer.displayPartyDetailed();
				break;
				
			// Create Trainer
			case 3:
				TrainerUtilities.createTrainer(kb, Pokedex, Movedex);
				break;
			}
		}
		*/
		
		kb.close();
		System.out.println("End of program");
		System.exit(0);
	}

}
