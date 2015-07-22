package me.porterk.mg.mobs;

import net.minecraft.server.v1_8_R3.EntitySkeleton;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.World;

public class MortalSkeleton extends EntitySkeleton
{
    public MortalSkeleton(World world)
    {
        super(world);

        try
        {
            this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(160);
        } catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }
}