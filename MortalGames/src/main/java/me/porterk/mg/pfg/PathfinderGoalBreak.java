package me.porterk.mg.pfg;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R1.CraftSound;
import org.bukkit.craftbukkit.v1_8_R1.block.CraftBlock;

import me.porterk.mg.mobs.MortalZombie;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.MathHelper;
import net.minecraft.server.v1_8_R1.PathfinderGoal;

public class PathfinderGoalBreak extends PathfinderGoal{

	protected EntityInsentient a;
	protected Block block;
	protected Block block2;
	protected Block block3;
	
	public void breakBlock(Block b) throws Throwable {
        b.breakNaturally();
        for(Sound sound : Sound.values()) {
            Field f = CraftSound.class.getDeclaredField("sounds");
            f.setAccessible(true);
           
            String[] sounds = (String[]) f.get(null);
            Method getBlock = CraftBlock.class.getDeclaredMethod("getNMSBlock");
            getBlock.setAccessible(true);
            Object nmsBlock = getBlock.invoke(b);
            net.minecraft.server.v1_8_R1.Block block = (net.minecraft.server.v1_8_R1.Block) nmsBlock;
 
            if(block.stepSound.getBreakSound()
                    .equals(sounds[sound.ordinal()])) {
                b.getWorld().playSound(b.getLocation(), sound,10, 10);
            }
        }
    }

	public PathfinderGoalBreak(EntityInsentient entityinsentient) {
		this.a = entityinsentient;
	}

	@Override
	public boolean a() {

		return true;
	}

	@Override
	public void e(){
		int direction = MathHelper.floor((double)((a.yaw * 4F) / 360F) + 0.5D) & 3;
		int x = (int) a.locX;
		int y = MathHelper.floor((int) a.locY + 1);
		int z = (int) a.locZ;


		if(a.getGoalTarget() != null){

			a.getGoalTarget().getBukkitEntity().getLocation();

			org.bukkit.World world = a.world.getWorld();

			switch(direction)
			{
			case 0: //Direction 0 = +Z
			{

				Location l = new Location(world, x, y, z +1);

				block = (Block) world.getBlockAt(l);



				if((!block.getType().equals(Material.AIR)) && 
						(!block.getType().equals(Material.WATER))&& 
						(!block.getType().equals(Material.LAVA))){


					if(a.getGoalTarget() instanceof EntityPlayer){
						try {
							breakBlock(block);
						} catch (Throwable e) {
							e.printStackTrace();
						}

						a.world.broadcastEntityEffect(a, (byte) 16);
					}

				}

				break;
			}
			case 1: //Direction 1 = -X
			{

				Location l = new Location(world, x -1, y, z);

				block = (Block) world.getBlockAt(l);



				if((!block.getType().equals(Material.AIR)) && 
						(!block.getType().equals(Material.WATER))&& 
						(!block.getType().equals(Material.LAVA))){


					if(a.getGoalTarget() instanceof EntityPlayer){
						try {
							breakBlock(block);
						} catch (Throwable e) {
							e.printStackTrace();
						}

						a.world.broadcastEntityEffect(a, (byte) 16);
					}

				}
				break;
			}
			case 2: //Direction 2 = -Z
			{

				Location l = new Location(world, x, y, z -1);

				block = (Block) world.getBlockAt(l);



				if((!block.getType().equals(Material.AIR)) && 
						(!block.getType().equals(Material.WATER))&& 
						(!block.getType().equals(Material.LAVA))){


					if(a.getGoalTarget() instanceof EntityPlayer){
						try {
							breakBlock(block);
						} catch (Throwable e) {
							e.printStackTrace();
						}

						a.world.broadcastEntityEffect(a, (byte) 16);
					}

				}
				break;
			}
			case 3: //Direction 3 = +X
			{


				Location l = new Location(world, x + 1, y, z);

				block = (Block) world.getBlockAt(l);

				if((!block.getType().equals(Material.AIR)) && 
						(!block.getType().equals(Material.WATER))&& 
						(!block.getType().equals(Material.LAVA))){


					if(a.getGoalTarget() instanceof EntityPlayer){
						try {
							breakBlock(block);
						} catch (Throwable e) {
							e.printStackTrace();
						}

						MortalZombie.breakAnim(a);
					}

				}
				break;
			}

			}


		}
	}

}
