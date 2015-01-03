package me.porterk.mg.mobs;

import me.porterk.mg.pfg.PathfinderGoalBreak;
import net.minecraft.server.v1_8_R1.EntityBat;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.World;

public class MortalBat extends EntityBat{

	public MortalBat(World arg0) {
		super(arg0);
		
		this.goalSelector.a(0, new PathfinderGoalBreak(this));

	}

}
