package me.porterk.mg.gametype;

import me.porterk.mg.mobs.MortalBat;
import me.porterk.mg.mobs.MortalSkeleton;
import me.porterk.mg.mobs.MortalSpider;
import me.porterk.mg.mobs.MortalZombie;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class Classic extends MortalGameType{

	@SuppressWarnings("deprecation")
	public void runGame(){
		
		for(Player tar : main.getServer().getOnlinePlayers()){
		
		api.mobAmount = api.waveNumber * 2;
			
			tar.getWorld().setTime(15000);
			
		if(!api.spectating.contains(tar.getName())){

			int zombie;

			zombie = api.random(api.waveNumber, api.mobAmount);
			api.mobAmount -= zombie;

			while(zombie > 0){
				Location mobSpawn = new Location(tar.getWorld(), api.random(tar.getLocation().getBlockX() - 20, tar.getLocation().getBlockX() + 13) + 1, 0, api.random(tar.getLocation().getBlockZ() - 20, tar.getLocation().getBlockZ() + 13) + 1);

				mobSpawn.setY(mobSpawn.getWorld().getHighestBlockYAt(mobSpawn));

				net.minecraft.server.v1_8_R1.World world = ((CraftWorld) tar.getWorld()).getHandle();

				MortalZombie z = new MortalZombie(world);

				z.setPosition(mobSpawn.getX(), mobSpawn.getY(), mobSpawn.getZ());
				world.addEntity(z, SpawnReason.CUSTOM);
				zombie--;
				
			}
			
			
			if(api.waveNumber >= 3){
				
				int skeleton;
				
				skeleton = api.random((api.waveNumber / 2), api.waveNumber);
				
				while(skeleton > 0){
					
					
					Location mobSpawn = new Location(tar.getWorld(), api.random(tar.getLocation().getBlockX() - 20, tar.getLocation().getBlockX() + 13) + 1, 0, api.random(tar.getLocation().getBlockZ() - 20, tar.getLocation().getBlockZ() + 13) + 1);

					mobSpawn.setY(mobSpawn.getWorld().getHighestBlockYAt(mobSpawn));

					net.minecraft.server.v1_8_R1.World world = ((CraftWorld) tar.getWorld()).getHandle();
					
					MortalSkeleton s = new MortalSkeleton(world);
					
					s.setPosition(mobSpawn.getX(),mobSpawn.getY(), mobSpawn.getZ());
					world.addEntity(s, SpawnReason.CUSTOM);
					
					ItemStack bow = new ItemStack(Material.BOW);
						
						bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
						
						Skeleton sk = (Skeleton) s.getBukkitEntity();
						
						EntityEquipment se = (EntityEquipment) sk.getEquipment();
						
						se.setItemInHand(bow);
						
						skeleton--;
				}
				
			
			}
			
			if(api.waveNumber >= 4){
				int bat;
				
				bat = api.random(1, 3);
				
				while (bat > 0){
					
					Location mobSpawn = new Location(tar.getWorld(), api.random(tar.getLocation().getBlockX() - 20, tar.getLocation().getBlockX() + 13) + 1, 0, api.random(tar.getLocation().getBlockZ() - 20, tar.getLocation().getBlockZ() + 13) + 1);

					mobSpawn.setY(mobSpawn.getWorld().getHighestBlockYAt(mobSpawn));

					net.minecraft.server.v1_8_R1.World world = ((CraftWorld) tar.getWorld()).getHandle();
					
					MortalBat b = new MortalBat(world);
					
					b.setTarget(tar);
					
					b.setPosition(mobSpawn.getX(), mobSpawn.getY(), mobSpawn.getZ());
					
					world.addEntity(b, SpawnReason.CUSTOM);
					
					while(!b.getBukkitEntity().isDead()){
					
					b.getBukkitEntity().getLocation().getWorld().playEffect(b.getBukkitEntity().getLocation(), Effect.MOBSPAWNER_FLAMES, 3);
					
					}
					
					api.bat(b, tar);

					bat--;
				}
			}
			
			if(api.waveNumber >= 5){
	
				int spider;
				
				spider = api.random(api.waveNumber / 5, api.waveNumber / 2);
				
				while(spider > 0){
					
					
					Location mobSpawn = new Location(tar.getWorld(), api.random(tar.getLocation().getBlockX() - 20, tar.getLocation().getBlockX() + 13) + 1, 0, api.random(tar.getLocation().getBlockZ() - 20, tar.getLocation().getBlockZ() + 13) + 1);

					mobSpawn.setY(mobSpawn.getWorld().getHighestBlockYAt(mobSpawn));

					net.minecraft.server.v1_8_R1.World world = ((CraftWorld) tar.getWorld()).getHandle();
					
					MortalSpider s = new MortalSpider(world);
					
					s.setPosition(mobSpawn.getX(), mobSpawn.getY(), mobSpawn.getZ());
					
					world.addEntity(s, SpawnReason.CUSTOM);
					
					spider--;
				}
				
			}
		}
	}
		
	}
	
}
