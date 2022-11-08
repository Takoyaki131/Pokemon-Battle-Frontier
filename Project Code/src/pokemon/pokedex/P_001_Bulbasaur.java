package pokemon.pokedex;

import java.util.ArrayList;
import java.util.Arrays;

import pokemon.Pokemon;
import types.Grass;
import types.Poison;

public class P_001_Bulbasaur extends Pokemon implements Grass, Poison{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public P_001_Bulbasaur()
	{
		super();
		this.name = this.nickname = "Bulbasaur";
		this.pokedex_number = 1;
		
		this.base_max_hp = 45;
		this.base_attack = 49;
		this.base_defense = 49;
		this.base_special_attack = 65;
		this.base_special_defense = 65;
		this.base_speed = 45;
		
		this.learnable_moves = new ArrayList<Integer>(Arrays.asList());
	}
	
	public P_001_Bulbasaur copy() {
		P_001_Bulbasaur copy = new P_001_Bulbasaur();
		copy.setIV_Values();
		return copy;
	}

}
