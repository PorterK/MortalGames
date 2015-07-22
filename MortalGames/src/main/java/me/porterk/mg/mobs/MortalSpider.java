package me.porterk.mg.mobs;

import net.minecraft.server.v1_8_R3.EntityCaveSpider;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.World;

public class MortalSpider extends EntityCaveSpider
{
    public MortalSpider(World world)
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