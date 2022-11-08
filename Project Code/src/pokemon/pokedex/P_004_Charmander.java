package pokemon.pokedex;

import java.util.ArrayList;
import java.util.Arrays;

import pokemon.Pokemon;
import types.Fire;

public class P_004_Charmander extends Pokemon implements Fire{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public P_004_Charmander()
	{
		super();
		this.name = this.nickname = "Charmander";
		this.pokedex_number = 4;
		
		this.base_max_hp = 39;
		this.base_attack = 52;
		this.base_defense = 43;
		this.base_special_attack = 60;
		this.base_special_defense = 50;
		this.base_speed = 65;
		
		this.learnable_moves = new ArrayList<Integer>(Arrays.asList());
	}
	
	@Override
	public P_004_Charmander copy() {
		P_004_Charmander copy = new P_004_Charmander();
		copy.setIV_Values();
		return copy;
	}

}
