package main;
import java.util.ArrayList;
import java.util.Scanner;

import battle.*;
import move.move_tree.*;
import pokemon.*;
import trainer.Trainer;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Pokemon> Pokedex = initialize.initializePokedex();
		ArrayList<Move> Movedex = initialize.initializeMovedex();

		Trainer Ash = initialize.createAsh(Pokedex, Movedex);
		Trainer Ritchie = initialize.createRitchie(Pokedex, Movedex);
		//Ash.displayParty();
		//Ritchie.displayParty();
		
		Scanner kb = new Scanner(System.in);
		new Single_Battle(Ash, Ritchie, kb, false).Battle();;
		
		Ash.displayParty();
		Ritchie.displayParty();
		System.exit(0);
	}

}
