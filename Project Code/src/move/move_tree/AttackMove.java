package move.move_tree;

public abstract class AttackMove extends Move{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int power;
	public AttackMove(int tm_no, String name, int max_pp, double accuracy, int power) {
		super(tm_no, name, max_pp, accuracy);
		// TODO Auto-generated constructor stub
		this.power = power;
	}
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	
}
