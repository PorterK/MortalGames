package me.porterk.mg.mobs;

import net.minecraft.server.v1_8_R1.EntitySkeleton;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.World;

public class MortalSkeleton extends EntitySkeleton{

	public MortalSkeleton(World world) {
		super(world);
		
		try {
			this.getAttributeInstance(GenericAttributes.b).setValue(160);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	
		}
	

}
