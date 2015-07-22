package me.porterk.mg.powerups;

import org.bukkit.entity.Player;

public class DoubleEmeralds extends Powerup
{
    public void start(Player p)
    {
        name = Powers.DOUBLE_EMERALDS.getName();
        time = 60;

        initiate(p);
    }
}