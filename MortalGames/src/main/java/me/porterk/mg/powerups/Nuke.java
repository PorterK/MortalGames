package me.porterk.mg.powerups;

import java.lang.reflect.Constructor;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Nuke extends Powerup{
	
	int mobs;
	
	/*Powerups that are a single use only (like Nuke) do not need a time or the initiate variable*/
	public void start(Player p){
		
		name = Powers.NUKE.getName();
		
		api.broadcastMessage(ChatColor.GREEN + p.getDisplayName() + ChatColor.DARK_RED+ " has used" + 
		ChatColor.GREEN + " Nuke" + ChatColor.DARK_RED + "!");
		
		for(Entity e : p.getWorld().getEntities()){
			
			if(!(e instanceof Player)){
				
				if(e.isValid()){
					
					e.remove();
					e.getWorld().playEffect(e.getLocation(), Effect.EXPLOSION_HUGE, 0);
					e.getWorld().playSound(e.getLocation(), Sound.EXPLODE, 10, 1);
					mobs++;
					
				}
				
			}
			
		}
		
		p.sendMessage(main.tag + ChatColor.DARK_RED + "You killed " + ChatColor.GREEN + 
				mobs + ChatColor.DARK_RED + " mobs!");

	}
	
}
