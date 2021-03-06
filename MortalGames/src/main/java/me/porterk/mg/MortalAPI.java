package me.porterk.mg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import me.porterk.mg.gametype.GameType;
import me.porterk.mg.mobs.MortalBat;
import me.porterk.mg.mobs.MortalTrader;
import me.porterk.mg.powerups.Powers;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Score;
import org.bukkit.util.Vector;
import org.bukkit.Location;

public class MortalAPI
{
    public int                beforeStart;
    public BukkitTask         musicLoop;
    public int                preGameTime;
    public int                wave;
    public int                waveCount;
    public int                waveNumber;
    public int                preWave;
    public int                preWaveCount;
    public int                mobAmount;
    public int                voteTime;
    public int                voteTimer;
    public int                totalVotes;
    public boolean            voteEnabled;
    protected boolean         isGameOn;
    protected boolean         pvp;
    protected boolean         build;
    int                       cash;
    int                       teamNumber;
    int                       duration;
    BukkitTask                bat;
    Statement                 s            = null;
    String                    uuid;
    HashMap<Player, Integer>  teamSelect   = new HashMap<Player, Integer>();
    HashMap<Player, String>   team         = new HashMap<Player, String>();
    public ArrayList<String>  spectating   = new ArrayList<String>();
    private FileConfiguration customConfig = null;

    public void config()
    {
        File config = new File(Main.getInstance().getDataFolder() + "/config.yml");
        File dir = Main.getInstance().getDataFolder();
        try
        {
            if (!dir.exists())
            {
                dir.mkdirs();
            }
            if (!config.exists())
            {
                config.createNewFile();
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public File configFile()
    {
        File config = new File(Main.getInstance().getDataFolder() + "/config.yml");

        return config;
    }

    public void debugLog(String logMessage)
    {
        if (Main.getInstance().config.getBoolean("Dev Mode"))
        {
            Date date = new Date();
            SimpleDateFormat d1 = new SimpleDateFormat("MM-dd-yyyy");
            String t = d1.format(date);
            File directory = Main.getInstance().getDataFolder();
            File logFolder = new File(directory, "/log");
            File logFile = new File("plugins/Mortal Games/log/" + t + ".txt");
            try
            {
                if (!directory.exists())
                {
                    directory.mkdirs();
                }
                if (!logFolder.exists())
                {
                    logFolder.mkdirs();
                }
                if (!logFile.exists())
                {
                    logFile.createNewFile();
                }
                FileWriter fw = new FileWriter(logFile, true);
                PrintWriter pw = new PrintWriter(fw);
                SimpleDateFormat d = new SimpleDateFormat("[MM/dd/yyyy HH:mm:ss]");
                String t1 = d.format(date);
                pw.println(t1 + logMessage);
                pw.flush();
                pw.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void buildMapYML()
    {
        File directory = Main.getInstance().getDataFolder();
        File mapdata = new File(directory, "/world_data");
        File yml = new File(mapdata + "/data.yml");

        try
        {
            if (!directory.exists())
            {
                directory.mkdirs();
            }

            if (!mapdata.exists())
            {
                mapdata.mkdirs();
            }

            if (!yml.exists())
            {
                yml.createNewFile();
            }

            customConfig = YamlConfiguration.loadConfiguration(yml);

            customConfig.addDefault("map_name", "MAP_NAME_HERE");
            customConfig.addDefault("team.orange.base.x", "0");
            customConfig.addDefault("team.orange.base.y", "0");
            customConfig.addDefault("team.orange.base.z", "0");
            customConfig.addDefault("team.orange.trader.x", "0");
            customConfig.addDefault("team.orange.trader.y", "0");
            customConfig.addDefault("team.orange.trader.z", "0");
            customConfig.addDefault("team.green.base.x", "0");
            customConfig.addDefault("team.green.base.y", "0");
            customConfig.addDefault("team.green.base.z", "0");
            customConfig.addDefault("team.green.trader.x", "0");
            customConfig.addDefault("team.green.trader.y", "0");
            customConfig.addDefault("team.green.trader.z", "0");
            customConfig.addDefault("drop_rate", "100"); // drop rates in percent
            customConfig.options().copyDefaults(true);
            getCustomConfig().save(yml);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public FileConfiguration getCustomConfig()
    {
        return customConfig;
    }

    public void playRecord(Player player, Vector loc, Material record)
    {

        /*
         * BlockPosition b = new BlockPosition(loc);
         * 
         * WrapperPlayServerWorldEvent event = new WrapperPlayServerWorldEvent(); event.setData(record.getId()); event.setLocation(b); event.sendPacket(player); event.setEffectId(1005);
         */
    }

    public void playWaitMusic()
    {
        for (final Player p : Bukkit.getServer().getOnlinePlayers())
        {
            final List<Material> record = new ArrayList<Material>();
            final HashMap<Material, Integer> map = new HashMap<Material, Integer>();

            map.put(Material.GOLD_RECORD, (2 * 60) + 58);
            map.put(Material.GREEN_RECORD, (3 * 60) + 5);
            map.put(Material.RECORD_3, (5 * 60) + 45);
            map.put(Material.RECORD_4, (3 * 60) + 5);
            map.put(Material.RECORD_5, (2 * 60) + 54);
            map.put(Material.RECORD_6, (3 * 60) + 17);
            map.put(Material.RECORD_7, (1 * 60) + 36);
            map.put(Material.RECORD_8, (2 * 60) + 30);
            map.put(Material.RECORD_9, (3 * 60) + 8);
            map.put(Material.RECORD_10, (4 * 60) + 11);
            map.put(Material.RECORD_12, (3 * 60) + 55);

            record.add(Material.RECORD_3);
            record.add(Material.RECORD_4);
            record.add(Material.RECORD_5);
            record.add(Material.RECORD_6);
            record.add(Material.RECORD_7);
            record.add(Material.RECORD_8);
            record.add(Material.RECORD_9);
            record.add(Material.RECORD_10);
            record.add(Material.GOLD_RECORD);
            record.add(Material.GREEN_RECORD);

            Collections.shuffle(record);
            duration = map.get(record.get(3));

            playRecord(p, p.getLocation().toVector(), record.get(3));

            musicLoop = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable()
            {
                public void run()
                {
                    duration--;

                    switch (duration)
                    {
                        case 0:
                            doNewTrack();
                    }
                }
            }, 20, 20L);
        }
    }

    public void doNewTrack()
    {
        musicLoop.cancel();
        playWaitMusic();
    }

    public boolean isGameOn()
    {

        return isGameOn;
    }

    public void startGame()
    {
        buildTrader(new Location(Bukkit.getWorld("world"), 0, 0, 0));

        playWaitMusic();

        setCanBuild(true);

        preGameTime = 301;

        beforeStart = Main.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable()
        {
            @SuppressWarnings("deprecation")
            public void run()
            {
                preGameTime--;
                final Score time = Main.getInstance().obj.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + "Time: "));
                time.setScore(preGameTime);

                switch (preGameTime)
                {

                    case 300:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "5 minutes");
                        break;
                    case 240:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "4 minutes");
                        break;
                    case 180:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "3 minutes");
                        break;
                    case 120:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "2 minutes");
                        break;
                    case 60:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "1 minute");
                        break;
                    case 30:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "30 seconds");
                        break;
                    case 10:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "10 seconds");
                        break;
                    case 9:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "9 seconds");
                        break;
                    case 8:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "8 seconds");
                        break;
                    case 7:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "7 seconds");
                        break;
                    case 6:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "6 seconds");
                        break;
                    case 5:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "5 seconds");
                        break;
                    case 4:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "4 seconds");
                        break;
                    case 3:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "3 seconds");
                        break;
                    case 2:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "2 seconds");
                        break;
                    case 1:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "1 seconds");
                        break;
                    case 0:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GREEN + "Prepare yourself," + ChatColor.DARK_RED + " mortal" + ChatColor.GREEN + ", the game is starting...");
                        for (final Player p : Main.getInstance().getServer().getOnlinePlayers())
                        {
                            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 10, 1);
                        }

                        debugLog("Game started");

                        Main.getInstance().getServer().getScheduler().cancelTask(beforeStart);

                        gameStart();

                        return;
                }
            }
        }, 0L, 20L);
    }

    public void gameStart()
    {
        musicLoop.cancel();

        Main.getInstance().getServer().getScheduler().cancelTask(beforeStart);

        List<Player> players = new ArrayList<Player>();
        for (Player player : Bukkit.getOnlinePlayers())
        {
            players.add(player);
        }

        Collections.shuffle(players);

        teamNumber = 0;
        for (Player player : players)
        {
            teamSelect.put(player, teamNumber++);
            if ((teamNumber == 1) || (teamNumber == 3) || (teamNumber == 5) || (teamNumber == 7) || (teamNumber == 9))
            {
                team.put(player, "orange");
                Main.getInstance();
                Main.chat.put(player.getName(), "orange");
                player.sendMessage("You're on the " + ChatColor.GOLD + "orange " + ChatColor.WHITE + "team");
            } else if ((teamNumber == 0) || (teamNumber == 2) || (teamNumber == 4) || (teamNumber == 6) || (teamNumber == 8))
            {
                team.put(player, "green");
                Main.getInstance();
                Main.chat.put(player.getName(), "green");
                player.sendMessage("You're on the " + ChatColor.GREEN + "green " + ChatColor.WHITE + "team");
            } else
            {
                player.sendMessage(Main.tag + ChatColor.RED + "Sorry, you did not make it into this game, feel free to spectate or rejoin the hub to join another match.");
                setSpectating(player);
            }
        }
        preWave();
    }

    public void preWave()
    {

        Main.getInstance().getServer().getScheduler().cancelTask(wave);

        setCanBuild(true);

        preWaveCount = 121;

        preWave = Main.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable()
        {
            @SuppressWarnings("deprecation")
            public void run()
            {
                preWaveCount--;

                final Score time = Main.getInstance().obj.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + "Time: "));
                time.setScore(preWaveCount);

                switch (preWaveCount)
                {
                    case 120:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Wave " + (waveNumber + 1) + " starts in " + ChatColor.DARK_RED + "2 minutes" + ChatColor.GOLD + ".");
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.AQUA + "Reinforce your fortress while you have the chance!");
                        break;
                    case 60:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Wave " + (waveNumber + 1) + " starts in " + ChatColor.DARK_RED + "1 minute" + ChatColor.GOLD + ".");
                        break;
                    case 30:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Wave " + (waveNumber + 1) + " starts in " + ChatColor.DARK_RED + "30 seconds" + ChatColor.GOLD + ".");
                        break;
                    case 10:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Wave " + (waveNumber + 1) + " starts in " + ChatColor.DARK_RED + "10 seconds" + ChatColor.GOLD + ".");
                        for (final Player p : Main.getInstance().getServer().getOnlinePlayers())
                        {
                            p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
                        }
                        break;
                    case 9:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Wave " + (waveNumber + 1) + " starts in " + ChatColor.DARK_RED + "9 seconds" + ChatColor.GOLD + ".");
                        for (final Player p : Main.getInstance().getServer().getOnlinePlayers())
                        {
                            p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
                        }
                        break;
                    case 8:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Wave " + (waveNumber + 1) + " starts in " + ChatColor.DARK_RED + "8 seconds" + ChatColor.GOLD + ".");
                        for (final Player p : Main.getInstance().getServer().getOnlinePlayers())
                        {
                            p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
                        }
                        break;
                    case 7:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Wave " + (waveNumber + 1) + " starts in " + ChatColor.DARK_RED + "7 seconds" + ChatColor.GOLD + ".");
                        for (final Player p : Main.getInstance().getServer().getOnlinePlayers())
                        {
                            p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
                        }
                        break;
                    case 6:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Wave " + (waveNumber + 1) + " starts in " + ChatColor.DARK_RED + "6 seconds" + ChatColor.GOLD + ".");
                        for (final Player p : Main.getInstance().getServer().getOnlinePlayers())
                        {
                            p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
                        }
                        break;
                    case 5:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Wave " + (waveNumber + 1) + " starts in " + ChatColor.DARK_RED + "5 seconds" + ChatColor.GOLD + ".");
                        for (final Player p : Main.getInstance().getServer().getOnlinePlayers())
                        {
                            p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
                        }
                        break;
                    case 4:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Wave " + (waveNumber + 1) + " starts in " + ChatColor.DARK_RED + "4 seconds" + ChatColor.GOLD + ".");
                        for (final Player p : Main.getInstance().getServer().getOnlinePlayers())
                        {
                            p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
                        }
                        break;
                    case 3:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Wave " + (waveNumber + 1) + " starts in " + ChatColor.DARK_RED + "3 seconds" + ChatColor.GOLD + ".");
                        for (final Player p : Main.getInstance().getServer().getOnlinePlayers())
                        {
                            p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
                        }
                        break;
                    case 2:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Wave " + (waveNumber + 1) + " starts in " + ChatColor.DARK_RED + "2 seconds" + ChatColor.GOLD + ".");
                        for (final Player p : Main.getInstance().getServer().getOnlinePlayers())
                        {
                            p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
                        }
                        break;
                    case 1:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Wave " + (waveNumber + 1) + " starts in " + ChatColor.DARK_RED + "1 seconds" + ChatColor.GOLD + ".");
                        for (final Player p : Main.getInstance().getServer().getOnlinePlayers())
                        {
                            p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
                        }
                        break;
                    case 0:
                        Main.getInstance().getServer().broadcastMessage(Main.tag + ChatColor.GOLD + "Prepare yourself, wave " + (waveNumber + 1) + " is starting...");
                        Main.getInstance().getServer().getScheduler().cancelAllTasks();
                        startWave();
                        for (final Player p : Main.getInstance().getServer().getOnlinePlayers())
                        {
                            p.getWorld().strikeLightningEffect(p.getLocation());
                            p.playSound(p.getLocation(), Sound.ZOMBIE_IDLE, 10, 1);
                        }
                        final Score round = Main.getInstance().obj.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + "Wave: "));
                        waveNumber += 1;
                        round.setScore(waveNumber);
                        return;
                }
            }
        }, 0L, 20L);
    }

    public void startWave()
    {
        setCanBuild(false);

        /*
         * for(Player p : Bukkit.getServer().getOnlinePlayers()){ String team = getTeam(p);
         * 
         * int x = (Integer) customConfig.get("team." + team + ".base.x"); int y = (Integer) customConfig.get("team." + team + ".base.y"); int z = (Integer) customConfig.get("team." + team + ".base.z");
         * 
         * Location base = new Location(p.getWorld(), x, y, z);
         * 
         * p.teleport(base); } Taking out for now to stop NPE
         */

        waveCount = 3;

        wave = Main.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable()
        {
            public void run()
            {
                waveCount--;

                switch (waveCount)
                {
                    case 0:
                        preWave();
                        return;
                }

                GameType.CLASSIC.doRound();
            }
        }, 0L, 960L);

        if (waveNumber > 10)
        {
            setAllowPVP(true);

        } else
        {
            setAllowPVP(false);
        }
    }

    public void buildPrefab()
    {

    }

    public String getTeam(Player p)
    {
        return team.get(p);
    }

    public int getCash(Player p)
    {
        try
        {
            uuid = p.getUniqueId().toString();

            Main.getInstance().cs = Main.getInstance().c.createStatement();

            ResultSet res = Main.getInstance().cs.executeQuery("SELECT * FROM mg WHERE UUID = '" + uuid + "';");
            res.next();

            cash = res.getInt("Money");

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return cash;
    }

    public int getKills(Player p)
    {
        int kills = 0;

        try
        {
            uuid = p.getUniqueId().toString();

            Main.getInstance().cs = Main.getInstance().c.createStatement();

            ResultSet res = Main.getInstance().cs.executeQuery("SELECT * FROM mg WHERE UUID = '" + uuid + "';");
            res.next();

            kills = res.getInt("Kills");
        } catch (SQLException e)
        {

            e.printStackTrace();
        }

        return kills;
    }

    public int getDeaths(Player p)
    {
        int deaths = 0;

        try
        {
            uuid = p.getUniqueId().toString();

            Main.getInstance().cs = Main.getInstance().c.createStatement();

            ResultSet res = Main.getInstance().cs.executeQuery("SELECT * FROM mg WHERE UUID = '" + uuid + "';");
            res.next();

            deaths = res.getInt("Deaths");
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return deaths;
    }

    public String getKDR(Player p)
    {
        float kd = 0;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        try
        {
            uuid = p.getUniqueId().toString();

            Main.getInstance().cs = Main.getInstance().c.createStatement();

            ResultSet res = Main.getInstance().cs.executeQuery("SELECT * FROM mg WHERE UUID = '" + uuid + "';");
            res.next();

            if ((res.getInt("Deaths") > 0) && (res.getInt("Kills") > 0))
            {
                kd = (res.getInt("Kills") / res.getInt("Deaths"));

            } else if ((res.getInt("Kills") == 0) && (res.getInt("Deaths") == 0))
            {
                kd = 1;
            } else
            {
                kd = res.getInt("Kills");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return df.format(kd);
    }

    public int gamesPlayed(Player p)
    {
        int games = 0;

        try
        {
            uuid = p.getUniqueId().toString();

            Main.getInstance().cs = Main.getInstance().c.createStatement();

            ResultSet res = Main.getInstance().cs.executeQuery("SELECT * FROM mg WHERE UUID = '" + uuid + "';");
            res.next();

            games = res.getInt("GamesPlayed");
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return games;
    }

    public String rank(Player p)
    {
        String rank = "";

        try
        {
            uuid = p.getUniqueId().toString();

            Main.getInstance().cs = Main.getInstance().c.createStatement();

            ResultSet res = Main.getInstance().cs.executeQuery("SELECT * FROM mg WHERE UUID = '" + uuid + "';");
            res.next();

            rank = res.getString("Rank");
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return rank;
    }

    public void registerSQLTable()
    {
        try
        {
            Main.getInstance().cs = Main.getInstance().c.createStatement();

            Main.getInstance().cs.executeUpdate("CREATE TABLE IF NOT EXISTS mg (UUID TEXT(50), PlayerName TEXT(20), Money int, Kills int, Deaths int, GamesPlayed int, Rank TEXT(20));");

            Main.getInstance().getLogger().log(Level.INFO, "MySQL table 'mg' was either found or created-- ready to rock!");
        } catch (SQLException e)
        {
            Main.getInstance().getLogger().log(Level.SEVERE, "MySQL table could not be created");

            e.printStackTrace();
        }
    }

    public void registerPlayer(Player p)
    {
        /*
         * try { Main.getInstance().cs = Main.getInstance().c.createStatement();
         * 
         * ResultSet res = Main.getInstance().cs.executeQuery("SELECT * FROM mg WHERE UUID = '" + p.getUniqueId().toString() + "';");
         * 
         * if(!res.next()){
         * 
         * p.sendMessage(Main.getInstance().tag + "Welcome to the " + ChatColor.DARK_RED + "Mortal Games" + ChatColor.RESET + "! Your stats are now being recorded!");
         * 
         * Main.getInstance().cs.executeUpdate("INSERT INTO mg (`UUID`, `PlayerName`, `Money`,`Kills`, `Deaths`, `GamesPlayed`, `Rank`) VALUES " + "('" + p.getUniqueId().toString() + "', '" + p.getName() + "', '500', '0', '0', '0', 'default');"); }
         * 
         * } catch (SQLException e) { debugLog(e.toString());
         * 
         * e.printStackTrace(); }
         */
    }

    public int random(int min, int max)
    {
        return (int) (min + (Math.random() * (max - min) + 1));
    }

    public void gameOver()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            player.kickPlayer(ChatColor.GOLD + getWinner() + " has won!");
        }

        Bukkit.getServer().shutdown();
    }

    public String getWinner()
    {
        String display = null;

        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (!isSpectating(player))
            {
                display = player.getDisplayName();
            }
        }

        return display;
    }

    public void setSpectating(Player p)
    {
        if (p != null)
        {
            if (spectating.size() < 10)
            {
                Main.getInstance();
                Main.chat.put(p.getName(), "spectate");
                p.sendMessage(Main.tag + ChatColor.AQUA + "You are now spectating.");
                p.getInventory().clear();

                spectating.add(p.getName());

                p.setGameMode(GameMode.SPECTATOR);
            } else
            {
                for (Player s : Bukkit.getServer().getOnlinePlayers())
                {
                    p.getInventory().clear();
                    s.kickPlayer(ChatColor.DARK_RED + "Game over! " + ChatColor.GOLD + p.getName() + ChatColor.DARK_RED + " has won!");
                    gameOver();
                }
            }
        }
    }

    public Boolean isSpectating(Player p)
    {
        if (spectating.contains(p.getName()))
        {
            return true;
        } else
        {
            return false;
        }

    }

    public void stopSpectating(Player p)
    {
        p.setGameMode(GameMode.SURVIVAL);

        spectating.remove(p.getName());
    }

    public void setAllowPVP(Boolean b)
    {
        pvp = b;
    }

    public Boolean allowPVP()
    {
        return pvp;
    }

    public void setCanBuild(Boolean b)
    {
        build = b;
    }

    public Boolean canBuild()
    {
        return build;
    }

    public void addItem(Material m, Inventory i, String name, String lore, Integer price)
    {
        ItemStack a = new ItemStack(m, 1);

        ItemMeta b = a.getItemMeta();

        List<String> c = new ArrayList<String>();

        c.add(lore);
        c.add(ChatColor.GREEN + "" + price + " emeralds");

        b.setDisplayName(ChatColor.GOLD + name);

        b.setLore(c);

        a.setItemMeta(b);

        i.addItem(a);
    }

    public void openShop(Player p)
    {
        Inventory shop = Main.getInstance().getServer().createInventory(null, 54, ChatColor.DARK_RED + "Mortal Shop");

        for (Powers power : Powers.values())
        {
            addItem(power.getShopItem(), shop, power.getName(), power.getDescription(), power.getPrice());
        }

        p.openInventory(shop);
    }

    public void moveTo(Location l, Entity e)
    {
        EntityLiving el = ((CraftLivingEntity) ((LivingEntity) e)).getHandle();
        ((EntityInsentient) el).getNavigation().a(l.getX(), l.getY(), l.getZ(), .3f);
    }

    public void bat(final MortalBat b, final Player p)
    {
        bat = Bukkit.getServer().getScheduler().runTaskTimer(Main.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                Bat e = (Bat) b.getBukkitEntity();

                Location a = e.getLocation();
                Location b = p.getLocation();

                Vector vector = b.toVector().subtract(a.toVector());

                vector.multiply(.1);

                e.setVelocity(vector);

                if (a.distance(b) <= 3)
                {
                    if (e.isValid())
                    {
                        a.getWorld().createExplosion(a, 3);

                    } else
                    {
                        cancelBat();
                    }

                    e.remove();
                }
            }
        }, 0L, 1L);
    }

    public void cancelBat()
    {
        bat.cancel();
    }

    public boolean isMod(Player p)
    {
        if (p.hasPermission("mortal.mod"))
        {
            return true;
        } else
        {
            return false;
        }
    }

    public void buildTrader(Location l)
    {
        l.setY(l.getWorld().getHighestBlockYAt(l));

        net.minecraft.server.v1_8_R3.World world = ((CraftWorld) l.getWorld()).getHandle();

        MortalTrader t = new MortalTrader(world);

        t.setPosition(l.getX(), l.getY(), l.getZ());
        world.addEntity(t, SpawnReason.CUSTOM);

        t.getBukkitEntity().setCustomName("Trader");
        t.getBukkitEntity().setCustomNameVisible(true);
        
        Location init = t.getBukkitEntity().getLocation();
    }

    public void broadcastMessage(String s)
    {
        for (Player p : Main.getInstance().getServer().getOnlinePlayers())
        {
            p.sendMessage(Main.tag + s);
        }
    }

    public void startVoteCycle()
    {
        voteEnabled = true;

        voteTime = 300;

        voteTimer = Main.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable()
        {
            public void run()
            {
                voteTime--;

                switch (voteTime)
                {
                    case 300:
                        broadcastMessage(ChatColor.DARK_RED + "Type " + ChatColor.GREEN + "/mg vote " + ChatColor.DARK_RED + "to vote to start the game early!");
                        break;

                    case 150:
                        broadcastMessage(ChatColor.DARK_RED + "Type " + ChatColor.GREEN + "/mg vote " + ChatColor.DARK_RED + "to vote to start the game early!");
                        break;
                    case 60:
                        broadcastMessage(ChatColor.DARK_RED + "Type " + ChatColor.GREEN + "/mg vote " + ChatColor.DARK_RED + "to vote to start the game early!");
                        break;
                    case 30:
                        broadcastMessage(ChatColor.DARK_RED + "Type " + ChatColor.GREEN + "/mg vote " + ChatColor.DARK_RED + "to vote to start the game early!");
                        break;
                    case 0:
                        broadcastMessage(ChatColor.DARK_RED + "There were not enough votes to start the game! Restarting cycle!");

                        voteTime = 300;
                        return;
                }
            }
        }, 0L, 20L);
    }

    public void vote(Player p)
    {
        int a = 0;

        for (Player u : Bukkit.getOnlinePlayers())
        {

            if (!isSpectating(u))
            {
                a++;
            }
        }

        int votesNeeded = a / 2;

        totalVotes += 1;

        if (totalVotes >= votesNeeded)
        {
            startGame();
            Main.getInstance().getServer().getScheduler().cancelTask(voteTimer);

        } else
        {
            broadcastMessage(ChatColor.GREEN + "Thank you " + p.getDisplayName() + ChatColor.GREEN + " for your vote!" + ChatColor.DARK_RED + (votesNeeded - totalVotes) + ChatColor.GREEN + " votes still needed!");
        }
    }
}
