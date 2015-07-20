package me.porterk.mg.powerups;

import org.bukkit.entity.Player;

public class UltraDamage extends Powerup{
	
	public void start(Player p){
		
		name = Powers.ULTRA_DAMAGE.getName();
		time = 60;
		
		initiate(p);
		
	}
	
}
