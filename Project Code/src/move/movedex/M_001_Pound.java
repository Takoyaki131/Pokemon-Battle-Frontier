package move.movedex;

import move.move_tree.AttackMove;
import move.move_tree.PhysicalAttack;
import types.Normal;

public class M_001_Pound extends AttackMove implements PhysicalAttack, Normal{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public M_001_Pound() {
		super(1, "Pound", 35, 1, 40);
		// TODO Auto-generated constructor stub
	}
	public M_001_Pound copy()
	{
		return new M_001_Pound();
	}
}
