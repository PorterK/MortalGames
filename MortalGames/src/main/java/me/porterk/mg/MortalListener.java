package me.porterk.mg;


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

public class MortalListener implements Listener{

	public void scoreboard(Player p) {
		p.setScoreboard(Main.getInstance().s);
	}

	MortalAPI api = new MortalAPI();

	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent e){

		e.setJoinMessage(null);

		Player p = e.getPlayer();
		int playerCount = p.getServer().getOnlinePlayers().length;
		
		Main.getInstance().chat.put(p.getName(), "general");
		
		p.sendMessage(ChatColor.DARK_RED + "Testing the automated " + ChatColor.GOLD + "stuff 2.");
		
		if(playerCount < 10){
			p.sendMessage(ChatColor.GOLD + "Welcome, "  + ChatColor.GREEN + p.getDisplayName() +
					ChatColor.GOLD + " to " + ChatColor.DARK_RED + "The Mortal Games");
			p.sendMessage(ChatColor.BLUE + "Made by " + ChatColor.RED + " PorterK");
			p.sendMessage(ChatColor.GOLD + "There are currently " + ChatColor.DARK_RED + playerCount
					+ "/10" + ChatColor.GOLD +" players online!");
			scoreboard(p);
		}else if(playerCount >= 10){
			p.sendMessage(ChatColor.GOLD + "Welcome to " + ChatColor.DARK_RED + "The Mortal Games!");
			p.sendMessage(ChatColor.BLUE + "Visit our website at" + ChatColor.RED + " mortal.gldesert.com");
			p.sendMessage(ChatColor.DARK_RED + "" +ChatColor.ITALIC + "Currently the game is full.");
			api.setSpectating(p);
			
			
		}

		if(p.isOp()){
			p.sendMessage(ChatColor.AQUA + "Type" + ChatColor.DARK_RED + " /mg admin" + ChatColor.AQUA + " to get to admin chat!");
		}

		if(playerCount == 10){
			api.startGame();
		}
		
		if(api.isGameOn()){
			api.setSpectating(p);
		}


	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e){	
		String name = e.getPlayer().getName();
		e.setQuitMessage(Main.getInstance().tag + name + " chickened out!");
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e){
		e.setCancelled(true);

		Player sender = e.getPlayer();

		for(Player online : Main.getInstance().getServer().getOnlinePlayers()){


			if(Main.getInstance().chat.containsKey(sender.getName())){

				if(Main.getInstance().chat.get(sender.getName()).equals("general")){

					if(Main.getInstance().chat.get(online.getName()).equals("general") || online.isOp()){


						online.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "G" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " " + sender.getDisplayName() + ChatColor.WHITE +": " + e.getMessage());

					}
				}

				if(Main.getInstance().chat.get(sender.getName()).equals("green")){	

					if(Main.getInstance().chat.get(online.getName()).equals("green") || online.isOp()){

						online.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Gr" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " " + sender.getDisplayName() + ChatColor.WHITE + ": " + e.getMessage());


					}
				}

				if(Main.getInstance().chat.get(sender.getName()).equals("orange")){
					if(Main.getInstance().chat.get(online.getName()).equals("orange") || online.isOp()){

						online.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "O" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " " + sender.getDisplayName() + ChatColor.WHITE + ":" + e.getMessage());

					}
				}


				if(Main.getInstance().chat.get(sender.getName()).equals("admin")){
					if(Main.getInstance().chat.get(online.getName()).equals("admin") || online.isOp()){

						online.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "A" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " " + sender.getDisplayName() + ChatColor.WHITE + ": " + ChatColor.GRAY + "" + ChatColor.ITALIC + "" + e.getMessage());

					}
				}
				
				if(Main.getInstance().chat.get(sender.getName()).equals("spectate")){
					if(Main.getInstance().chat.get(online.getName()).equals("spectate") || online.isOp()){
						
						online.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "Spec" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET + " " + sender.getDisplayName() + ChatColor.WHITE + ": " + e.getMessage());
						
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
		
		api.setSpectating(a);
		
		if(a.getKiller() instanceof Player){
		
		e.setDeathMessage(ChatColor.DARK_RED + "Player " + ChatColor.GOLD + a.getName() + ChatColor.DARK_RED + " has fallen to " + ChatColor.GOLD + a.getKiller().getName());
		
		}
	}
	
	 @EventHandler
	    public void onEntityDamage(EntityDamageByEntityEvent e) {
		 
	        if (e.getDamager() instanceof Player){
	        	 Player p = (Player) e.getDamager();
	    		 Player a = (Player) e.getDamager();
	            if (e.getEntity() instanceof Player) {
	            	
	            	if(!api.allowPVP()){
	            		e.setCancelled(true);
	            		p.sendMessage(Main.getInstance().tag + ChatColor.DARK_RED + "PVP is not enabled yet.");
	            	}else{
	            		
	            		if(api.getTeam(p) == api.getTeam(a)){
	            			
	            			p.sendMessage(Main.getInstance().tag + ChatColor.DARK_RED + "You may not attack your team mates.");
	            			e.setCancelled(true);
	            			
	            		}
	            		
	            	}
	            }
	        }
	    }
	 
	 @EventHandler
	 public void onMobDeath(EntityDeathEvent e){
		 
		 if(e.getEntity().getKiller()  instanceof Player){
			 
			 if(e.getEntity().getType().equals(EntityType.ZOMBIE)){
				 
				 e.getDrops().clear();
				
				 e.getDrops().add(new ItemStack(Material.BREAD, 1));
				 e.getDrops().add(new ItemStack(Material.GOLD_INGOT, api.random(0, 5))); //Will eventually be currency
				 
			 }
			 
			 if(e.getEntity().getType().equals(EntityType.SKELETON)){
				 
				 e.getDrops().clear();
				 
				 e.getDrops().add(new ItemStack(Material.GOLD_INGOT, api.random(3, 6)));
				 
			 }
			 
			 if(e.getEntity().getType().equals(EntityType.SPIDER)){
				 
				 e.getDrops().clear();
				 
				 e.getDrops().add(new ItemStack(Material.GOLD_INGOT, api.random(4, 7)));
				 
			 }
			 
		 }
		 
	 }
	 
	 @EventHandler
	 public void onBlockPlace(BlockPlaceEvent e){
		 
		 Player p = e.getPlayer();
		 
		 if(!p.isOp()){
		 	if(!api.canBuild()){
		 		e.setCancelled(true);
		 	}
		 }
		 
	 }
	 
	 @EventHandler
	 public void onProjectileHit(ProjectileHitEvent e){
		 
		Entity a = e.getEntity();
		
		if(a instanceof Arrow){			
				
				if(!(((Arrow) a).getShooter() instanceof Player)){
				Location l = a.getLocation();
				 
				 l.getWorld().createExplosion(l, 3);
				 
				 a.remove();
				}
				
		 }
		 
	 }

}
