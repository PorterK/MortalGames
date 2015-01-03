package me.porterk.mg.pfg;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.PathfinderGoal;

public class PathfinderGoalBatExplode extends PathfinderGoal{

	protected EntityInsentient a;
	
	public void PathfinderGoalBreak(EntityInsentient entityinsentient) {
		this.a = entityinsentient;
	}
	
	@Override
	public boolean a() {
		
		return true;
	}

	@Override
	public void c(){
		
		if(a.getGoalTarget() != null){
			
			Location b = a.getGoalTarget().getBukkitEntity().getLocation();
			Location c = a.getBukkitEntity().getLocation();
			
			if(a.getGoalTarget() instanceof Player){
				
				if((c.getBlockY() <= b.getBlockY() + 3) ||
						c.getBlockY() <= c.getBlockY() - 3){
				
					if((b.getBlockX() <= c.getBlockX() - 3) ||
						(b.getBlockZ() <= c.getBlockZ() - 3) ||
						(b.getBlockX() <= c.getBlockX() + 3) ||
						(b.getBlockZ() <= c.getBlockZ() + 3)){
					
						c.getWorld().createExplosion(c, 3);
					
					}
				}
				
			}
			
		}
		
	}
}
