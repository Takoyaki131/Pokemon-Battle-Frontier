package trainer;
import pokemon.Pokemon;

import java.io.Serializable;
import java.util.ArrayList;

public class Trainer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	ArrayList<Pokemon> party = new ArrayList<>();
	
	public Trainer(String name) {
		super();
		this.name = name;
	}
	
	public void addToParty(Pokemon pokemon)
	{
		party.add(pokemon);
	}
	
	public void displayPartyDetailed()
	{
		System.out.println("\n" + this.name + "'s Party");
		for(Pokemon pokemon : party)
		{
			pokemon.displayInformation();
		}
		System.out.println();
	}

	public void displayPartyBasic()
	{
		System.out.println("\n" + this.name + "'s Party");
		for(int i = 0; i < party.size(); i++)
		{
			System.out.println((i+1) + ". " + party.get(i).getName());
		}
		System.out.println();
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Pokemon> getParty() {
		return party;
	}

	public void setParty(ArrayList<Pokemon> party) {
		this.party = party;
	}

	
	
}
