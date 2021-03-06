package me.porterk.mg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.porterk.mg.powerups.DoubleEmeralds;
import me.porterk.mg.powerups.DoubleExp;
import me.porterk.mg.powerups.UltraDamage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

//TPHelix Stuff
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class MortalListener implements Listener
{
    List<String> team;

    public void scoreboard(Player p)
    {
        p.setScoreboard(Main.s);
    }

    MortalAPI api = new MortalAPI();

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent e)
    {
        e.setJoinMessage(null);

        Player p = e.getPlayer();

        api.registerPlayer(p);

        int playerCount = p.getServer().getOnlinePlayers().size();

        Main.chat.put(p.getName(), "general");

        if (playerCount < 10)
        {
            p.sendMessage(ChatColor.GOLD + "Welcome, " + ChatColor.GREEN + p.getDisplayName() + ChatColor.GOLD + " to " + ChatColor.DARK_RED + "The Mortal Games");
            p.sendMessage(ChatColor.BLUE + "Made by " + ChatColor.RED + " PorterK & Joannou1"); //#SorryIWasTryingToStealAllTheCredit :P
            p.sendMessage(ChatColor.GOLD + "There are currently " + ChatColor.DARK_RED + playerCount + "/10" + ChatColor.GOLD + " players online!");
            scoreboard(p);

            if (api.isGameOn)
            {
                int number = 0;

                while (!team.isEmpty())
                {
                    number++;

                    String t = api.team.get(team.get(number));

                    if (t != null)
                    {
                        api.team.put(p, team.get(number));
                        team.remove(number);
                    }
                }
            }
        } else if (playerCount >= 10)
        {
            p.sendMessage(ChatColor.GOLD + "Welcome to " + ChatColor.DARK_RED + "The Mortal Games!");
            p.sendMessage(ChatColor.BLUE + "Visit our website at" + ChatColor.RED + " http://www.gld.bz");
            p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.ITALIC + "Currently the game is full.");
            api.setSpectating(p);
            scoreboard(p);
        }

        if (p.isOp())
        {
            p.sendMessage(ChatColor.AQUA + "Type" + ChatColor.DARK_RED + " /mg admin" + ChatColor.AQUA + " to get to admin chat!");
        }

        if (api.isMod(p))
        {
            p.sendMessage(ChatColor.AQUA + "Type /mg moderate to join as a moderator, or continue playing as normal!");
        }

        if (playerCount == 10)
        {
            api.startGame();
        }

        if (api.isGameOn())
        {
            api.setSpectating(p);
        }

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e)
    {
        String name = e.getPlayer().getName();
        e.setQuitMessage(Main.tag + name + " chickened out!");

        api.spectating.remove(name);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        e.setCancelled(true);

        Player sender = e.getPlayer();

        for (Player online : Main.getInstance().getServer().getOnlinePlayers())
        {
            if (Main.chat.containsKey(sender.getName()))
            {
                if (Main.chat.get(sender.getName()).equals("general"))
                {
                    if (Main.chat.get(online.getName()).equals("general") || online.isOp())
                    {
                        online.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "G" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " " + sender.getDisplayName() + ChatColor.WHITE + ": " + e.getMessage());
                    }
                }

                if (Main.chat.get(sender.getName()).equals("green"))
                {

                    if (Main.chat.get(online.getName()).equals("green") || online.isOp())
                    {
                        online.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Gr" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " " + sender.getDisplayName() + ChatColor.WHITE + ": " + e.getMessage());
                    }
                }

                if (Main.chat.get(sender.getName()).equals("orange"))
                {
                    if (Main.chat.get(online.getName()).equals("orange") || online.isOp())
                    {
                        online.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "O" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " " + sender.getDisplayName() + ChatColor.WHITE + ": " + e.getMessage());
                    }
                }

                if (Main.chat.get(sender.getName()).equals("admin"))
                {
                    if (Main.chat.get(online.getName()).equals("admin") || online.isOp())
                    {
                        online.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "A" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " " + sender.getDisplayName() + ChatColor.WHITE + ": " + ChatColor.GRAY + "" + ChatColor.ITALIC + "" + e.getMessage());
                    }
                }

                if (Main.chat.get(sender.getName()).equals("spectate"))
                {
                    if (Main.chat.get(online.getName()).equals("spectate") || online.isOp())
                    {

                        online.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "Spec" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " " + sender.getDisplayName() + ChatColor.WHITE + ": " + e.getMessage());
                    }
                }

                if (Main.chat.get(sender.getName()).equals("mod"))
                {
                    if (Main.chat.get(online.getName()).equals("mod") || online.isOp())
                    {

                        online.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "Mod" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " " + sender.getDisplayName() + ChatColor.WHITE + ": " + e.getMessage());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e)
    {
        if (!e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {

        e.setDeathMessage(null);

        Player a = e.getEntity();

        a.setHealth(20);

        api.setSpectating(a);

        if (a.getKiller() instanceof Player)
        {
            e.setDeathMessage(Main.tag + ChatColor.DARK_RED + "Player " + ChatColor.GOLD + a.getName() + ChatColor.DARK_RED + " has fallen to " + ChatColor.GOLD + a.getKiller().getName());

            Player b = a.getKiller();

            b.setHealth(0);

            b.sendMessage(Main.tag + ChatColor.DARK_RED + "Please don't team kill :)");
        } else
        {
            List<String> deathMessage = new ArrayList<String>();

            String d1 = Main.tag + ChatColor.DARK_RED + "Player " + ChatColor.GOLD + a.getName() + ChatColor.DARK_RED + " couldn't handle the truth!";
            String d2 = Main.tag + ChatColor.DARK_RED + "Player " + ChatColor.GOLD + a.getName() + ChatColor.DARK_RED + " can't stand the rush!";
            String d3 = Main.tag + ChatColor.DARK_RED + "Player " + ChatColor.GOLD + a.getName() + ChatColor.DARK_RED + " got rek'd m8.";
            String d4 = Main.tag + ChatColor.DARK_RED + "Player " + ChatColor.GOLD + a.getName() + ChatColor.DARK_RED + " was terminated.";
            String d5 = Main.tag + ChatColor.DARK_RED + "Player " + ChatColor.GOLD + a.getName() + ChatColor.DARK_RED + " met their maker";
            String d6 = Main.tag + ChatColor.DARK_RED + "Player " + ChatColor.GOLD + a.getName() + ChatColor.DARK_RED + " dropped their sword.";
            String d7 = Main.tag + ChatColor.DARK_RED + "Player " + ChatColor.GOLD + a.getName() + ChatColor.DARK_RED + " had a heart attack, but 911 hung up.";

            deathMessage.add(d1);
            deathMessage.add(d2);
            deathMessage.add(d3);
            deathMessage.add(d4);
            deathMessage.add(d5);
            deathMessage.add(d6);
            deathMessage.add(d7);

            Collections.shuffle(deathMessage);

            e.setDeathMessage(deathMessage.get(1));
        }

        List<Player> players = new ArrayList<Player>();
        for (Player player : Bukkit.getOnlinePlayers())
        {
            players.add(player);
        }

        if (players.size() - api.spectating.size() == 1)
        {
            api.gameOver();
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e)
    {
        if (e.getDamager() instanceof Player)
        {
            Player p = (Player) e.getDamager();
            Player a = (Player) e.getDamager();
            if (e.getEntity() instanceof Player)
            {
                if (!api.allowPVP())
                {
                    e.setCancelled(true);
                    p.sendMessage(Main.tag + ChatColor.DARK_RED + "Squad vs squad PVP is not enabled on this version.");
                } else
                {
                    if (api.getTeam(p) == api.getTeam(a))
                    {
                        p.sendMessage(Main.tag + ChatColor.DARK_RED + "Attacking your teammates is discouraged! ");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent e)
    {
        int maxEm = 1;

        if (DoubleEmeralds.isEnabled())
        {
            maxEm = 2;
        }

        if (e.getEntity().getKiller() instanceof Player)
        {
            if (e.getEntity().getType().equals(EntityType.ZOMBIE))
            {
                e.getDrops().clear();

                e.getDrops().add(new ItemStack(Material.BREAD, 1));

                if (api.waveNumber <= 2)
                    e.getDrops().add(new ItemStack((Material.GOLDEN_APPLE), api.random(0, 2), (short) 1));

                if (DoubleEmeralds.isEnabled())
                    e.getDrops().add(new ItemStack(Material.EMERALD, api.random(0, 1)));

                if (DoubleExp.isEnabled())
                    e.setDroppedExp(e.getDroppedExp() * 3);
            }

            if (e.getEntity().getType().equals(EntityType.SKELETON))
            {
                e.getDrops().clear();

                e.getDrops().add(new ItemStack(Material.EMERALD, api.random(0, maxEm)));

                if (DoubleExp.isEnabled())
                    e.setDroppedExp(e.getDroppedExp() * 3);
            }

            if (e.getEntity().getType().equals(EntityType.SPIDER))
            {
                e.getDrops().clear();

                e.getDrops().add(new ItemStack(Material.EMERALD, api.random(0, maxEm)));
                /* The perk is called double exp, but the exp is really trippled to notice the difference! */
                if (DoubleExp.isEnabled())
                    e.setDroppedExp(e.getDroppedExp() * 3);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e)
    {
        Player p = e.getPlayer();

        if (!p.isOp())
        {
            if (!api.canBuild())
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e)
    {
        Entity a = e.getEntity();

        if (a instanceof Arrow)
        {
            if (!(((Arrow) a).getShooter() instanceof Player))
            {
                Location l = a.getLocation();

                l.getWorld().createExplosion(l, 2);

                a.remove();
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        if (event.getCause() == TeleportCause.COMMAND || event.getCause() == TeleportCause.PLUGIN || event.getCause() == TeleportCause.ENDER_PEARL)
        {
            Main.createHelixAsync(event.getPlayer());
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e)
    {
        if (e.getDamager() instanceof Player)
        {
            if (!(e.getEntity() instanceof Player))
            {
                if (UltraDamage.isEnabled())
                    e.setDamage(100);
            }
        }
    }
}
