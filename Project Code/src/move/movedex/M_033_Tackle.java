package move.movedex;

import move.move_tree.AttackMove;
import move.move_tree.PhysicalAttack;
import types.Normal;

public class M_033_Tackle extends AttackMove implements PhysicalAttack, Normal{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public M_033_Tackle() {
		super(33, "Tackle", 30, 1, 35);
		// TODO Auto-generated constructor stub
	}

	public M_033_Tackle(M_033_Tackle c)
	{
		super(c.power, c.name, c.power, c.accuracy, c.power);
	}
	
	public M_033_Tackle copy()
	{
		return new M_033_Tackle(this);
	}
	
}
