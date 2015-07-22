package me.porterk.mg.powerups;

import org.bukkit.entity.Player;

public class DoubleExp extends Powerup
{
    public void start(Player p)
    {
        name = Powers.DOUBLE_EXP.getName();
        time = 60;

        initiate(p);
    }
}