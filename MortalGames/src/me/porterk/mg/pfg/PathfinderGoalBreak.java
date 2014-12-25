package me.porterk.mg.pfg;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.MathHelper;
import net.minecraft.server.v1_8_R1.PathfinderGoal;

public class PathfinderGoalBreak extends PathfinderGoal{

	protected EntityInsentient a;
	protected Block block;
	protected Block block2;
	protected Block block3;

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



				if(!block.getType().equals(Material.AIR)){


					if(a.getGoalTarget() instanceof EntityPlayer){

						EntityPlayer tar = (EntityPlayer) a.getGoalTarget();

						if(tar.getBukkitEntity().getLocation().getY() == a.locY){

							world.getBlockAt(x, y -1, z + 1);

						}


						world.getBlockAt(x, y, z +1).breakNaturally();

						a.world.broadcastEntityEffect(a, (byte) 0);
					}

				}

				break;
			}
			case 1: //Direction 1 = -X
			{

				Location l = new Location(world, x -1, y, z);

				block = (Block) world.getBlockAt(l);



				if(!block.getType().equals(Material.AIR)){



					if(a.getGoalTarget() instanceof EntityPlayer){



						world.getBlockAt(x -1, y, z).breakNaturally();

						a.world.broadcastEntityEffect(a, (byte) 0);

					}

				}
				break;
			}
			case 2: //Direction 2 = -Z
			{

				Location l = new Location(world, x, y, z -1);

				block = (Block) world.getBlockAt(l);



				if(!block.getType().equals(Material.AIR)){



					if(a.getGoalTarget() instanceof EntityPlayer){



						world.getBlockAt(x, y, z -1).breakNaturally();

						a.world.broadcastEntityEffect(a, (byte) 0);

					}

				}
				break;
			}
			case 3: //Direction 3 = +X
			{


				Location l = new Location(world, x + 1, y, z);

				block = (Block) world.getBlockAt(l);

				if(!block.getType().equals(Material.AIR)){

					if(a.getGoalTarget() instanceof EntityPlayer){



						world.getBlockAt(x +1, y, z).breakNaturally();

						a.world.broadcastEntityEffect(a, (byte) 0);

					}

				}
				break;
			}

			}


		}
	}

}
