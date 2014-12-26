package me.porterk.mg.mobs;

import net.minecraft.server.v1_8_R1.EntitySpider;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.World;

public class MortalSpider extends EntitySpider{

	public MortalSpider(World world) {
		super(world);
		
		try {
			this.getAttributeInstance(GenericAttributes.b).setValue(80);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
	}
	
}