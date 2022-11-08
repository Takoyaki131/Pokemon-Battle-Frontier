package battle;

import java.util.Collections;
import java.util.Scanner;


import move.extras.*;
import move.move_tree.Move;
import pokemon.Pokemon;
import trainer.Trainer;

public class Single_Battle {
	private Trainer left_trainer;
	private Trainer right_trainer;
	
	private Pokemon left_current_pokemon;
	private Pokemon right_current_pokemon;
	
	private Move left_pokemon_move;
	private Move right_pokemon_move;
	
	private String left_previous_pokemon;
	private String right_previous_pokemon;
	
	boolean left_move_made = false;
	boolean right_move_made = false;
	
	private boolean left_made_swap = false;
	private boolean right_made_swap = false;
	
	private boolean left_trainer_wins = false;
	private boolean right_trainer_wins = false;
	
	private Scanner kb;
	private int input;
	private boolean show_console;
	
	// Constructor
	public Single_Battle(Trainer left_trainer, Trainer right_trainer, Scanner scanner, boolean show_console)
	{
		super();
		this.left_trainer = left_trainer;
		this.right_trainer = right_trainer;
		
		this.left_current_pokemon = left_trainer.getParty().get(0);
		this.right_current_pokemon = right_trainer.getParty().get(0);
		
		this.show_console = show_console;
		this.kb = scanner;
	}
	
	public void Battle()
	{
		System.out.println(left_trainer.getName() + " sent out " + left_current_pokemon.getNickname()+ "!");
		System.out.println(right_trainer.getName() + " sent out " + right_current_pokemon.getNickname() + "!");
		
		int turn_count = 0;	// counting for console purposes
		
		// Keep going until a winner is determined
		while (left_trainer_wins == false & right_trainer_wins == false)
		{
			turn_count++;
			if (show_console == true) System.out.println("Turn: " + turn_count);
			turn();
		}
		
		if (left_trainer_wins == true) System.out.println(right_trainer.getName() + " is out of usable Pokemon! " + left_trainer.getName() + " wins!");
		else System.out.println(left_trainer.getName() + " is out of usable Pokemon! " + right_trainer.getName() + " wins!");
	
		System.out.println("End of battle");
		return;
	}
	
	public void turn()
	{
		// Left Trainer Mid Turn
		while(left_move_made == false)
		{
			displayHP();
			System.out.println("\n1. Fight \n2. Pokemon \n3. Bag");
			System.out.print("What will " + left_current_pokemon.getNickname() + " do? ");
			input = kb.nextInt();
			switch(input)
			{
				// Fight option
			case 1:
				left_current_pokemon.displayCurrentMoves();
				System.out.println("0. Back");
				System.out.println("Your choice: ");
				input = kb.nextInt();
				
				// Check if input is a valid choice
				if(input == 0) break;
				if (input < 0 || input > left_current_pokemon.getLearnable_moves().size())
				{
					break;
				}
				else
				{
					left_pokemon_move = left_current_pokemon.getMoveset()[input-1];
					if(show_console == true) System.out.println("Move selected: " + left_pokemon_move.getName());
					left_move_made = true;
				}
				break;
				// Pokemon option
			case 2:
				// Prompt input and/or swap
				left_trainer.displayPartyBasic();
				System.out.print("(0 to back) Who would you like to switch out for? ");
				input = kb.nextInt();
				if (input <= 1 || input > left_trainer.getParty().size()) break;
				
				// Perform swap
				left_previous_pokemon = left_current_pokemon.getName();
				Collections.swap(left_trainer.getParty(), 0, input-1);
				left_current_pokemon = left_trainer.getParty().get(0);
				left_made_swap = true;
				left_move_made = true;
				break;
			case 3:
				System.out.println("Bag selected");
				break;
			}
		}

		// Determine if the pokemon fainted pre turn
		if(Battle_Mechanics.isFainted(right_current_pokemon) == true);
		{
			// Check if trainer can keep battling
		}
		
		// Right Trainer Mid Turn
		while(right_move_made == false)
		{
			displayHP();
			System.out.println("\n1. Fight \n2. Pokemon \n3. Bag");
			System.out.print("What will " + right_current_pokemon.getNickname() + " do? ");
			input = kb.nextInt();
			switch(input)
			{
				// Fight option
			case 1:
				right_current_pokemon.displayCurrentMoves();
				System.out.println("0. Back");
				System.out.println("Your choice: ");
				input = kb.nextInt();
				
				// Check if input is a valid choice
				if(input == 0) break;
				if (input < 0 || input > right_current_pokemon.getLearnable_moves().size())
				{
					break;
				}
				else
				{
					right_pokemon_move = right_current_pokemon.getMoveset()[input-1];
					if(show_console == true) System.out.println("Move selected: " + right_pokemon_move.getName());
					right_move_made = true;
				}
				break;
				// Pokemon option
			case 2:
				// Prompt input and/or swap
				//TODO: Add check for selected same pokemon
				right_trainer.displayPartyBasic();
				System.out.print("(0 to back) Who would you like to switch out for? ");
				input = kb.nextInt();
				if (input <= 1 || input > right_trainer.getParty().size()) break;
				
				// Perform swap
				right_previous_pokemon = right_current_pokemon.getName();
				Collections.swap(right_trainer.getParty(), 0, input-1);
				right_current_pokemon = right_trainer.getParty().get(0);
				right_made_swap = true;
				right_move_made = true;
				break;
			case 3:
				System.out.println("Bag selected");
				break;
			}
		}
		
		// Flag check with console
		if (show_console == true)
		{
			System.out.println("LEFT TRAINER FLAGS");
			System.out.println("left_made_swap: " + left_made_swap);
			if (left_pokemon_move == null) System.out.println("left_pokemon_move: null");
			else System.out.println("left_pokemon_move: Move ->" + left_pokemon_move.getName());
			
			System.out.println("\nRIGHT TRAINER FLAGS");
			System.out.println("right_made_swap: " + right_made_swap);
			if (right_pokemon_move == null) System.out.println("right_pokemon_move: null");
			else System.out.println("right_pokemon_move: Move ->" + right_pokemon_move.getName());
		}
		
		// Calculate the Turn events
		// Both Swap
		
		if (left_made_swap == true & right_made_swap == true)
		{
			//System.out.println("Both swap");
			System.out.println(left_previous_pokemon + " get back! Go " + left_current_pokemon.getName() + "!");
			System.out.println(right_previous_pokemon + " get back! Go " + right_current_pokemon.getName() + "!");
			
			// Reset flags
			left_made_swap = right_made_swap = false;
		}
		
		// Left Swap / Right Attacks 
		if (left_made_swap == true & right_pokemon_move != null)
		{
			//System.out.println("left swap / right attack");
			System.out.println(left_previous_pokemon + " get back! Go " + left_current_pokemon.getName() + "!");
			usePokemonMove(right_current_pokemon, left_current_pokemon, right_pokemon_move);
			
			// Reset flags
			left_made_swap = false;
			right_pokemon_move = null;

		}
		// Right Swaps / Left Attacks
		if (left_pokemon_move != null & right_made_swap == true)
		{
			//System.out.println("Left attack / right swap");
			System.out.println(right_previous_pokemon + " get back! Go " + right_current_pokemon.getName() + "!");
			usePokemonMove(left_current_pokemon, right_current_pokemon, left_pokemon_move);
			
			// Reset flags
			right_made_swap = false;
			left_pokemon_move = null;
		
		}
		// Both Attack
		if(left_pokemon_move != null & right_pokemon_move != null)
		{
			//System.out.println("Both attack");
			// Determine who goes first
			if (left_current_pokemon.getBattle_speed() > right_current_pokemon.getBattle_speed())
			{
				usePokemonMove(left_current_pokemon, right_current_pokemon, left_pokemon_move);
				// Check is move results in fainted pokemon
				if(Battle_Mechanics.isFainted(right_current_pokemon) == false)
				{
					usePokemonMove(right_current_pokemon, left_current_pokemon, right_pokemon_move);
				}
			}
			else
			{
				usePokemonMove(right_current_pokemon, left_current_pokemon, right_pokemon_move);
				// Check if move results in fainted pokemon
				if(Battle_Mechanics.isFainted(left_current_pokemon) == false)
				{
					usePokemonMove(left_current_pokemon, right_current_pokemon, left_pokemon_move);
				}
			}
			
			// Reset flags
			left_pokemon_move = right_pokemon_move = null;
		}
		
		
		// Check result
		postTurn();
		
		left_move_made = false;
		right_move_made = false;
	}
	
	/* Checks post move conditions such as fainting and potential swaping of pokemon
	 * Input:	none
	 * Reuturn	void
	 */
	private void postTurn()
	{
		Battle_Mechanics.postMoveStatusEffects(left_current_pokemon);
		Battle_Mechanics.postMoveStatusEffects(right_current_pokemon);
		
		int faint_count = 0;
		if (Battle_Mechanics.isFainted(left_current_pokemon) == true)
		{
			System.out.println(left_current_pokemon.getName() + " fainted");
			left_current_pokemon.setIs_fainted(true);
			
			// Check if left trainer has available pokemon
			for(Pokemon pokemon : left_trainer.getParty())
			{
				if(pokemon.isIs_fainted() == true) faint_count++;
			}
			if (faint_count == left_trainer.getParty().size()) right_trainer_wins = true;
			else
			{
				// Prompt for swap
				left_trainer.displayPartyBasic();
				System.out.print("Select your next pokemon: ");
				input = kb.nextInt();
				
				// Input error check
				while(input <= 1 || input > left_trainer.getParty().size())
				{
					System.out.print("Select a pokemon: ");
					input = kb.nextInt();
				}
					
				// Make the swap
				Collections.swap(left_trainer.getParty(), 0, input-1);
				left_current_pokemon = left_trainer.getParty().get(0);
				System.out.println(left_trainer.getName() + " sent out " + left_current_pokemon.getName());
			
			}
		}
		if (Battle_Mechanics.isFainted(right_current_pokemon) == true)
		{
			System.out.println(right_current_pokemon.getName() + " fainted");
			right_current_pokemon.setIs_fainted(true);
			
			// Check if left trainer has available pokemon
			for(Pokemon pokemon : right_trainer.getParty())
			{
				if(pokemon.isIs_fainted() == true) faint_count++;
			}
			if (faint_count == right_trainer.getParty().size()) left_trainer_wins = true;
			else
			{
				// Prompt for swap
				right_trainer.displayPartyBasic();
				System.out.print("Select your next pokemon: ");
				input = kb.nextInt();
				
				// Input error check
				while(input <= 1 || input > right_trainer.getParty().size())
				{
					System.out.print("Select a pokemon: ");
					input = kb.nextInt();
				}
					
				// Make the swap
				Collections.swap(right_trainer.getParty(), 0, input-1);
				right_current_pokemon = right_trainer.getParty().get(0);
				System.out.println(right_trainer.getName() + " sent out " + right_current_pokemon.getName());
			}
		}
	}
	
	private void usePokemonMove(Pokemon user, Pokemon target, Move move)
	{
		// Set up the move based on the target pokemon
		if (move instanceof TargetSelf) Battle_Mechanics.useMove(user, user, move);
		else Battle_Mechanics.useMove(user, target, move);
	}
	
	/* Displays the hp and status condition of both pokemon
	  * Input: 	none
	 * Returns: void
	 */
	private void displayHP()
	{
		System.out.println();
		if(left_current_pokemon.getBattle_status() != null)
		{
			System.out.println("HP: " + left_current_pokemon.getCurrent_hp() + " / " + left_current_pokemon.getCurrent_max_hp() + "\t" + left_current_pokemon.getNickname() + "\t" + left_current_pokemon.getBattle_status());
		}
		else
		{
			System.out.println("HP: " + left_current_pokemon.getCurrent_hp() + " / " + left_current_pokemon.getCurrent_max_hp() + "\t" + left_current_pokemon.getNickname());
		}
		if(right_current_pokemon.getBattle_status() != null)
		{
			System.out.println("HP: " + right_current_pokemon.getCurrent_hp() + " / " + right_current_pokemon.getCurrent_max_hp() + "\t" + right_current_pokemon.getNickname() + "\t" + right_current_pokemon.getBattle_status());
		}
		else
		{
			System.out.println("HP: " + right_current_pokemon.getCurrent_hp() + " / " + right_current_pokemon.getCurrent_max_hp() + "\t" + right_current_pokemon.getNickname());
		}
	}
}
