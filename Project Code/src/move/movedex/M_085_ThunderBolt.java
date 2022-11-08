package move.movedex;

import move.move_tree.AttackMove;
import move.move_tree.SpecialAttack;
import move.status_effect.ApplyParalyze;
import types.Electric;

public class M_085_ThunderBolt extends AttackMove implements Electric, SpecialAttack, ApplyParalyze {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public M_085_ThunderBolt() {
		super(85, "Thunderbolt", 15, 1, 90);
		// TODO Auto-generated constructor stub
	}

	public M_085_ThunderBolt copy()
	{
		return new M_085_ThunderBolt();
	}
	
	@Override
	public double getParalyzeChance() {
		// TODO Auto-generated method stub
		return .1;
	}

}
