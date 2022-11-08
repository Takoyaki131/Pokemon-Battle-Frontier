package move.movedex;

import move.move_tree.AttackMove;
import move.move_tree.SpecialAttack;
import types.Water;

public class M_055_WaterGun extends AttackMove implements Water, SpecialAttack{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public M_055_WaterGun() {
		super(55, "Water Gun", 25, 1, 40);
		// TODO Auto-generated constructor stub
	}

	@Override
	public M_055_WaterGun copy() {
		return new M_055_WaterGun();
	}

}
