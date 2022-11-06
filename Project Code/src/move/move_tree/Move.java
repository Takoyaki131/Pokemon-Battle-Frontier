package move.move_tree;

import types.*;

public class Move {
	protected int tm_no;
	protected String name;
	protected String type;
	protected int current_pp;
	protected int max_pp;
	protected double accuracy;
	
	// Constructor
	public Move(int tm_no, String name, int max_pp, double accuracy) {
		super();
		this.tm_no = tm_no;
		this.name = name;
		this.current_pp = max_pp;
		this.max_pp = max_pp;
		this.accuracy = accuracy;
	}

	// Copy Constructor
	public Move(Move c) {
		// TODO Auto-generated constructor stub
		this.tm_no = c.tm_no;
		this.name = c.name;
		this.current_pp = c.max_pp;
		this.max_pp = c.max_pp;
		this.accuracy = c.accuracy;
	}

	public Move copy() {
        return new Move(this);
    }
	
	public void displayMoveInformation()
	{
		System.out.print("\t" + this.name + " ");
		if(this instanceof Normal) System.out.print(" Normal ");
		System.out.println(this.current_pp + " / " + this.max_pp);
	}
	// Setters and Getters
	public int getTm_no() {
		return tm_no;
	}

	public void setTm_no(int tm_no) {
		this.tm_no = tm_no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCurrent_pp() {
		return current_pp;
	}

	public void setCurrent_pp(int current_pp) {
		this.current_pp = current_pp;
	}

	public int getMax_pp() {
		return max_pp;
	}

	public void setMax_pp(int max_pp) {
		this.max_pp = max_pp;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
	
}
