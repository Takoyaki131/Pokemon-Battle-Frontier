package pokemon.pokedex;

import java.util.ArrayList;
import java.util.Arrays;

import pokemon.Pokemon;
import types.Water;

public class P_007_Squirtle extends Pokemon implements Water{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public P_007_Squirtle()
	{
		super();
		this.name = this.nickname = "Squirtle";
		this.pokedex_number = 7;
		
		this.base_max_hp = 44;
		this.base_attack = 48;
		this.base_defense = 65;
		this.base_special_attack = 60;
		this.base_special_defense = 54;
		this.base_speed = 43;
		
		this.learnable_moves = new ArrayList<Integer>(Arrays.asList(1, 33, 39, 55, 61));
	}
	@Override
	public P_007_Squirtle copy() {
		P_007_Squirtle copy = new P_007_Squirtle();
		copy.setIV_Values();
		return copy;
	}

}
