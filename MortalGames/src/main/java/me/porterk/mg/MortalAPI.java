package me.porterk.mg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;








import me.porterk.mg.mobs.MortalSkeleton;
import me.porterk.mg.mobs.MortalSpider;
import me.porterk.mg.mobs.MortalZombie;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Score;
import org.bukkit.Location;

public class MortalAPI {

	public int beforeStart;
	public int preGameTime;
	public int wave;
	public int waveCount;
	public int waveNumber;
	public int preWave;
	public int preWaveCount;
	public int mobAmount;
	protected boolean isGameOn;
	protected boolean pvp;
	protected boolean build;
	int cash;
	int teamNumber;
	Statement s = null;
	String name;
	HashMap<Player, Integer> teamSelect = new HashMap<Player, Integer>();
	HashMap<Player, String> team = new HashMap<Player, String>();
	ArrayList<String> spectating =  new ArrayList<String>();
	
	public void config(){

		File config = new File(Main.getInstance().getDataFolder() + "/config.yml");
		File dir = Main.getInstance().getDataFolder();
		try{
			if(!dir.exists()){
				dir.mkdirs();
			}
			if(!config.exists()){
				config.createNewFile();
			}

		}catch(IOException e){
			e.printStackTrace();
		}

	}

	public File configFile(){

		File config = new File(Main.getInstance().getDataFolder() + "/config.yml");

		return config;
	}

	public void debugLog(String logMessage){

		if(Main.getInstance().config.getBoolean("Dev Mode")){
			Date date = new Date();
			SimpleDateFormat d1 = new SimpleDateFormat("MM-dd-yyyy");
			String t = d1.format(date);
			File directory = Main.getInstance().getDataFolder();
			File logFolder = new File(directory, "/log");
			File logFile = new File("Main.getInstance()s/Mortal Games/log/" + t + ".txt");
			try{
				if(!directory.exists()){
					directory.mkdirs();
				}
				if(!logFolder.exists()){
					logFolder.mkdirs();
				}
				if(!logFile.exists()){
					logFile.createNewFile();
				}
				FileWriter fw = new FileWriter(logFile, true);
				PrintWriter pw = new PrintWriter(fw);
				SimpleDateFormat d = new SimpleDateFormat("[MM/dd/yyyy HH:mm:ss]");
				String t1 = d.format(date);
				pw.println(t1 + logMessage);	
				pw.flush();
				pw.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	public boolean isGameOn(){

		return isGameOn;
	}

	public void startGame(){
		setCanBuild(true);

		preGameTime = 301;

		beforeStart = Main.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {

			@SuppressWarnings("deprecation")
			public void run(){
				

				preGameTime--;
				final Score time = Main.getInstance().obj.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + "Time: "));
				time.setScore(preGameTime);

				switch(preGameTime){

				case 300: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "5 minutes");
				break;
				case 240: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "4 minutes");
				break;
				case 180: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "3 minutes");
				break;
				case 120: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "2 minutes");
				break;
				case 60: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "1 minute");
				break;
				case 30: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "30 seconds");
				break;
				case 10: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "10 seconds");
				break;
				case 9: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "9 seconds");
				break;
				case 8: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "8 seconds");
				break;
				case 7: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "7 seconds");
				break;
				case 6: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "6 seconds");
				break;
				case 5: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "5 seconds");
				break;
				case 4: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "4 seconds");
				break;
				case 3: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "3 seconds");
				break;
				case 2: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "2 seconds");
				break;
				case 1: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Game starting in " + ChatColor.DARK_RED + "1 seconds");
				break;
				case 0: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GREEN + "Prepare yourself,"  + ChatColor.DARK_RED + " mortal" +
						ChatColor.GREEN + ", the game is starting...");
				for(final Player p : Main.getInstance().getServer().getOnlinePlayers()){
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

	public void gameStart(){
		
		Main.getInstance().getServer().getScheduler().cancelTask(beforeStart);

		List<Player> players = new ArrayList<Player>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			players.add(player);
		}

		Collections.shuffle(players);

		teamNumber = 0;
		for (Player player : players) {
			teamSelect.put(player, teamNumber++);
			if ((teamNumber == 1) ||
				(teamNumber == 3) ||
				(teamNumber == 5) ||
				(teamNumber == 7) ||
				(teamNumber == 9)
					) {
				team.put(player, "orange");
				Main.getInstance().chat.put(player.getName(), "orange");
				player.sendMessage("You're on the " + ChatColor.GOLD + "orange " + ChatColor.WHITE + "team");
			}else{
				team.put(player, "green");
				Main.getInstance().chat.put(player.getName(), "green");
				player.sendMessage("You're on the " + ChatColor.GREEN + "green " + ChatColor.WHITE + "team");
			}
		}
		preWave();
	}
	public void preWave(){
		
		Main.getInstance().getServer().getScheduler().cancelTask(wave);
		
		setCanBuild(true);

		preWaveCount = 121;

		preWave = Main.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() { 

			@SuppressWarnings("deprecation")
			public void run(){

				preWaveCount--;
				
				final Score time = Main.getInstance().obj.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + "Time: "));
				time.setScore(preWaveCount);
				
				switch(preWaveCount){

				case 120: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Wave " + (waveNumber + 1) + " starts in " + ChatColor.DARK_RED + "2 minutes" + ChatColor.GOLD + ".");
				 Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.AQUA + "Reinforce your fortress while you have the chance!");
				break;
				case 60: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Wave " + (waveNumber + 1)  + " starts in " + ChatColor.DARK_RED + "1 minute" + ChatColor.GOLD + ".");
				break;
				case 30: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Wave " + (waveNumber + 1)  + " starts in " + ChatColor.DARK_RED + "30 seconds" + ChatColor.GOLD + ".");
				break;
				case 10: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Wave " + (waveNumber + 1)  + " starts in " + ChatColor.DARK_RED + "10 seconds" + ChatColor.GOLD + ".");
				for(final Player p : Main.getInstance().getServer().getOnlinePlayers()){
				p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
				}
				break;
				case 9: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Wave " + (waveNumber + 1)  + " starts in " + ChatColor.DARK_RED + "9 seconds" + ChatColor.GOLD + ".");
				for(final Player p : Main.getInstance().getServer().getOnlinePlayers()){
				p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
				}
				break;
				case 8: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Wave " + (waveNumber + 1)  + " starts in " + ChatColor.DARK_RED + "8 seconds" + ChatColor.GOLD + ".");
				for(final Player p : Main.getInstance().getServer().getOnlinePlayers()){
				p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
				}
				break;
				case 7: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Wave " + (waveNumber + 1)  + " starts in " + ChatColor.DARK_RED + "7 seconds" + ChatColor.GOLD + ".");
				for(final Player p : Main.getInstance().getServer().getOnlinePlayers()){
				p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
				}
				break;
				case 6: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Wave " + (waveNumber + 1)  + " starts in " + ChatColor.DARK_RED + "6 seconds" + ChatColor.GOLD + ".");
				for(final Player p : Main.getInstance().getServer().getOnlinePlayers()){
				p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
				}
				break;
				case 5: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Wave " + (waveNumber + 1)  + " starts in " + ChatColor.DARK_RED + "5 seconds" + ChatColor.GOLD + ".");
				for(final Player p : Main.getInstance().getServer().getOnlinePlayers()){
				p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
				}
				break;
				case 4: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Wave " + (waveNumber + 1)  + " starts in " + ChatColor.DARK_RED + "4 seconds" + ChatColor.GOLD + ".");
				for(final Player p : Main.getInstance().getServer().getOnlinePlayers()){
				p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
				}
				break;
				case 3: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Wave " + (waveNumber + 1)  + " starts in " + ChatColor.DARK_RED + "3 seconds" + ChatColor.GOLD + ".");
				for(final Player p : Main.getInstance().getServer().getOnlinePlayers()){
				p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
				}
				break;
				case 2: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Wave " + (waveNumber + 1)  + " starts in " + ChatColor.DARK_RED + "2 seconds" + ChatColor.GOLD + ".");
				for(final Player p : Main.getInstance().getServer().getOnlinePlayers()){
				p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
				}
				break;
				case 1: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Wave " + (waveNumber + 1)  + " starts in " + ChatColor.DARK_RED + "1 seconds" + ChatColor.GOLD + ".");
				for(final Player p : Main.getInstance().getServer().getOnlinePlayers()){
				p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
				}
				break;
				case 0: Main.getInstance().getServer().broadcastMessage(Main.getInstance().tag + ChatColor.GOLD + "Prepare yourself, wave " + (waveNumber + 1) + " is starting...");
				Main.getInstance().getServer().getScheduler().cancelAllTasks();
				startWave();
				for(final Player p : Main.getInstance().getServer().getOnlinePlayers()){
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

	public void startWave(){
				
				setCanBuild(false);

				waveCount = 3;

				wave = Main.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() { 

					public void run(){

						waveCount--;

						if(waveCount == 0){
							
							preWave();
							
							
							
						}

						for(Player tar : Main.getInstance().getServer().getOnlinePlayers()){
							
							mobAmount = waveNumber * 2;
								
								tar.getWorld().setTime(15000);
								
							if(!spectating.contains(tar.getName())){

								int zombie;

								zombie = random(waveNumber, mobAmount);
								mobAmount -= zombie;

								while(zombie > 0){
									Location mobSpawn = new Location(tar.getWorld(), random(tar.getLocation().getBlockX() - 20, tar.getLocation().getBlockX() + 13) + 1, 0, random(tar.getLocation().getBlockZ() - 20, tar.getLocation().getBlockZ() + 13) + 1);

									mobSpawn.setY(mobSpawn.getWorld().getHighestBlockYAt(mobSpawn));

									net.minecraft.server.v1_8_R1.World world = ((CraftWorld) tar.getWorld()).getHandle();

									MortalZombie z = new MortalZombie(world);

									z.setPosition(mobSpawn.getX(), mobSpawn.getY(), mobSpawn.getZ());
									world.addEntity(z, SpawnReason.CUSTOM);
									
									zombie--;

								}
								
								
								if(waveNumber >= 3){
									
									int skeleton;
									
									skeleton = random((waveNumber / 2), waveNumber);
									
									while(skeleton > 0){
										
										
										Location mobSpawn = new Location(tar.getWorld(), random(tar.getLocation().getBlockX() - 20, tar.getLocation().getBlockX() + 13) + 1, 0, random(tar.getLocation().getBlockZ() - 20, tar.getLocation().getBlockZ() + 13) + 1);

										mobSpawn.setY(mobSpawn.getWorld().getHighestBlockYAt(mobSpawn));

										net.minecraft.server.v1_8_R1.World world = ((CraftWorld) tar.getWorld()).getHandle();
										
										MortalSkeleton s = new MortalSkeleton(world);
										
										s.setPosition(mobSpawn.getX(),mobSpawn.getY(), mobSpawn.getZ());
										world.addEntity(s, SpawnReason.CUSTOM);
										
										ItemStack bow = new ItemStack(Material.BOW);
											
											bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
											
											Skeleton sk = (Skeleton) s.getBukkitEntity();
											
											EntityEquipment se = (EntityEquipment) sk.getEquipment();
											
											se.setItemInHand(bow);
											
											skeleton--;
									}
									
								
								}
								
								if(waveNumber >= 5){
						
									int spider;
									
									spider = random(waveNumber / 5, waveNumber / 2);
									
									while(spider > 0){
										
										
										Location mobSpawn = new Location(tar.getWorld(), random(tar.getLocation().getBlockX() - 20, tar.getLocation().getBlockX() + 13) + 1, 0, random(tar.getLocation().getBlockZ() - 20, tar.getLocation().getBlockZ() + 13) + 1);

										mobSpawn.setY(mobSpawn.getWorld().getHighestBlockYAt(mobSpawn));

										net.minecraft.server.v1_8_R1.World world = ((CraftWorld) tar.getWorld()).getHandle();
										
										MortalSpider s = new MortalSpider(world);
										
										s.setPosition(mobSpawn.getX(), mobSpawn.getY(), mobSpawn.getZ());
										
										world.addEntity(s, SpawnReason.CUSTOM);
										
										spider--;
									}
									
								}
							}
						}

					}

				}, 0L, 960L);

				if(waveNumber > 10){
					
					setAllowPVP(true);
					
				}else{
					setAllowPVP(false);
				}


			}

	public void buildPrefab(){
		
		
	}
	
	public String getTeam(Player p){

		return team.get(p);
	}

	public int getCash(Player p){



		try{

			name = p.getName();

			Main.getInstance().cs = Main.getInstance().c.createStatement();
			ResultSet res = Main.getInstance().cs.executeQuery("SELECT * FROM mortal WHERE PlayerName = '" + name + "';");

			if(!res.next()){
				Main.getInstance().cs.executeUpdate("INSERT INTO mortal (`PlayerName`, `inGameCash`, `totalCash`, " +
						"`totalKills`, `serverCash`, `rank`) VALUES ('" + p.getName() + "', '500', '500', '0', '0', 'Default');");
			}else{
				res.next();
			}

			if(res.getString("PlayerName") == null){

				cash = 1;

			}else {

				cash = res.getInt("inGameCash");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return cash;
	}

	public int random(int min, int max){

		return (int) (min + (Math.random() * (max - min) + 1));
	}
	
	public void gameOver(){
		
		Bukkit.getServer().shutdown();
		
	}
	
	public void setSpectating(Player p){

		if(p != null){
		if(spectating.size() < 10){
			
			Main.getInstance().chat.put(p.getName(), "spectate");
			p.sendMessage(Main.getInstance().tag + ChatColor.AQUA + "You are now spectating.");
			p.getInventory().clear();
			
			spectating.add(p.getName());
			
			p.setGameMode(GameMode.SPECTATOR);
		}else{
			
			for(Player s : Bukkit.getServer().getOnlinePlayers()){
				p.getInventory().clear();
				s.kickPlayer(ChatColor.DARK_RED + "Game over! " + ChatColor.GOLD + p.getName() + ChatColor.DARK_RED + " has won!");
				gameOver();
			}
		}
			
		}
		
	}
	
	public Boolean isSpectating(Player p){
		
		if(spectating.contains(p.getName())){
			return true;
		}else{
			return false;
		}
		
	}
	
	public void stopSpectating(Player p){
		p.setGameMode(GameMode.SURVIVAL);
		
		spectating.remove(p.getName());
		
	}
	
	public void setAllowPVP(Boolean b){
		pvp = b;
	}
	
	public Boolean allowPVP(){
		
		return pvp;
	}
	
	public void setCanBuild(Boolean b){
		
		build = b;
		
	}
	
	public Boolean canBuild(){
		
		return build;
	}

}