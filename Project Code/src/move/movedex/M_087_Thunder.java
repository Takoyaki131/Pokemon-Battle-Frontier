package move.movedex;

import move.move_tree.AttackMove;
import move.move_tree.SpecialAttack;
import move.status_effect.ApplyParalyze;
import types.Electric;

public class M_087_Thunder extends AttackMove implements SpecialAttack, Electric, ApplyParalyze{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public M_087_Thunder() {
		super(87, "Thunder", 10, .7, 110);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getParalyzeChance() {
		// TODO Auto-generated method stub
		return .1;
	}

	@Override
	public M_087_Thunder copy() {
		// TODO Auto-generated method stub
		return new M_087_Thunder();
	}

}
