package main;

import java.util.ArrayList;

import move.move_tree.*;
import move.movedex.*;
import pokemon.Pokemon;
import pokemon.pokedex.*;
import trainer.Trainer;
import types.*;

public class initialize {
	
	public static ArrayList<Pokemon> initializePokedex()
	{
		ArrayList<Pokemon> pokedex = new ArrayList<>();
		pokedex.add(new P_025_Pikachu());
		return pokedex;
	}
	
	public static Trainer createAsh(ArrayList<Pokemon> pokedex, ArrayList<Move> movedex)
	{
		Trainer ash = new Trainer("Ash");
	
		ash.addToParty(pokedex.get(0).copy());
		ash.getParty().get(0).learnMove(movedex.get(0).copy(), 0);
		ash.getParty().get(0).learnMove(movedex.get(1).copy(), 1);
		ash.getParty().get(0).setLevel(20);
		return ash;
	}
	
	public static Trainer createRitchie(ArrayList<Pokemon> pokedex, ArrayList<Move> movedex)
	{
		Trainer Ritchie = new Trainer("Ritchie");
		Ritchie.addToParty(pokedex.get(0).copy());
		Ritchie.getParty().get(0).giveNickname("Sparky");
		Ritchie.getParty().get(0).learnMove(movedex.get(0).copy(), 0);
		Ritchie.getParty().get(0).setLevel(20);
		return Ritchie;
	}
	public static ArrayList<Move> initializeMovedex()
	{
		ArrayList<Move> movedex = new ArrayList<>();
		movedex.add(new M_033_Tackle());
		movedex.add(new M_085_ThunderBolt());
		return movedex;
	}
}
