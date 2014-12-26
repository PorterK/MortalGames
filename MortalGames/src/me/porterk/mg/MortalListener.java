package me.porterk.mg;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MortalListener implements Listener{

	Main plugin;

	public MortalListener(Main plugin) {
		this.plugin = plugin;
	}


	MortalAPI api = new MortalAPI(plugin);
	
	

	public void scoreboard(Player p) {
		p.setScoreboard(plugin.s);
	}



	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent e){

		e.setJoinMessage(null);

		Player p = e.getPlayer();
		int playerCount = p.getServer().getOnlinePlayers().size();
		
		plugin.chat.put(p.getName(), "general");
		
		if(playerCount < 10){
			p.sendMessage(ChatColor.GOLD + "Welcome, "  + ChatColor.GREEN + p.getDisplayName() +
					ChatColor.GOLD + " to " + ChatColor.DARK_RED + "The Mortal Games");
			p.sendMessage(ChatColor.BLUE + "Made by " + ChatColor.RED + " PorterK");
			p.sendMessage(ChatColor.GOLD + "There are currently " + ChatColor.DARK_RED + playerCount
					+ "/10" + ChatColor.GOLD +" players online!");
			scoreboard(p);
		}else if(playerCount >= 10){
			p.sendMessage(ChatColor.GOLD + "Welcome to " + ChatColor.DARK_RED + "The Mortal Games!");
			p.sendMessage(ChatColor.BLUE + "Visit my website at" + ChatColor.RED + " kalob.net!");
			p.sendMessage(ChatColor.DARK_RED + "" +ChatColor.ITALIC + "Currently the game is full.");
			api.setSpectating(p, plugin);
			
			
		}

		if(p.isOp()){
			p.sendMessage(ChatColor.AQUA + "Type" + ChatColor.DARK_RED + " /mg admin" + ChatColor.AQUA + " to get to admin chat!");
		}

		if(playerCount == 10){
			api.startGame();
		}
		
		if(api.isGameOn()){
			api.setSpectating(p, plugin);
		}


	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e){	
		String name = e.getPlayer().getName();
		e.setQuitMessage(plugin.tag + name + " chickened out!");
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e){
		e.setCancelled(true);

		Player sender = e.getPlayer();

		for(Player online : plugin.getServer().getOnlinePlayers()){


			if(plugin.chat.containsKey(sender.getName())){

				if(plugin.chat.get(sender.getName()).equals("general")){

					if(plugin.chat.get(online.getName()).equals("general") || online.isOp()){


						online.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "G" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " " + sender.getDisplayName() + ChatColor.WHITE +": " + e.getMessage());

					}
				}

				if(plugin.chat.get(sender.getName()).equals("green")){	

					if(plugin.chat.get(online.getName()).equals("green") || online.isOp()){

						online.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Gr" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " " + sender.getDisplayName() + ChatColor.WHITE + ": " + e.getMessage());


					}
				}

				if(plugin.chat.get(sender.getName()).equals("orange")){
					if(plugin.chat.get(online.getName()).equals("orange") || online.isOp()){

						online.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "O" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " " + sender.getDisplayName() + ChatColor.WHITE + ":" + e.getMessage());

					}
				}


				if(plugin.chat.get(sender.getName()).equals("admin")){
					if(plugin.chat.get(online.getName()).equals("admin") || online.isOp()){

						online.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "A" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " " + sender.getDisplayName() + ChatColor.WHITE + ": " + ChatColor.GRAY + "" + ChatColor.ITALIC + "" + e.getMessage());

					}
				}
				
				if(plugin.chat.get(sender.getName()).equals("spectate")){
					if(plugin.chat.get(online.getName()).equals("spectate") || online.isOp()){
						
						online.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "Spec" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + "" + sender.getDisplayName() + ChatColor.WHITE + ": " + e.getMessage());
						
					}
					
				}
			}
		}

	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e){
		
		if(!e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM)){
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		
		e.setDeathMessage(null);
		
		Player a =  e.getEntity();
		
		a.setHealth(20);
		
		api.setSpectating(a, plugin);
		
		if(a.getKiller() instanceof Player){
		
		e.setDeathMessage(ChatColor.DARK_RED + "Player " + ChatColor.GOLD + a.getName() + ChatColor.DARK_RED + " has fallen to " + ChatColor.GOLD + a.getKiller().getName());
		
		}
	}

}
