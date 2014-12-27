package me.porterk.mg;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;

import me.porterk.mg.mobs.CustomEntityType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class Main extends JavaPlugin{
	public boolean SQL;

	public FileConfiguration config = getConfig();
	public MySQL ms;
	public Connection c = null;
	public String database = config.getString("MySQL.database");
	public String tag;
	public HashMap<String, String> chat;
	public HashMap<String, String> lastchat = new HashMap<String, String>();
	public Statement cs;
	public Scoreboard s;
	public String name;
	public Objective obj;
	public Score round;
	public Score time;
	public Score cash;
	public Score mobs;
	public Score website;
	
	private static Main plugin;
	

	
	MortalAPI api = new MortalAPI();
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable(){
		
		plugin = this;
		
		for(Entity e : Bukkit.getWorld("world").getEntities()){
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
		Score website = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "" + ChatColor.ITALIC +  "mortal.gldesert.com"));
		website.setScore(-9999);
		
		CustomEntityType.registerEntities();
		
		api.config();
		try{
		SQL = config.getBoolean("MySQL.enabled");
		
		if(!config.contains("MySQL.username")){
		config.set("MySQL.username", "user");
		config.set("MySQL.password", "pass");
		config.set("MySQL.host name", "999.99.99.9");
		config.set("MySQL.port", "8080");
		config.set("MySQL.database", "mortal_games");
		config.set("MySQL.enabled", false);
		config.set("Dev Mode", false);
		}
		
		}catch(Exception e){
			
			Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not connect to MySQL server");;
			
		}
		
		ms = new MySQL(this, config.getString("MySQL.host name"), 
				config.getString("MySQL.port"), config.getString("MySQL.database"), 
				config.getString("MySQL.username"), config.getString("MySQL.password"));
		c = ms.openConnection();
		
		try {
			config.save(api.configFile());
		} catch (IOException e) {
			e.printStackTrace();
		}	
		try{
			getServer().getPluginManager().registerEvents(new MortalListener(), this);
			api.debugLog("Listener class registered!");
		}catch(NullPointerException e){
			e.printStackTrace();
			api.debugLog("Listener class not registered! (NullPointerException)");
		}
		api.debugLog("Plugin enabled!");
		
		try{
		api.buildPrefab();
		api.buildMapYML();
		Bukkit.getServer().getLogger().log(Level.INFO, "Team bases constructed");
		}catch(Exception e){
			api.debugLog(e.toString());
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDisable(){
		api.debugLog("Plugin disabled!");
		chat.clear();
		CustomEntityType.unregisterEntities();
		
		for(Entity e : Bukkit.getWorld("world").getEntities()){
			e.remove();
		}
		
		plugin = null;
	}

	public static Main getInstance(){
		
		if(plugin == null){
			plugin = new Main();
		}
		
		return plugin;
	}
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		Player p = (Player) s;

		if(cmd.getName().equalsIgnoreCase("mg")){

			if(args[0].equalsIgnoreCase("admin")){

				if(chat.containsKey(p.getName())){

					if(!chat.get(p.getName()).equals("admin")){
						
						if(chat.containsKey(p.getName())){
							lastchat.put(p.getName(), chat.get(p.getName()));
						}else{
							lastchat.put(p.getName(), "general");
						}

						p.sendMessage(tag + ChatColor.DARK_RED + "Admin " + ChatColor.WHITE + "chat has been " + ChatColor.GREEN + "enabled" + ChatColor.WHITE + ".");

						chat.put(p.getName(), "admin");
						


					}else{
						p.sendMessage(tag + ChatColor.DARK_RED + "Admin " + ChatColor.WHITE + "chat has been " + ChatColor.RED + "disabled" + ChatColor.WHITE + ".");
						
						if(lastchat.containsKey(p.getName())){
							chat.put(p.getName(), lastchat.get(p.getName()));
						}else{
							chat.put(p.getName(), "general");
						}

					}

				}else{
					chat.put(p.getName(), "admin");
					p.sendMessage(tag + ChatColor.DARK_RED + "Admin " + ChatColor.WHITE + "chat has been " + ChatColor.GREEN + "enabled" + ChatColor.WHITE + ".");
					p.sendMessage("this one");
				}
				return true;
			}

			if(args[0].equalsIgnoreCase("dev")){

				if(p.hasPermission("mortal.dev")){

					if(args[1].equalsIgnoreCase("start")){

						if(args[2].equalsIgnoreCase("game")){

							api.startGame();

						}

						if(args[2].equalsIgnoreCase("waitwave")){

							if(args.length >= 4){

								if(args[3].equalsIgnoreCase("skipcount")){

									if(args.length >= 5){

										if(!args[4].equals(null)){

											try{

												api.waveNumber = Integer.parseInt(args[4]);

											}catch(Exception e){

												s.sendMessage(tag + ChatColor.GOLD + "You need to enter a wave " + ChatColor.BOLD + "NUMBER.");

											}

										}else{
											api.waveNumber += 1;

										}
										api.startWave();
									}

								}else{

									api.preWave();
								}

							}else{
								api.preWave();
							}


						}

					}else{
						p.sendMessage(tag + ChatColor.GOLD + "Not enough arguments.");
					}

				}
				return true;
			}

			if(args[0].equals(null)){

				s.sendMessage(tag + ChatColor.GOLD + "Do me a favor, learn the commands " + ChatColor.DARK_RED + "[INVALID ARGUMENTS]");

			}

		}

		return false;
	}
	
	@SuppressWarnings("deprecation")
	public static void createHelixAsync(final Player player)
	{
	  Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable()
	  {
	    public void run()
	    {
	            	//player.sendMessage (ChatColor.BLUE + "Vwoosh!");
	            	createHelix(player);
	            	//Moved into createHelix to prevent ear rape.. Sounds kinda cool but glitchy if both.
	            	player.playSound(player.getLocation(),Sound.ENDERMAN_TELEPORT,1, 1.3F);	

	     }
	  }
	  ,(1) * 1L);
	}

public static void createHelix(Player player) {
    int radius = 2;
    for (double y = 0; y <= 50; y+=0.05) {
        double x = radius * Math.cos(y);
        double z = radius * Math.sin(y);
        Location loc = new Location(player.getLocation().getWorld(), player.getLocation().getX() + x, player.getLocation().getY() + y, player.getLocation().getZ() + z);
        loc.getWorld().playEffect(loc, Effect.WITCH_MAGIC, 0);
        //player.playSound(player.getLocation(),Sound.ENDERMAN_TELEPORT,1, 1.3F);
    }
}
}
