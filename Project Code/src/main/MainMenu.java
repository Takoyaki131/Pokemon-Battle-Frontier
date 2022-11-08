package main;

import java.util.ArrayList;

import move.move_tree.Move;

public class MainMenu {

	public static void displayMainMenuOptions()
	{
		System.out.println("MAIN MENU");
		System.out.println("---------");
		System.out.println("1. Battle");
		System.out.println("2. View/Edit Trainer");
		System.out.println("3. Create A Trainer");
		System.out.println("4. View Pokedex");
		System.out.println("5. View Movedex");
		System.out.println("0. Exit");
		System.out.print("Input: ");
	}
	
	public static void displayBattleTypes()
	{
		System.out.println("\nSelect Battle Type");
		System.out.println("1. Single Battle");
		System.out.println("2. Double Battle(Not available)");
		System.out.println("0. Back");
		System.out.print("Choice: ");
	}
	
	public static void displayMovedex(ArrayList<Move> movedex)
	{
		System.out.println("Movedex");
		System.out.println("-------");
		for(Move move : movedex)
		{
			System.out.println(move.getTm_no() + ". " + move.getName());
		}
	}
}
