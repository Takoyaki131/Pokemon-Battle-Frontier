package pokemon.pokedex;

import pokemon.Pokemon;
import types.Electric;
import types.Normal;

public class P_025_Pikachu extends Pokemon implements Electric, Normal{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public P_025_Pikachu() {
		super(25, "Pikachu", 35, 55, 30, 50, 40,90);
		// TODO Auto-generated constructor stub
	}
	
	public P_025_Pikachu(P_025_Pikachu c)
	{
		super(c.pokedex_number, c.name, c.base_max_hp, c.base_attack, c.base_defense, 
				c.base_special_attack, c.base_special_defense, c.base_speed);
		setIVValues();
		
	}
	public P_025_Pikachu copy()
	{
		return new P_025_Pikachu(this);
	}
}
