package me.porterk.mg.powerups;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.porterk.mg.Main;
import me.porterk.mg.MortalAPI;

public class Powerup
{
    public static int     time;
    public static int     timer;
    public static boolean enabled;
    public static String  name;

    MortalAPI             api  = new MortalAPI();
    Main                  main = Main.getInstance();

    public void initiate(Player p)
    {
        enabledMessage(p, name);

        enabled = true;

        timer = Main.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable()
        {
            public void run()
            {
                time--;

                switch (time)
                {
                    case 0:

                        disabledMessage(name);

                        whileOn();
                }
            }
        }, 0L, 20L);
    }

    protected void whileOn()
    {

    }

    public static Boolean isEnabled()
    {
        return enabled;
    }

    public void enabledMessage(Player p, String perk)
    {
        api.broadcastMessage(ChatColor.DARK_RED + "The " + ChatColor.GREEN + perk + ChatColor.DARK_RED + " perk has been enabled by " + ChatColor.GREEN + p.getDisplayName() + ChatColor.DARK_RED + "!");
    }

    public void disabledMessage(String perk)
    {
        api.broadcastMessage(ChatColor.DARK_RED + "The " + ChatColor.GREEN + perk + ChatColor.DARK_RED + " has been disabled!");
    }
}