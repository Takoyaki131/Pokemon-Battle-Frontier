package move.movedex;

import move.move_tree.ApplyStatChange;
import move.move_tree.AttackMove;
import move.move_tree.SpecialAttack;
import move.statistic_effects_enemy.TargetSpeedMinusOne;
import types.Water;

public class M_061_BubbleBeam extends AttackMove implements SpecialAttack, Water, ApplyStatChange, TargetSpeedMinusOne{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public M_061_BubbleBeam() {
		super(61, "Bubble Beam", 20, 1, 65);
		// TODO Auto-generated constructor stub
	}

	public M_061_BubbleBeam copy()
	{
		return new M_061_BubbleBeam();
	}

	@Override
	public double getApplyChance() {
		// TODO Auto-generated method stub
		return .33;
	}
}
