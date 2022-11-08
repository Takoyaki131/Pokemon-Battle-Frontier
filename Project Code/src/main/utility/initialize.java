package main.utility;

import java.util.ArrayList;

import move.move_tree.*;
import move.movedex.*;
import pokemon.Pokemon;
import pokemon.pokedex.*;
import trainer.Trainer;

public class initialize {
	
	public static ArrayList<Pokemon> initializePokedex()
	{
		ArrayList<Pokemon> pokedex = new ArrayList<>();
		pokedex.add(new P_001_Bulbasaur());
		
		pokedex.add(new P_004_Charmander());
		
		pokedex.add(new P_007_Squirtle());
		
		pokedex.add(new P_025_Pikachu());
		return pokedex;
	}
	
	public static Trainer createAsh(ArrayList<Pokemon> pokedex, ArrayList<Move> movedex)
	{
		Trainer ash = new Trainer("Ash");
	
		ash.addToParty(pokedex.get(3).copy());
		ash.addToParty(pokedex.get(2).copy());
		for(Pokemon pokemon : ash.getParty())
		{
			pokemon.setLevel(20);
			pokemon.assignRandomMoves(movedex);
		}
		return ash;
	}
	
	public static Trainer createRitchie(ArrayList<Pokemon> pokedex, ArrayList<Move> movedex)
	{
		Trainer Ritchie = new Trainer("Ritchie");
		Ritchie.addToParty(pokedex.get(3).copy());
		Ritchie.getParty().get(0).assignRandomMoves(movedex);
		Ritchie.getParty().get(0).setNickname("Sparky");
		Ritchie.getParty().get(0).setLevel(20);
		return Ritchie;
	}
	
	// Initialize all the moves
	public static ArrayList<Move> initializeMovedex()
	{
		ArrayList<Move> movedex = new ArrayList<>();
		movedex.add(new M_001_Pound());
		
		movedex.add(new M_033_Tackle());
		
		movedex.add(new M_039_TailWhip());
		
		movedex.add(new M_055_WaterGun());
		
		movedex.add(new M_061_BubbleBeam());
		
		movedex.add(new M_085_ThunderBolt());
		
		movedex.add(new M_087_Thunder());
		return movedex;
	}
}
