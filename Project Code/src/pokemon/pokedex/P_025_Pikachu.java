package pokemon.pokedex;

import java.util.ArrayList;
import java.util.Arrays;

import pokemon.Pokemon;
import types.Electric;
import types.Normal;

public class P_025_Pikachu extends Pokemon implements Electric, Normal{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public P_025_Pikachu() {
		super();
		this.name = this.nickname = "Pikachu";
		this.pokedex_number = 25;
		
		this.base_max_hp = 35;
		this.base_attack = 55;
		this.base_defense = 30;
		this.base_special_attack = 50;
		this.base_special_defense = 40;
		this.base_speed = 90;
		
		this.learnable_moves = new ArrayList<Integer>(Arrays.asList(1, 33, 39, 85, 87));

	}
	
	public P_025_Pikachu copy()
	{
		P_025_Pikachu copy = new P_025_Pikachu();
		copy.setIV_Values();
		return copy;
	}
}
