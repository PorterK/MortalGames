package me.porterk.mg;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

import me.porterk.mg.commands.Commands;
import me.porterk.mg.mobs.CustomEntityType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;

public class Main extends JavaPlugin
{
    public boolean                        SQL;

    public FileConfiguration              config          = getConfig();
    public MySQL                          ms;
    public Connection                     c               = null;
    public String                         database        = config.getString("MySQL.database");
    public static String                  tag;
    public static HashMap<String, String> chat;
    public static HashMap<String, String> lastchat        = new HashMap<String, String>();
    public Statement                      cs;
    public static Scoreboard              s;
    public String                         name;
    public Objective                      obj;
    public Score                          round;
    public Score                          time;
    public Score                          cash;
    public Score                          mobs;
    public Score                          website;

    private static Main                   plugin;

    public static MortalAPI               api             = new MortalAPI();
    public Commands                       CommandExecutor = new Commands();

    @SuppressWarnings("deprecation")
    @Override
    public void onEnable()
    {
        plugin = this;

        for (Entity e : Bukkit.getWorld("world").getEntities())
        {
            e.remove();
        }

        tag = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "MG" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET;
        chat = new HashMap<String, String>();

        s = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        obj = s.registerNewObjective("game", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(ChatColor.DARK_RED + "The Mortal Games");

        Score time = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + "Time: "));
        time.setScore(0);
        Score round = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + "Wave: "));
        round.setScore(0);
        Score website = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "" + ChatColor.ITALIC + "gld.bz"));
        website.setScore(-1);

        CustomEntityType.registerEntities();

        getCommand("mg").setExecutor(CommandExecutor);

        api.config();
        try
        {
            SQL = config.getBoolean("MySQL.enabled");

            if (!config.contains("MySQL.username"))
            {
                config.set("MySQL.username", "user");
                config.set("MySQL.password", "pass");
                config.set("MySQL.host name", "999.99.99.9");
                config.set("MySQL.port", "8080");
                config.set("MySQL.database", "mortal_games");
                config.set("MySQL.enabled", false);
                config.set("Dev Mode", false);
            }

        } catch (Exception e)
        {
            Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not connect to MySQL server");;
        }

        ms = new MySQL(this, config.getString("MySQL.host name"), config.getString("MySQL.port"), config.getString("MySQL.database"), config.getString("MySQL.username"), config.getString("MySQL.password"));
        c = ms.openConnection();

        try
        {
            config.save(api.configFile());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            getServer().getPluginManager().registerEvents(new MortalListener(), this);
            api.debugLog("Listener class registered!");
        } catch (NullPointerException e)
        {
            e.printStackTrace();
            api.debugLog("Listener class not registered! (NullPointerException)");
        }
        api.debugLog("Plugin enabled!");

        try
        {
            api.buildPrefab();
            api.buildMapYML();
            Bukkit.getServer().getLogger().log(Level.INFO, "Team bases constructed");
        } catch (Exception e)
        {
            api.debugLog(e.toString());
            e.printStackTrace();
        }

        try
        {
            api.registerSQLTable();
        } catch (Exception e)
        {
            Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not register SQL table");
        }
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, Arrays.asList(PacketType.Status.Server.OUT_SERVER_INFO), ListenerOptions.ASYNC)
        {

            @Override
            public void onPacketSending(PacketEvent event)
            {
                handlePing(event.getPacket().getServerPings().read(0));
            }
        });

        api.startVoteCycle();
    }

    @SuppressWarnings("deprecation")
    private void handlePing(WrappedServerPing ping)
    {
        ping.setPlayers(Arrays.asList(new WrappedGameProfile("id1", ChatColor.DARK_RED + "The Mortal Games"), new WrappedGameProfile("id2", ChatColor.GOLD + "A project by"), new WrappedGameProfile("id3", ChatColor.GOLD + "PorterK and Joannou1")

        ));
    }

    @Override
    public void onDisable()
    {
        api.debugLog("Plugin disabled!");
        chat.clear();
        CustomEntityType.unregisterEntities();

        for (Entity e : Bukkit.getWorld("world").getEntities())
        {
            e.remove();
        }

        plugin = null;
    }

    public static Main getInstance()
    {
        if (plugin == null)
        {
            plugin = new Main();
        }

        return plugin;
    }

    @SuppressWarnings("deprecation")
    public static void createHelixAsync(final Player player)
    {
        Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable()
        {
            public void run()
            {
                // player.sendMessage (ChatColor.BLUE + "Vwoosh!");
                createHelix(player);
                // Moved into createHelix to prevent ear rape.. Sounds kinda cool but glitchy if both.
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1.3F);

            }
        }, 1L);
    }

    public static void createHelix(Player player)
    {
        int radius = 2;
        for (double y = 0; y <= 50; y += 0.05)
        {
            double x = radius * Math.cos(y);
            double z = radius * Math.sin(y);
            Location loc = new Location(player.getLocation().getWorld(), player.getLocation().getX() + x, player.getLocation().getY() + y, player.getLocation().getZ() + z);
            loc.getWorld().playEffect(loc, Effect.SMOKE, 0);
            // player.playSound(player.getLocation(),Sound.ENDERMAN_TELEPORT,1, 1.3F);
        }
    }
}
