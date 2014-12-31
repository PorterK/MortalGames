package me.porterk.mg.mobs;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_8_R1.util.UnsafeList;

import me.porterk.mg.pfg.PathfinderGoalBreak;
import net.minecraft.server.v1_8_R1.*;

public class MortalZombie extends EntityZombie {

	public MortalZombie(World world) {
		super(world);

		try {
			Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
			bField.setAccessible(true);
			Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);
			bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());

			this.getAttributeInstance(GenericAttributes.b).setValue(160);
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(1, new PathfinderGoalBreak(this));
		this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this,
				EntityHuman.class, 1.0D, false));
		this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this,
				1.0D));
		this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D,
				false));
		this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this,
				EntityHuman.class, 15.0F));
		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(
				this, EntityHuman.class, 0, true, canPickUpLoot, null));
	}

	protected void aD() {
		super.ad();
		this.getAttributeInstance(GenericAttributes.e).setValue(4.0D); // Original
																		// 3.0D
	}

}
