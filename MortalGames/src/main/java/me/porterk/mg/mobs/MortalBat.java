package me.porterk.mg.mobs;

import net.minecraft.server.v1_8_R3.EntityBat;
import net.minecraft.server.v1_8_R3.World;

import org.bukkit.entity.Player;

public class MortalBat extends EntityBat
{
    static Player pl;

    public MortalBat(World arg0)
    {
        super(arg0);
    }

    public void setTarget(Player p)
    {
        pl = p;
    }

    public static Player target()
    {
        return pl;
    }
}