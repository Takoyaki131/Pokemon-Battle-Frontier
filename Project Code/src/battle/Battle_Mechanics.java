package battle;

import move.extras.*;
import move.move_tree.*;
import move.statistic_effects_enemy.*;
import move.statistic_effects_user.*;
import move.status_effect.*;
import pokemon.Pokemon;
import types.*;

public class Battle_Mechanics {

	final static int MAX_STAT_CHANGE = 2;
	/* Calculate the damage a move can inflict
	 * Input: 	level		- Attacker pokemon's level
	 * 			attack 		- Attacker pokemon's attack or special attack
	 * 			defense 	- Defender pokemon's defense or special defense
	 * 			power 		- Attacker's move power
	 * 			type_bonus	- Multiplier based on type matchup
	 * Returns: int 	- The calculated damage
	 */
	public static int calcDamage(int level, int attack, int defense, int power, double type_bonus)
	{
		/* Calculate damage WIP
		 *  Formula ((((2 * Level / 5 + 2) * AttackStat * AttackPower / DefenseStat) / 50) + 2) * STAB * Weakness/Resistance * RandomNumber / 100 ) - From wiki
		 */
		int damage = (int) (((((2 * level / 5 + 2) * attack * power / defense) / 50) + 2) * type_bonus );
		return damage;
	}
	
	/* Checks the hp of a pokemon
	 * Input: 	pokemon	- Pokemon to check
	 * Returns: void
	 */
	public static boolean isFainted(Pokemon pokemon)
	{
		if(pokemon.getCurrent_hp() <= 0) return true;
		return false;
	}
	
	/* Checks/Updates the hp if a status effect persists
	 * Input: 	pokemon	- Pokemon to check
	 * Returns: void
	 */
	public static void postMoveStatusEffects(Pokemon pokemon)
	{
		// Update condition of left_pokemon (Poison / Burn Damage)
		if (pokemon.getBattle_status() == "PSN")
			{
				System.out.println(pokemon.getName() + " is hurt by its poison!");
				pokemon.takeDamage(pokemon.getCurrent_max_hp() * 1/16);
			}
		if (pokemon.getBattle_status() == "BRN")
			{
				System.out.println(pokemon.getName() + " is hurt by its burn!");
				pokemon.takeDamage(pokemon.getCurrent_max_hp() * 1/16);
			}
	}
	
	/* Determine if the pokemon is allowed to use a move
	 * Input: 	user		- Pokemon in question
	 * Returns: boolean 	- If the pokemon can use a move
	 */
	public static boolean canUseMove(Pokemon user)
	{
		// Check for flinch - cannot use move
		if (user.is_flinched() == true)
		{
			System.out.println(user.getName() + " flinched and couldn't move!");
			user.set_flinched(false);
			return false;
		}
		
		/* Confusion 
		 *  - 50% chance to hurt itself
		 *  - damage is 40 power typeless physical attack
		 */
		if (user.getIs_confused() == true)
		{
			int chance_to_hurt = (int) (Math.random() * 2);
			if (chance_to_hurt != 1) return true;
			
			// "hurt itself in confusion"
			else
			{
				System.out.println(user.getName() + " hurt itself in confusion");
				int damage = (int) (((((2 * user.getLevel() / 5 + 2) * user.getBattle_attack() * 40 / user.getBattle_defense()) / 50) + 2));
				user.takeDamage(damage);
				return false;
			}
		}
		
		/* Paralyze
		 * 	- 25% chance to not move 
		 *  - speed reduced by 25% *NEED ADD
		 */
		if (user.getBattle_status() == "PAR")
		{
			int chance_to_be_paralyzed = (int) (Math.random() * 4);
			/*
			if (console_info == true)
			{
				System.out.println("Chance Number: " + chance_to_be_paralyzed);
			}
			*/
			if (chance_to_be_paralyzed != 1) return true;
			else
				System.out.println(user.getName() + " is paralyzed! It can't move!");
				return false;
		}
		
		/* Frozen condition 
		 *  - 20% chance to thaw out each turn
		 */
		if (user.getBattle_status() == "FRZ")
		{
			int chance_to_thaw = (int) (Math.random() * 5);
			/*
			if (console_info == true)
			{
				System.out.println("Chance Number: " + chance_to_thaw + " / 5");
			}
			*/
			if (chance_to_thaw == 1)
			{
				System.out.println(user.getName() + " thawed out!");
				return true;
			}
			else
				System.out.println(user.getName() + " is frozen! It can't move!");
				return false;
		}
		
		/* Sleep condition 
		 *  - last randomly 1-7 turns
		 */
		if (user.getBattle_status() == "SLP")
		{
			user.setSleep_turns(user.getSleep_turns() - 1);
			if (user.getSleep_turns() != 0)
			{
				System.out.println(user.getName() + " is asleep!");
				return false;
			}
			else
			{
				System.out.println(user.getName() + " woke up!");
				return true;
			}
		}
		
		// Successful checks
		return true;
	}
	
	/* Logic to for single pokemon's turn
	 * Input: 	user 	- Pokemon using the move
	 * 			target 	- Pokemon recive the move (self or target)
	 * Returns: void
	 */
	public static void useMove(Pokemon user, Pokemon target, Move move) 
	{
		// Check move's power point amount
		if (move.getCurrent_pp() == 0) 
		{
			//TODO: Add implementation for struggle
			System.out.println("There is no PP left for this move!");
			return;
		}
		
		// Check if user's current status cancels ability to use move
		if (canUseMove(user) == false) return;
		
		// Output to console
		System.out.println(user.getNickname() + " used " + move.getName() + "! ");
		// Update the pp of the move
		move.setCurrent_pp(move.getCurrent_pp() - 1);
		
		/*Check for move success, i.e the move hit or missed ( this is not the offical formula for accuracy )
		* chance = battle_accuracy(0.0 - 1.0) * move accuracy *(0.0 - 1.0) * target evasion(0.0 - 1.0)
		* result = random double between 0.0 and 1.0
		* if result falls under the chance, the move hits
		*/
		double chance = (user.getBattle_accuracy() * move.getAccuracy() * target.getBattle_evasion() );
		double result = Math.random();
		
		// If move missed, return to second half of the turn
		if (result > chance) 
		{
			// Print move outcome
			System.out.println("The move missed!");
			return;
		}
		
		double type_multiplier = getTypeMultiplier(move, target);
		// Display status matchup message
		if(type_multiplier == 0)
		{
			System.out.println("The move had no effect.");
			return;
		}
		if(type_multiplier < 1.0 && type_multiplier > 0)
		{
			System.out.println("Its not very effective...");
		}
		if(type_multiplier > 1.0)
		{
			System.out.println("Its super effective!");
		}
		
		int damage; // variable holding the damage dealt by move
		// Check type of move
		if (move instanceof AttackMove)
		{
			// Physical Attack
			if(move instanceof PhysicalAttack)
			{
				damage = calcDamage(user.getLevel(), user.getBattle_attack(), target.getBattle_defense(), ((AttackMove) move).getPower(), type_multiplier);
				/* Check if there is a burn status of user pokem
				 * 	- burn 		1/16 of maximun hp and halves physical attack damage
				 */
				if (user.getBattle_status() == "BRN")
				{
					damage /= 2;
				}
				
				//if (console_info == true ) System.out.println("Move did " + damage + " damage! ");
				target.takeDamage(damage);
				
				// Recoil Mechanic
				if (move instanceof HasRecoil)
				{
					int recoilDamage = (int) (((HasRecoil)move).getRecoilRatio() * damage);
					user.takeDamage(recoilDamage);
					System.out.println(user.getName() + " took damage in recoil!");
				}
				// Flinch mechanic
				if (move instanceof ApplyFlinch)
				{
					// Flinch has 30% chance to apply
					int chance_to_be_flinch = (int) (Math.random() * 10);
					//if (console_info == true ) System.out.println("Flinch chance : " + chance_to_be_flinch );
					if ( chance_to_be_flinch <= 3 )
					{
						target.set_flinched(true);
					}
				}
			}
			// Special Attack
			if(move instanceof SpecialAttack)
			{
				damage = calcDamage(user.getLevel(), user.getBattle_special_attack(), target.getBattle_special_defense(), ((AttackMove) move).getPower(), type_multiplier);
				//if (console_info == true ) System.out.println("Move did " + damage + " damage! ");
				target.takeDamage(damage);
			}
		}
		
		// Move statistic effects
		if (move instanceof ApplyStatChange)
		{
			// Check chance to apply the chance
			double statModChance= ( (ApplyStatChange) move).getApplyChance();
			double statModResult = Math.random();
			
			// If move gets apply success, apply correct stat change
			if(statModChance > statModResult)
			{
				applyTargetStatEffects(target, move);
				applyUserStatEffects(user, move);
			}
		}
		
		// Move has a chance to apply a status
		if (move instanceof ApplyParalyze || move instanceof ApplyPoison || move instanceof ApplyBurn
				|| move instanceof ApplySleep || move instanceof ApplyFrozen)
		{
			// Check if target pokemon is already under a status condition
			if (!(target.getBattle_status() == null))
			{
				String status = "The target is already ";
				if (target.getBattle_status().equals("PAR")) status += "paralyzed!";
				if (target.getBattle_status().equals("PSN")) status += "poisoned!";
				if (target.getBattle_status().equals("BRN")) status += "burned!";
				if (target.getBattle_status().equals("SLP")) status += "asleep!";
				if (target.getBattle_status().equals("FRZ")) status += "frozen!";
				System.out.println(status);
				
				// Move fails due to target already have a status condition
				return;
			}
			
			/* If target dosent have pre existing condition, move can attempt to apply
			 * Moves such as poison sting have a .2 or 20% chance to poison and will "roll" to get the applied affect
			 * Move such as thunderwave automatically apply when the move hits
			 * Each status_move contains a method that returns the chance (0.0 - 1)
			 */
			else
			{
				result = Math.random();
				//if (this.console_info == true) System.out.println("Result to apply: " + result);
				if (move instanceof ApplyParalyze)
				{
					chance = ((ApplyParalyze) move).getParalyzeChance();
					//if (this.console_info == true) System.out.println("Chance to apply: " + chance);
					if(result < chance)
					{
						target.setBattle_status("PAR");
						System.out.println("Enemy " + target.getName() + " was paralyzed! ");
					}
				}
				if (move instanceof ApplyPoison)
				{
					chance = ((ApplyPoison) move).getPoisonChance();
					//if (this.console_info == true) System.out.println("Chance to apply: " + chance);
					if (result < chance)
					{
						target.setBattle_status("PSN");
						System.out.println("Enemy " + target.getName() + " was poisoned! ");
					}
				}
				if (move instanceof ApplyBurn)
				{
					chance = ((ApplyBurn) move).getBurnChance();
					//if (this.console_info == true) System.out.println("Chance to apply: " + chance);
					if (result < chance)
					{
						target.setBattle_status("BRN");
						System.out.println("Enemy " + target.getName() + " was burned! ");
						
					}
				}
				if (move instanceof ApplySleep)
				{
					chance = ((ApplySleep) move).getSleepChance();
					//if (this.console_info == true) System.out.println("Chance to apply: " + chance);
					if (result < chance)
					{
					target.setBattle_status("SLP");
					System.out.println("Enemy " + target.getName() + " was put to sleep! ");
					}
				}
				if (move instanceof ApplyFrozen)
				{
					chance = ((ApplyFrozen) move).getFrozenChance();
					//if (this.console_info == true) System.out.println("Chance to apply: " + chance);
					if (result < chance)
					{
						target.setBattle_status("FRZ");
						System.out.println("Enemy " + target.getName() + " was frozen solid! ");
					}
				}
			}
		}
	} // End of useMove method

	/* Apply statistic changes to the user
	 * Input: 	user			- User pokemon
	 * 			move			- Move
	 * Returns: void
	 */
	private static void applyUserStatEffects(Pokemon user, Move move) {
		
		// TODO Auto-generated method stub
		if (move instanceof UserAttackPlusOne || move instanceof UserAttackPlusTwo || move instanceof UserAttackMinusOne || move instanceof UserAttackMinusTwo)
		{
			// Check is the min/max change has been reached
			if (user.getBattle_attack_count() >= MAX_STAT_CHANGE)
			{
				System.out.println(user.getName() + "'s attack cannot go any higher!");
				return;
			}
			if (user.getBattle_attack_count() <= -MAX_STAT_CHANGE)
			{
				System.out.println(user.getName() + "'s attack cannot go any lower!");
				return;
			}
			
			if(move instanceof UserAttackPlusOne)
			{
				user.setBattle_attack_count(user.getBattle_attack_count() + 1);
				user.setBattle_attack(((user.getBase_attack() + user.getIv_values().get(1)) * 2 * (user.getLevel() + user.getBattle_attack_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s attack rose!");
			}
			if(move instanceof UserAttackPlusTwo)
			{
				user.setBattle_attack_count(user.getBattle_attack_count() + 2);
				user.setBattle_attack(((user.getBase_attack() + user.getIv_values().get(1)) * 2 * (user.getLevel() + user.getBattle_attack_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s attack rose sharply");
			}
			if(move instanceof UserAttackMinusOne)
			{
				user.setBattle_attack_count(user.getBattle_attack_count() - 1);
				user.setBattle_attack(((user.getBase_attack() + user.getIv_values().get(1)) * 2 * (user.getLevel() + user.getBattle_attack_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s attack fell");
			}
			if(move instanceof UserAttackMinusTwo)
			{
				user.setBattle_attack_count(user.getBattle_attack_count() - 2);
				user.setBattle_attack(((user.getBase_attack() + user.getIv_values().get(1)) * 2 * (user.getLevel() + user.getBattle_attack_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s attack rose fell harshly");
			}
		}
		
		//Defense Stat Changes
		if (move instanceof UserDefensePlusOne || move instanceof UserDefensePlusTwo || move instanceof UserDefenseMinusOne || move instanceof UserDefenseMinusTwo)
		{
			// Check is the min/max change has been reached
			if (user.getBattle_defense_count() >= MAX_STAT_CHANGE)
			{
				System.out.println(user.getName() + "'s defense cannot go any higher!");
				return;
			}
			if (user.getBattle_defense_count() <= -MAX_STAT_CHANGE)
			{
				System.out.println(user.getName() + "'s defense cannot go any lower!");
				return;
			}
			
			if (move instanceof UserDefensePlusOne)
			{
				user.setBattle_defense_count(user.getBattle_defense_count() + 1);
				user.setBattle_defense(((user.getBase_defense() + user.getIv_values().get(2)) * 2 * (user.getLevel() + user.getBattle_defense_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s defense rose!");
			}
			if (move instanceof UserDefensePlusTwo)
			{
				user.setBattle_defense_count(user.getBattle_defense_count() + 2);
				user.setBattle_defense(((user.getBase_defense() + user.getIv_values().get(2)) * 2 * (user.getLevel() + user.getBattle_defense_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s defense rose sharply!");
			}
			if (move instanceof UserDefenseMinusOne)
			{
				user.setBattle_defense_count(user.getBattle_defense_count() - 1);
				user.setBattle_defense(((user.getBase_defense() + user.getIv_values().get(2)) * 2 * (user.getLevel() + user.getBattle_defense_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s defense fell!");
			}
			if (move instanceof UserDefenseMinusTwo)
			{
				user.setBattle_defense_count(user.getBattle_defense_count() - 2);
				user.setBattle_defense(((user.getBase_defense() + user.getIv_values().get(2)) * 2 * (user.getLevel() + user.getBattle_defense_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s defense fell sharply!");
			}
		}
		
		// SpecialAttack Stat Changes
		if (move instanceof UserSpecialAttackPlusOne || move instanceof UserSpecialAttackPlusTwo || move instanceof UserSpecialAttackMinusOne || move instanceof UserSpecialAttackMinusTwo)
		{
			// Check is the min/max change has been reached
			if (user.getBattle_special_attack_count() >= MAX_STAT_CHANGE)
			{
				System.out.println(user.getName() + "'s special attack cannot go any higher!");
				return;
			}
			if (user.getBattle_special_attack_count() <= -MAX_STAT_CHANGE)
			{
				System.out.println(user.getName() + "'s special attack cannot go any lower!");
				return;
			}
			
			if (move instanceof UserSpecialAttackPlusOne)
			{
				user.setBattle_special_attack_count(user.getBattle_special_attack_count() + 1);
				user.setBattle_special_attack(((user.getBase_special_attack() + user.getIv_values().get(3)) * 2 * (user.getLevel() + user.getBattle_special_attack_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s special attack rose!");
			}
			if (move instanceof UserSpecialAttackPlusTwo)
			{
				user.setBattle_special_attack_count(user.getBattle_special_attack_count() + 2);
				user.setBattle_special_attack(((user.getBase_special_attack() + user.getIv_values().get(3)) * 2 * (user.getLevel() + user.getBattle_special_attack_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s special attack rose sharply!");
			}
			if (move instanceof UserSpecialAttackMinusOne)
			{
				user.setBattle_special_attack_count(user.getBattle_special_attack_count() - 1);
				user.setBattle_special_attack(((user.getBase_special_attack() + user.getIv_values().get(3)) * 2 * (user.getLevel() + user.getBattle_special_attack_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s special attack fell!");
			}
			if (move instanceof UserSpecialAttackMinusTwo)
			{
				user.setBattle_special_attack_count(user.getBattle_special_attack_count() - 2);
				user.setBattle_special_attack(((user.getBase_special_attack() + user.getIv_values().get(3)) * 2 * (user.getLevel() + user.getBattle_special_attack_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s special attack fell sharply!");
			}
		}
		
		// Special Defense Stat Changes
		if (move instanceof UserSpecialDefensePlusOne || move instanceof UserSpecialDefensePlusTwo || move instanceof UserSpecialDefenseMinusOne || move instanceof UserSpecialDefenseMinusTwo)
		{

			// Check is the max change has been reached
			if (user.getBattle_special_defense_count() <= -MAX_STAT_CHANGE)
			{
				System.out.println(user.getName() + "'s special defense cannot go any higher!");
				return;
			}
			if (user.getBattle_special_defense_count() <= -MAX_STAT_CHANGE)
			{
				System.out.println(user.getName() + "'s special defense cannot go any lower!");
				return;
			}
			
			if (move instanceof UserSpecialDefensePlusOne)
			{
				user.setBattle_special_defense_count(user.getBattle_special_defense_count() + 1);
				user.setBattle_special_defense(((user.getBase_special_defense() + user.getIv_values().get(4)) * 2 * (user.getLevel() + user.getBattle_special_defense_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s special defense rose!");
			}
			if (move instanceof UserSpecialDefensePlusTwo)
			{
				user.setBattle_special_defense_count(user.getBattle_special_defense_count() + 2);
				user.setBattle_special_defense(((user.getBase_special_defense() + user.getIv_values().get(4)) * 2 * (user.getLevel() + user.getBattle_special_defense_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s special defense rose sharply!");
			}
			if (move instanceof UserSpecialDefenseMinusOne)
			{
				user.setBattle_special_defense_count(user.getBattle_special_defense_count() - 1);
				user.setBattle_special_defense(((user.getBase_special_defense() + user.getIv_values().get(4)) * 2 * (user.getLevel() + user.getBattle_special_defense_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s special defense fell!");
			}
			if (move instanceof UserSpecialDefenseMinusTwo)
			{
				user.setBattle_special_defense_count(user.getBattle_special_defense_count() - 2);
				user.setBattle_special_defense(((user.getBase_special_defense() + user.getIv_values().get(4)) * 2 * (user.getLevel() + user.getBattle_special_defense_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s special defense fell harsly!");
			}
		}
		
		// Speed Stat Changes
		if (move instanceof UserSpeedPlusOne || move instanceof UserSpeedPlusTwo || move instanceof UserSpeedMinusOne || move instanceof UserSpeedMinusTwo)
		{
			// Check is the min/max change has been reached
			if (user.getBattle_speed_count() >= MAX_STAT_CHANGE)
			{
				System.out.println(user.getName() + "'s speed cannot go any higher!");
				return;
			}
			if (user.getBattle_speed_count() <= -MAX_STAT_CHANGE)
			{
				System.out.println(user.getName() + "'s speed defense cannot go any lower!");
				return;
			}
			
			if (move instanceof UserSpeedPlusOne)
			{
				user.setBattle_speed_count(user.getBattle_speed_count() + 1);
				user.setBattle_speed(((user.getBase_speed() + user.getIv_values().get(5)) * 2 * (user.getLevel() + user.getBattle_speed_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s speed rose!");
			}
			if (move instanceof UserSpeedPlusTwo)
			{
				user.setBattle_speed_count(user.getBattle_speed_count() + 2);
				user.setBattle_speed(((user.getBase_speed() + user.getIv_values().get(5)) * 2 * (user.getLevel() + user.getBattle_speed_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s speed rose sharply!");
			}
			if (move instanceof UserSpeedMinusOne)
			{
				user.setBattle_speed_count(user.getBattle_speed_count() - 1);
				user.setBattle_speed(((user.getBase_speed() + user.getIv_values().get(5)) * 2 * (user.getLevel() + user.getBattle_speed_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s speed fell!");
			}
			if (move instanceof UserSpeedMinusTwo)
			{
				user.setBattle_speed_count(user.getBattle_speed_count() - 2);
				user.setBattle_speed(((user.getBase_speed() + user.getIv_values().get(5)) * 2 * (user.getLevel() + user.getBattle_speed_count()) / 100 ) + 5);
				System.out.println(user.getName() + "'s speed fell harsly!");
			}
		}
		
		// Accuracy Stat Change
		if (move instanceof UserAccuracyPlusOne || move instanceof UserAccuracyPlusTwo || move instanceof UserAccuracyMinusOne || move instanceof UserAccuracyMinusTwo )
		{
			// Check min/max change been reached
			if (user.getBattle_accuracy_count() >= MAX_STAT_CHANGE)
			{
				System.out.println(user.getName() + "'s accuracy cannot go any higher!");
				return;
			}
			if (user.getBattle_accuracy_count() <= -MAX_STAT_CHANGE)
			{
				System.out.println(user.getName() + "'s speed accuracy cannot go any lower!");
				return;
			}
			
			if (move instanceof UserAccuracyPlusOne)
			{
				user.setBattle_accuracy_count(user.getBattle_accuracy_count() + 1);
				user.setBattle_accuracy(user.getBattle_accuracy() + .1);
				System.out.println(user.getName() + "'s speed rose!");
			}
			if (move instanceof UserAccuracyPlusTwo)
			{
				user.setBattle_accuracy_count(user.getBattle_accuracy_count() + 2);
				user.setBattle_accuracy(user.getBattle_accuracy() + .2);
				System.out.println(user.getName() + "'s speed rose sharply!");
			}
			if (move instanceof UserAccuracyMinusOne)
			{
				user.setBattle_accuracy_count(user.getBattle_accuracy_count() - 1);
				user.setBattle_accuracy(user.getBattle_accuracy() - .1);
				System.out.println(user.getName() + "'s speed fell!");
			}
			if (move instanceof UserAccuracyMinusTwo)
			{
				user.setBattle_accuracy_count(user.getBattle_accuracy_count() - 2);
				user.setBattle_accuracy(user.getBattle_accuracy() - .2);
				System.out.println(user.getName() + "'s speed fell harsly!");
			}
		}
		
		// Evasion Stat Change
		if (move instanceof UserEvasionPlusOne || move instanceof UserEvasionPlusTwo || move instanceof UserEvasionMinusOne || move instanceof UserEvasionMinusTwo )
		{
			// Check min/max change been reached
			if (user.getBattle_evasion_count() >= MAX_STAT_CHANGE)
			{
				System.out.println(user.getName() + "'s evasion cannot go any higher!");
				return;
			}
			if (user.getBattle_evasion_count() <= -MAX_STAT_CHANGE)
			{
				System.out.println(user.getName() + "'s evasion cannot go any lower!");
				return;
			}
			
			if (move instanceof UserEvasionPlusOne)
			{
				user.setBattle_evasion_count(user.getBattle_evasion_count() + 1);
				user.setBattle_evasion(user.getBattle_evasion() + .1);
				System.out.println(user.getName() + "'s evasion rose!");
			}
			if (move instanceof UserEvasionPlusTwo)
			{
				user.setBattle_evasion_count(user.getBattle_evasion_count() + 2);
				user.setBattle_evasion(user.getBattle_evasion() + .2);
				System.out.println(user.getName() + "'s evasion rose sharply!");
			}
			if (move instanceof UserEvasionMinusOne)
			{
				user.setBattle_evasion_count(user.getBattle_evasion_count() - 1);
				user.setBattle_evasion(user.getBattle_evasion() - .1);
				System.out.println(user.getName() + "'s evasion fell!");
			}
			if (move instanceof UserEvasionMinusTwo)
			{
				user.setBattle_evasion_count(user.getBattle_evasion_count() - 2);
				user.setBattle_evasion(user.getBattle_evasion() - .2);
				System.out.println(user.getName() + "'s evasion fell harsly!");
			}
		}
	}

	/* Apply statistic changes to the target
	 * Input: 	target			- Target pokemon
	 * 			move			- Move
	 * Returns: void
	 */
	public static void applyTargetStatEffects(Pokemon target, Move move) 
	{
	
		if (move instanceof TargetAttackPlusOne || move instanceof TargetAttackPlusTwo || move instanceof TargetAttackMinusOne || move instanceof TargetAttackMinusTwo)
		{
			// Check is the min/max change has been reached
			if (target.getBattle_attack_count() >= MAX_STAT_CHANGE)
			{
				System.out.println(target.getName() + "'s attack cannot go any higher!");
				return;
			}
			if (target.getBattle_attack_count() <= -MAX_STAT_CHANGE)
			{
				System.out.println(target.getName() + "'s attack cannot go any lower!");
				return;
			}
			
			if(move instanceof TargetAttackPlusOne)
			{
				target.setBattle_attack_count(target.getBattle_attack_count() + 1);
				target.setBattle_attack(((target.getBase_attack() + target.getIv_values().get(1)) * 2 * (target.getLevel() + target.getBattle_attack_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s attack rose!");
			}
			if(move instanceof TargetAttackPlusTwo)
			{
				target.setBattle_attack_count(target.getBattle_attack_count() + 2);
				target.setBattle_attack(((target.getBase_attack() + target.getIv_values().get(1)) * 2 * (target.getLevel() + target.getBattle_attack_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s attack rose sharply");
			}
			if(move instanceof TargetAttackMinusOne)
			{
				target.setBattle_attack_count(target.getBattle_attack_count() - 1);
				target.setBattle_attack(((target.getBase_attack() + target.getIv_values().get(1)) * 2 * (target.getLevel() + target.getBattle_attack_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s attack fell");
			}
			if(move instanceof TargetAttackMinusTwo)
			{
				target.setBattle_attack_count(target.getBattle_attack_count() - 2);
				target.setBattle_attack(((target.getBase_attack() + target.getIv_values().get(1)) * 2 * (target.getLevel() + target.getBattle_attack_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s attack rose fell harshly");
			}
		}
		
		//Defense Stat Changes
		if (move instanceof TargetDefensePlusOne || move instanceof TargetDefensePlusTwo || move instanceof TargetDefenseMinusOne || move instanceof TargetDefenseMinusTwo)
		{
			// Check is the min/max change has been reached
			if (target.getBattle_defense_count() >= MAX_STAT_CHANGE)
			{
				System.out.println(target.getName() + "'s defense cannot go any higher!");
				return;
			}
			if (target.getBattle_defense_count() <= -MAX_STAT_CHANGE)
			{
				System.out.println(target.getName() + "'s defense cannot go any lower!");
				return;
			}
			
			if (move instanceof TargetDefensePlusOne)
			{
				target.setBattle_defense_count(target.getBattle_defense_count() + 1);
				target.setBattle_defense(((target.getBase_defense() + target.getIv_values().get(2)) * 2 * (target.getLevel() + target.getBattle_defense_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s defense rose!");
			}
			if (move instanceof TargetDefensePlusTwo)
			{
				target.setBattle_defense_count(target.getBattle_defense_count() + 2);
				target.setBattle_defense(((target.getBase_defense() + target.getIv_values().get(2)) * 2 * (target.getLevel() + target.getBattle_defense_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s defense rose sharply!");
			}
			if (move instanceof TargetDefenseMinusOne)
			{
				target.setBattle_defense_count(target.getBattle_defense_count() - 1);
				target.setBattle_defense(((target.getBase_defense() + target.getIv_values().get(2)) * 2 * (target.getLevel() + target.getBattle_defense_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s defense fell!");
			}
			if (move instanceof TargetDefenseMinusTwo)
			{
				target.setBattle_defense_count(target.getBattle_defense_count() - 2);
				target.setBattle_defense(((target.getBase_defense() + target.getIv_values().get(2)) * 2 * (target.getLevel() + target.getBattle_defense_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s defense fell sharply!");
			}
		}
		
		// SpecialAttack Stat Changes
		if (move instanceof TargetSpecialAttackPlusOne || move instanceof TargetSpecialAttackPlusTwo || move instanceof TargetSpecialAttackMinusOne || move instanceof TargetSpecialAttackMinusTwo)
		{
			// Check is the min/max change has been reached
			if (target.getBattle_special_attack_count() >= MAX_STAT_CHANGE)
			{
				System.out.println(target.getName() + "'s special attack cannot go any higher!");
				return;
			}
			if (target.getBattle_special_attack_count() <= -MAX_STAT_CHANGE)
			{
				System.out.println(target.getName() + "'s special attack cannot go any lower!");
				return;
			}
			
			if (move instanceof TargetSpecialAttackPlusOne)
			{
				target.setBattle_special_attack_count(target.getBattle_special_attack_count() + 1);
				target.setBattle_special_attack(((target.getBase_special_attack() + target.getIv_values().get(3)) * 2 * (target.getLevel() + target.getBattle_special_attack_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s special attack rose!");
			}
			if (move instanceof TargetSpecialAttackPlusTwo)
			{
				target.setBattle_special_attack_count(target.getBattle_special_attack_count() + 2);
				target.setBattle_special_attack(((target.getBase_special_attack() + target.getIv_values().get(3)) * 2 * (target.getLevel() + target.getBattle_special_attack_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s special attack rose sharply!");
			}
			if (move instanceof TargetSpecialAttackMinusOne)
			{
				target.setBattle_special_attack_count(target.getBattle_special_attack_count() - 1);
				target.setBattle_special_attack(((target.getBase_special_attack() + target.getIv_values().get(3)) * 2 * (target.getLevel() + target.getBattle_special_attack_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s special attack fell!");
			}
			if (move instanceof TargetSpecialAttackMinusTwo)
			{
				target.setBattle_special_attack_count(target.getBattle_special_attack_count() - 2);
				target.setBattle_special_attack(((target.getBase_special_attack() + target.getIv_values().get(3)) * 2 * (target.getLevel() + target.getBattle_special_attack_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s special attack fell sharply!");
			}
		}
		
		// Special Defense Stat Changes
		if (move instanceof TargetSpecialDefensePlusOne || move instanceof TargetSpecialDefensePlusTwo || move instanceof TargetSpecialDefenseMinusOne || move instanceof TargetSpecialDefenseMinusTwo)
		{

			// Check is the max change has been reached
			if (target.getBattle_special_defense_count() >= MAX_STAT_CHANGE)
			{
				System.out.println(target.getName() + "'s special defense cannot go any higher!");
				return;
			}
			if (target.getBattle_special_defense_count() <= -MAX_STAT_CHANGE)
			{
				System.out.println(target.getName() + "'s special defense cannot go any lower!");
				return;
			}
			
			if (move instanceof TargetSpecialDefensePlusOne)
			{
				target.setBattle_special_defense_count(target.getBattle_special_defense_count() + 1);
				target.setBattle_special_defense(((target.getBase_special_defense() + target.getIv_values().get(4)) * 2 * (target.getLevel() + target.getBattle_special_defense_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s special defense rose!");
			}
			if (move instanceof TargetSpecialDefensePlusTwo)
			{
				target.setBattle_special_defense_count(target.getBattle_special_defense_count() + 2);
				target.setBattle_special_defense(((target.getBase_special_defense() + target.getIv_values().get(4)) * 2 * (target.getLevel() + target.getBattle_special_defense_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s special defense rose sharply!");
			}
			if (move instanceof TargetSpecialDefenseMinusOne)
			{
				target.setBattle_special_defense_count(target.getBattle_special_defense_count() - 1);
				target.setBattle_special_defense(((target.getBase_special_defense() + target.getIv_values().get(4)) * 2 * (target.getLevel() + target.getBattle_special_defense_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s special defense fell!");
			}
			if (move instanceof TargetSpecialDefenseMinusTwo)
			{
				target.setBattle_special_defense_count(target.getBattle_special_defense_count() - 2);
				target.setBattle_special_defense(((target.getBase_special_defense() + target.getIv_values().get(4)) * 2 * (target.getLevel() + target.getBattle_special_defense_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s special defense fell harsly!");
			}
		}
		
		// Speed Stat Changes
		if (move instanceof TargetSpeedPlusOne || move instanceof TargetSpeedPlusTwo || move instanceof TargetSpeedMinusOne || move instanceof TargetSpeedMinusTwo)
		{
			// Check is the min/max change has been reached
			if (target.getBattle_speed_count() >= MAX_STAT_CHANGE)
			{
				System.out.println(target.getName() + "'s speed cannot go any higher!");
				return;
			}
			if (target.getBattle_speed_count() <= -MAX_STAT_CHANGE)
			{
				System.out.println(target.getName() + "'s speed defense cannot go any lower!");
				return;
			}
			
			if (move instanceof TargetSpeedPlusOne)
			{
				target.setBattle_speed_count(target.getBattle_speed_count() + 1);
				target.setBattle_speed(((target.getBase_speed() + target.getIv_values().get(5)) * 2 * (target.getLevel() + target.getBattle_speed_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s speed rose!");
			}
			if (move instanceof TargetSpeedPlusTwo)
			{
				target.setBattle_speed_count(target.getBattle_speed_count() + 2);
				target.setBattle_speed(((target.getBase_speed() + target.getIv_values().get(5)) * 2 * (target.getLevel() + target.getBattle_speed_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s speed rose sharply!");
			}
			if (move instanceof TargetSpeedMinusOne)
			{
				target.setBattle_speed_count(target.getBattle_speed_count() - 1);
				target.setBattle_speed(((target.getBase_speed() + target.getIv_values().get(5)) * 2 * (target.getLevel() + target.getBattle_speed_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s speed fell!");
			}
			if (move instanceof TargetSpeedMinusTwo)
			{
				target.setBattle_speed_count(target.getBattle_speed_count() - 2);
				target.setBattle_speed(((target.getBase_speed() + target.getIv_values().get(5)) * 2 * (target.getLevel() + target.getBattle_speed_count()) / 100 ) + 5);
				System.out.println(target.getName() + "'s speed fell harsly!");
			}
		}
		
		// Accuracy Stat Change
		if (move instanceof TargetAccuracyPlusOne || move instanceof TargetAccuracyPlusTwo || move instanceof TargetAccuracyMinusOne || move instanceof TargetAccuracyMinusTwo )
		{
			// Check min/max change been reached
			if (target.getBattle_accuracy_count() >= MAX_STAT_CHANGE)
			{
				System.out.println(target.getName() + "'s accuracy cannot go any higher!");
				return;
			}
			if (target.getBattle_accuracy_count() <= -MAX_STAT_CHANGE)
			{
				System.out.println(target.getName() + "'s speed accuracy cannot go any lower!");
				return;
			}
			
			if (move instanceof TargetAccuracyPlusOne)
			{
				target.setBattle_accuracy_count(target.getBattle_accuracy_count() + 1);
				target.setBattle_accuracy(target.getBattle_accuracy() + .1);
				System.out.println(target.getName() + "'s speed rose!");
			}
			if (move instanceof TargetAccuracyPlusTwo)
			{
				target.setBattle_accuracy_count(target.getBattle_accuracy_count() + 2);
				target.setBattle_accuracy(target.getBattle_accuracy() + .2);
				System.out.println(target.getName() + "'s speed rose sharply!");
			}
			if (move instanceof TargetAccuracyMinusOne)
			{
				target.setBattle_accuracy_count(target.getBattle_accuracy_count() - 1);
				target.setBattle_accuracy(target.getBattle_accuracy() - .1);
				System.out.println(target.getName() + "'s speed fell!");
			}
			if (move instanceof TargetAccuracyMinusTwo)
			{
				target.setBattle_accuracy_count(target.getBattle_accuracy_count() - 2);
				target.setBattle_accuracy(target.getBattle_accuracy() - .2);
				System.out.println(target.getName() + "'s speed fell harsly!");
			}
		}
		
		// Evasion Stat Change
		if (move instanceof TargetEvasionPlusOne || move instanceof TargetEvasionPlusTwo || move instanceof TargetEvasionMinusOne || move instanceof TargetEvasionMinusTwo )
		{
			// Check min/max change been reached
			if (target.getBattle_evasion_count() >= MAX_STAT_CHANGE)
			{
				System.out.println(target.getName() + "'s evasion cannot go any higher!");
				return;
			}
			if (target.getBattle_evasion_count() <= -MAX_STAT_CHANGE)
			{
				System.out.println(target.getName() + "'s evasion cannot go any lower!");
				return;
			}
			
			if (move instanceof TargetEvasionPlusOne)
			{
				target.setBattle_evasion_count(target.getBattle_evasion_count() + 1);
				target.setBattle_evasion(target.getBattle_evasion() + .1);
				System.out.println(target.getName() + "'s evasion rose!");
			}
			if (move instanceof TargetEvasionPlusTwo)
			{
				target.setBattle_evasion_count(target.getBattle_evasion_count() + 2);
				target.setBattle_evasion(target.getBattle_evasion() + .2);
				System.out.println(target.getName() + "'s evasion rose sharply!");
			}
			if (move instanceof TargetEvasionMinusOne)
			{
				target.setBattle_evasion_count(target.getBattle_evasion_count() - 1);
				target.setBattle_evasion(target.getBattle_evasion() - .1);
				System.out.println(target.getName() + "'s evasion fell!");
			}
			if (move instanceof TargetEvasionMinusTwo)
			{
				target.setBattle_evasion_count(target.getBattle_evasion_count() - 2);
				target.setBattle_evasion(target.getBattle_evasion() - .2);
				System.out.println(target.getName() + "'s evasion fell harsly!");
			}
		}
	}
	
	/* Calculates the type move bonus based on the type relationship between move type and target type(s)
	 * Input: 	target			- Target pokemon
	 * 			move			- Move
	 * Returns: void
	 */
	private static double getTypeMultiplier(Move move, Pokemon target) {
		/*
		if (console_info == true)
		{
			System.out.println(move_type + " vs " + target_type1 + " | " + target_type2);
		}
		*/
		double type_bonus = 1.0;
		
		// Normal
		if (move instanceof Normal)
		{
			if(target instanceof Rock || target instanceof Steel)
			{
				type_bonus *= .5;
			}
			// Special Condition
			if(target instanceof Ghost) type_bonus *= 0;
		}
		// Fire
		if (move instanceof Fire)
		{
			// Not effective
			if(target instanceof Fire || target instanceof Water || target instanceof Rock || target instanceof Dragon)
			{
				type_bonus *= .5;
			}
			// Super effective
			if(target instanceof Grass || target instanceof Ice || target instanceof Bug || target instanceof Steel)
			{
				type_bonus *= 2.0;
			}
		}
		// Water
		if (move instanceof Water)
		{
			// Not effective
			if(target instanceof Water || target instanceof Grass || target instanceof Dragon)
			{
				type_bonus *= .5;
			}
			// Super effective
			if(target instanceof Fire || target instanceof Ground || target instanceof Rock)
			{
				type_bonus *= 2.0;
			}
		}
		// Electric
		if (move instanceof Electric)
		{
			// Not effective
			if(target instanceof Electric || target instanceof Grass || target instanceof Dragon)
			{
				type_bonus *= .5;
			}
			// Super effective
			if(target instanceof Water || target instanceof Flying)
			{
				type_bonus *= 2.0;
			}
			// Special Condition
			if(target instanceof Ground) type_bonus *= 0;
		}
		// Grass
		if (move instanceof Grass)
		{
			// Not effective
			if(target instanceof Fire|| target instanceof Grass || target instanceof Poison || target instanceof Flying
					|| target instanceof Bug || target instanceof Dragon || target instanceof Steel)
			{
				type_bonus *= .5;
			}
			// Super effective
			if(target instanceof Water || target instanceof Ground || target instanceof Rock)
			{
				type_bonus *= 2.0;
			}
		}
		// Ice
		if (move instanceof Ice)
		{
			// Not effective
			if(target instanceof Fire|| target instanceof Water || target instanceof Ice || target instanceof Steel)
			{
				type_bonus *= .5;
			}
			// Super effective
			if(target instanceof Grass || target instanceof Ground || target instanceof Flying || target instanceof Dragon)
			{
				type_bonus *= 2.0;
			}
		}
		// Fighting
		if (move instanceof Fighting)
		{
			// Not effective
			if(target instanceof Poison || target instanceof Flying || target instanceof Psychic || target instanceof Bug)
			{
				type_bonus *= .5;
			}
			// Super effective
			if(target instanceof Normal || target instanceof Ice || target instanceof Rock || target instanceof Dark || target instanceof Steel)
			{
				type_bonus *= 2.0;
			}
			// Special Condition
			if(target instanceof Ghost) type_bonus *= 0;
		}
		// Poison
		if (move instanceof Poison)
		{
			// Not effective
			if(target instanceof Poison || target instanceof Ground || target instanceof Rock || target instanceof Ghost)
			{
				type_bonus *= .5;
			}
			// Super effective
			if(target instanceof Grass)
			{
				type_bonus *= 2.0;
			}
			// Special Condition
			if(target instanceof Steel) type_bonus *= 0;
		}
		// Ground
		if (move instanceof Ground)
		{
			// Not effective
			if(target instanceof Grass || target instanceof Bug)
			{
				type_bonus *= .5;
			}
			// Super effective
			if(target instanceof Fire || target instanceof Electric || target instanceof Poison || target instanceof Rock || target instanceof Steel) 
			{
				type_bonus *= 2.0;
			}
			// Special Condition
			if(target instanceof Flying) type_bonus *= 0;
		}
		// Flying
		if (move instanceof Flying)
		{
			// Not effective
			if(target instanceof Electric || target instanceof Rock)
			{
				type_bonus *= .5;
			}
			// Super effective
			if(target instanceof Grass || target instanceof Fighting || target instanceof Bug) 
			{
				type_bonus *= 2.0;
			}
		}
		// Psychic
		if (move instanceof Psychic)
		{
			// Not effective
			if(target instanceof Bug || target instanceof Steel)
			{
				type_bonus *= .5;
			}
			// Super effective
			if(target instanceof Fighting || target instanceof Poison) 
			{
				type_bonus *= 2.0;
			}
			// Special Condition
			if(target instanceof Dark) type_bonus *= 0;
		}
		// Bug
		if (move instanceof Bug)
		{
			// Not effective
			if(target instanceof Fire || target instanceof Fighting || target instanceof Poison || target instanceof Flying 
					|| target instanceof Ghost || target instanceof Steel)
			{
				type_bonus *= .5;
			}
			// Super effective
			if(target instanceof Psychic || target instanceof Grass || target instanceof Dark) 
			{
				type_bonus *= 2.0;
			}
		}
		// Rock
		if (move instanceof Rock)
		{
			// Not effective
			if(target instanceof Fighting || target instanceof Ground || target instanceof Steel)
			{
				type_bonus *= .5;
			}
			// Super effective
			if(target instanceof Fire || target instanceof Ice || target instanceof Flying || target instanceof Bug) 
			{
				type_bonus *= 2.0;
			}
		}
		// Ghost
		if (move instanceof Ghost)
		{
			// Not effective
			if(target instanceof Dark || target instanceof Steel)
			{
				type_bonus *= .5;
			}
			// Super effective
			if(target instanceof Psychic || target instanceof Ghost) 
			{
				type_bonus *= 2.0;
			}
			// Special Condition
			if(target instanceof Normal) type_bonus *= 0;
		}
		// Dragon
		if (move instanceof Dragon)
		{
			// Not effective
			if(target instanceof Steel)
			{
				type_bonus *= .5;
			}
			// Super effective
			if(target instanceof Dragon) 
			{
				type_bonus *= 2.0;
			}
		}
		// Dark
		if (move instanceof Dark)
		{
			// Not effective
			if(target instanceof Fighting || target instanceof Dark || target instanceof Steel)
			{
				type_bonus *= .5;
			}
			// Super effective
			if(target instanceof Ghost || target instanceof Psychic) 
			{
				type_bonus *= 2.0;
			}
		}
		// Steel
		if (move instanceof Steel)
		{
			// Not effective
			if(target instanceof Fire || target instanceof Water || target instanceof Electric || target instanceof Steel)
			{
				type_bonus *= .5;
			}
			// Super effective
			if(target instanceof Rock || target instanceof Ice) 
			{
				type_bonus *= 2.0;
			}
		}
		
		return type_bonus;
	} // End of Method
	
}
