package trainer;

import java.util.Scanner;

import pokemon.Pokemon;

/*
 * Class used to organize methods to edit and view a specific trainer
 * and their pokemon party
 */
public class TrainerHandler {

	Trainer trainer;	// Trainer to edit
	Pokemon pokemon;	// Selected pokemon to edit
	Scanner kb;
	public TrainerHandler(Trainer trainer, Scanner kb)
	{
		this.trainer = trainer;
		this.pokemon = null;
		this.kb = kb;
	}
	
	// Enter the trainer handler menu
	public void runEditor()
	{
		int choice = -1;
		while(choice != 0)
		{
			displayMainOptions();
			choice = kb.nextInt();
			switch(choice)
			{
			// Exit case
			case 0:
				// TODO: Add code to save the new trainer
				return;
			// View Party Case
			case 1:
				trainer.displayPartyDetailed();
				break;
			// Edit Trainer Information case
			case 2:
				displayEditTrainerOptions();
				choice = kb.nextInt();
				if(choice == 0)
					{
						choice = -1;
						break;
					}
				//TODO: add code to edit trainer information
				System.out.println("Options selected");
				break;
			// Edit Pokemon case
			case 3:
				pokemon = selectPokemon();
				if(pokemon == null) break;
				//TODO: add code to edit trainer information
				editPokemon();
				break;
			}
		}
	}
	// Display options on what to do in the trainer menu
	private void displayMainOptions()
	{
		System.out.println("\nMY TRAINER MENU");
		System.out.println("----------------");
		System.out.println("0. Exit");
		System.out.println("1. View Party");
		System.out.println("2. Edit Trainer Information");
		System.out.println("3. Edit Party Pokemon");
		System.out.print("Choice: ");
	}
	
	// Display options on what to do with the trainer
	private void displayEditTrainerOptions()
	{
		System.out.println("\nWhat would you like to edit?");
		System.out.println("0. Back");
		System.out.println("1. Name");
		System.out.println("2. Title");
		System.out.print("Choice: ");
	}
	
	public void editTrainerInformation()
	{
		
	}
	
	// Display options on what to do with a pokemon
	private void displayEditPokemonOptions()
	{
		System.out.println("\nMY POKEMON MENU");
		System.out.println("----------------");
		System.out.println("Pokemon Selected: " + pokemon.getNickname());
		System.out.println("What would you like to edit?");
		System.out.println("0. Back");
		System.out.println("1. Give Nickname");
		System.out.println("2. Edit Moveset");
		System.out.println("3. Give/Take Item");
		System.out.print("Choice: ");
	}
	
	// Edit specific properties of a pokemon
	private void editPokemon() {
		int input = -1;
		while(input != 0)
		{
			displayEditPokemonOptions();
			input = kb.nextInt();
			switch(input)
			{
			case 0:
				return;
			// Nickname case
			case 1:
				kb.nextLine();
				System.out.print("Enter new nickname: ");
				pokemon.giveNickname(kb.nextLine());
				System.out.println("Saved!");
				break;
			// Edit Moveset case
			case 2:
				//TODO: Add code to edit moveset
				System.out.println("Edit Moveset Selected");
				break;
				
			// Give/Take Item case
			case 3:
				//TODO: Add code to give or take item
				System.out.println("Give/Take Item Selected");
				break;
			}
		}
	}
	
	// Selects a pokemon from the party
	private Pokemon selectPokemon()
	{
		Pokemon selected = null;
		for(int i = 1; i < trainer.getParty().size() + 1; i++)
		{
			System.out.println(i + ". " + trainer.getParty().get(i-1).getNickname());
		}
		System.out.print("Select Pokemon to edit (0 to cancel): ");
		int choice = kb.nextInt();
		if (choice == 0) return null;
		selected = trainer.getParty().get(choice-1);
		return selected;
	}
}
