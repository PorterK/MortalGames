package me.porterk.mg.commands;

import me.porterk.mg.Main;
import me.porterk.mg.mobs.MortalBat;
import me.porterk.mg.powerups.Powers;
import net.minecraft.server.v1_8_R3.World;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class Commands implements CommandExecutor
{
    public Commands plugin;

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        Player p = (Player) sender;
        String allargs = "";

        for (int i = 0; i < args.length; i++)
        {
            String arg = args[i] + " ";
            allargs = allargs + arg;
        }

        if (cmd.getName().equalsIgnoreCase("mg") || cmd.getName().equalsIgnoreCase("mortal"))
        {
            if (args.length == 0)
            {
                sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Mortal Games");
                sender.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Coded by PorterK & Joannou1");
                sender.sendMessage(ChatColor.GRAY + "To view available commands type: /mg help or /mg ?");
            } else if (args.length >= 1)
            {
                if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))
                {
                    sender.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "Here are all of the commands: ");
                    sender.sendMessage(ChatColor.GRAY + "/mg vote - Vote to start the round early");
                    sender.sendMessage(ChatColor.GRAY + "/mg money - View dat cash money");
                    sender.sendMessage(ChatColor.GRAY + "/mg stats - View your stats");
                    sender.sendMessage(ChatColor.GRAY + "/mg time - View information on times");

                    if (sender.hasPermission("mortal.admin"))
                    {
                        sender.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "Admin Commands: ");
                        sender.sendMessage(ChatColor.GRAY + "/mg admin - Toggle admin chat");
                        sender.sendMessage(ChatColor.GRAY + "/command - Description");
                        sender.sendMessage(ChatColor.GRAY + "/command - Description");
                    }

                    if (sender.hasPermission("mortal.dev"))
                    {
                        sender.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "Dev Commands: ");
                        sender.sendMessage(ChatColor.GRAY + "/mg dev start [game | waitwave (skipcount <number>)] - Starts the game in many ways.");
                        sender.sendMessage(ChatColor.GRAY + "/mg dev shop - Open the shop");
                        sender.sendMessage(ChatColor.GRAY + "/mg dev spawn [bat | etc] - Spawns some things.");
                        sender.sendMessage(ChatColor.GRAY + "/mg dev powertest - Test the nuke power.");
                    }
                } else if (args[0].equalsIgnoreCase("secret"))
                {
                    sender.sendMessage(ChatColor.GRAY + "Did you know... That there are secrets?!");
                } else if (args[0].equalsIgnoreCase("vote"))
                {
                    sender.sendMessage(Main.tag + ChatColor.GREEN + "You have voted to start the game early! Thank you!");
                    Main.api.vote(p);

                } else if (args[0].equals("money"))
                {
                    p.sendMessage(Main.tag + ChatColor.GOLD + "Your balance is " + ChatColor.RED + "" + Main.api.getCash(p));
                } else if (args[0].equals("stats"))
                {
                    p.sendMessage(ChatColor.GOLD + "~ Stats for " + p.getName() + " ~");
                    p.sendMessage("");
                    p.sendMessage(ChatColor.GREEN + "Kills: " + ChatColor.RED + Main.api.getKills(p));
                    p.sendMessage(ChatColor.GREEN + "Deaths: " + ChatColor.RED + Main.api.getDeaths(p));
                    p.sendMessage(ChatColor.GREEN + "KDR: " + ChatColor.RED + Main.api.getKDR(p));
                    p.sendMessage(ChatColor.GREEN + "Matches Played: " + ChatColor.RED + Main.api.gamesPlayed(p));
                    p.sendMessage(ChatColor.GREEN + "Rank: " + ChatColor.RED + Main.api.rank(p));
                } else if (args[0].equalsIgnoreCase("time"))
                {
                    sender.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "<Time information about current/future wave>");
                } else

                // //// Admin Commands //// //

                // Admin Chat
                if (args[0].equalsIgnoreCase("admin"))
                {
                    if (sender.hasPermission("mortal.admin"))
                    {
                        if (Main.chat.containsKey(p.getName()))
                        {
                            if (!Main.chat.get(p.getName()).equals("admin"))
                            {
                                if (Main.chat.containsKey(p.getName()))
                                {
                                    Main.lastchat.put(p.getName(), Main.chat.get(p.getName()));
                                } else
                                {
                                    Main.lastchat.put(p.getName(), "general");
                                }

                                p.sendMessage(Main.tag + ChatColor.DARK_RED + "Admin " + ChatColor.WHITE + "chat has been " + ChatColor.GREEN + "enabled" + ChatColor.WHITE + ".");

                                Main.chat.put(p.getName(), "admin");
                            } else
                            {
                                p.sendMessage(Main.tag + ChatColor.DARK_RED + "Admin " + ChatColor.WHITE + "chat has been " + ChatColor.RED + "disabled" + ChatColor.WHITE + ".");

                                if (Main.lastchat.containsKey(p.getName()))
                                {
                                    Main.chat.put(p.getName(), Main.lastchat.get(p.getName()));
                                } else
                                {
                                    Main.chat.put(p.getName(), "general");
                                }
                            }
                        } else
                        {
                            Main.chat.put(p.getName(), "admin");
                            p.sendMessage(Main.tag + ChatColor.DARK_RED + "Admin " + ChatColor.WHITE + "chat has been " + ChatColor.GREEN + "enabled" + ChatColor.WHITE + ".");
                            p.sendMessage("this one");
                        }

                        return true;
                    }
                } else

                // //// Dev Commands //// //

                if (args[0].equalsIgnoreCase("dev"))
                {
                    if (p.hasPermission("mortal.dev"))
                    {
                        if (args.length >= 1)
                        {
                            if (args.length >= 2)
                            {
                                if (args[1].equalsIgnoreCase("start"))
                                {
                                    if (args.length >= 3)
                                    {
                                        if (args[2].equalsIgnoreCase("game"))
                                        {
                                            sender.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "Dev: Starting game.");
                                            Main.api.startGame();

                                            return true;
                                        } else if (args[2].equalsIgnoreCase("waitwave"))
                                        {
                                            if (args.length >= 4)
                                            {
                                                if (args[3].equalsIgnoreCase("skipcount"))
                                                {
                                                    if (args.length >= 5)
                                                    {
                                                        if (!args[4].equals(null))
                                                        {
                                                            try
                                                            {
                                                                Main.api.waveNumber = Integer.parseInt(args[4]);
                                                            } catch (Exception e)
                                                            {
                                                                sender.sendMessage(Main.tag + ChatColor.GOLD + "You need to enter a wave " + ChatColor.BOLD + "NUMBER.");
                                                            }
                                                        } else
                                                        {
                                                            Main.api.waveNumber += 1;
                                                        }

                                                        sender.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "Dev: Starting wave.");
                                                        Main.api.startWave();
                                                    } else
                                                    {
                                                        sender.sendMessage(Main.tag + ChatColor.GOLD + "You need to enter a wave " + ChatColor.BOLD + "NUMBER.");
                                                    }
                                                } else
                                                {
                                                    sender.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "Dev: Starting pre-wave.");
                                                    Main.api.preWave();
                                                }
                                            } else
                                            {
                                                sender.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "Dev: Starting pre-wave.");
                                                Main.api.preWave();
                                            }

                                            return true;
                                        } else
                                        {
                                            sender.sendMessage(Main.tag + ChatColor.RED + "Unknown subcommand. Valid commands:");
                                            sender.sendMessage(Main.tag + ChatColor.GRAY + "game | waitwave (skipcount <waves>)");
                                        }
                                    } else
                                    {
                                        sender.sendMessage(Main.tag + ChatColor.RED + "Please enter a subcommand. Valid commands:");
                                        sender.sendMessage(Main.tag + ChatColor.GRAY + "game | waitwave");
                                    }
                                } else if (args[1].equalsIgnoreCase("shop"))
                                {
                                    Main.api.openShop(p);
                                } else if (args[1].equalsIgnoreCase("spawn"))
                                {
                                    if (args.length >= 3)
                                    {
                                        if (args[2].equalsIgnoreCase("bat"))
                                        {
                                            Location mobSpawn = new Location(p.getWorld(), Main.api.random(p.getLocation().getBlockX() - 20, p.getLocation().getBlockX() + 13) + 1, 0, Main.api.random(p.getLocation().getBlockZ() - 20, p.getLocation().getBlockZ() + 13) + 1);

                                            mobSpawn.setY(mobSpawn.getWorld().getHighestBlockYAt(mobSpawn));

                                            World world = ((CraftWorld) p.getWorld()).getHandle();

                                            MortalBat b = new MortalBat(world);

                                            b.setTarget(p);

                                            b.setPosition(mobSpawn.getX(), mobSpawn.getY(), mobSpawn.getZ());

                                            world.addEntity(b, SpawnReason.CUSTOM);

                                            Main.api.bat(b, p);
                                        } else
                                        {
                                            sender.sendMessage(Main.tag + ChatColor.RED + "Unknown subcommand. Valid subcommands:");
                                            sender.sendMessage(Main.tag + ChatColor.GRAY + "bat");
                                        }
                                    } else
                                    {
                                        sender.sendMessage(Main.tag + ChatColor.RED + "Please enter a subcommand. Valid subcommands:");
                                        sender.sendMessage(Main.tag + ChatColor.GRAY + "bat");
                                    }
                                } else if (args[1].equalsIgnoreCase("powertest"))
                                {
                                    Powers.NUKE.start(p);
                                } else
                                {
                                    sender.sendMessage(ChatColor.GRAY + "Unknown dev command!  To view available commands type: /mg help or /mg ?");
                                }
                            } else
                            {
                                sender.sendMessage(ChatColor.GRAY + "Enter a dev command!  To view available commands type: /mg help or /mg ?");
                            }
                            return true;
                        }
                    }
                } else
                {
                    sender.sendMessage(ChatColor.GRAY + "Unknown command! To view available commands type: /mg help or /mg ?");
                }
            }
        }
        return false;
    }
}