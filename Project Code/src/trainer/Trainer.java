package trainer;
import pokemon.Pokemon;
import java.util.ArrayList;

public class Trainer {
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
	
	public void displayParty()
	{
		System.out.println("\n" + this.name + "'s Party");
		for(Pokemon pokemon : party)
		{
			pokemon.displayInformation();
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
