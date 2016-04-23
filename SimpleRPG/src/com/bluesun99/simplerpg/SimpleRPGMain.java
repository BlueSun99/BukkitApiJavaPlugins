package com.bluesun99.simplerpg;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.bluesun99.simplerpg.ClassManager.RPGClass;

import net.milkbowl.vault.economy.Economy;

public class SimpleRPGMain extends JavaPlugin implements Listener {
	static org.bukkit.plugin.Plugin plugin;
	static java.util.logging.Logger logger;
	static Economy economy;
	static LocaleManager lm;
	static DBManager dbm;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		logger = this.getLogger();
		
		if (!loadEconomy())
			return;
		
		lm = new LocaleManager();
		
		dbm = new DBManager();
		dbm.getConnection();
		dbm.createDefaultStuff();
		
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getCommand("simplerpg").setExecutor(new CommandManager());
		this.getCommand("sr").setExecutor(new CommandManager());
	}
	
	@Override
	public void onDisable()
	{
		dbm.close();
	}
	
	private boolean loadEconomy()
	{
		if (this.getServer().getPluginManager().getPlugin("Vault") == null)
		{
			logger.severe(lm.getString("srt_unable_to_find_vault"));
			return false;
		}
		
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null || rsp.getProvider() == null) {
			logger.severe(lm.getString("srt_unable_to_find_econ"));
            return false;
        }
        
		economy = rsp.getProvider();
		
		return true;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		final Player ply = e.getPlayer();
		if (!ClassManager.hasPlayerClass(ply))
			SimpleRPGMain.plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
					new Runnable(){
				public void run()
				{
					ClassSelector.showSelectorGUI(ply);
				}
			}, 10L);
		else
		{
			RPGClass rc = ClassManager.getPlayerClass(ply);
			SimpleRPGMain.logger.info(lm.format("srt_player_join", ply.getName(), rc.getName()));
			ClassManager.setEffectToPlayer(ply);
		}
		
		if (CustomItems.Reselector.isReselecting(ply))
		{
			ply.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + lm.getString("srt_regive_reselector"));
			CustomItems.Reselector.give(ply);
			CustomItems.Reselector.setReselecting(ply, false);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent e)
	{
		final PlayerRespawnEvent _e = e;
		final Player ply = _e.getPlayer();
		
		_e.setRespawnLocation(ClassManager.getSpawnLocation(ply));
		
		this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run()
			{
				ClassManager.setEffectToPlayer(ply);
			}
		}, 1);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		Action a = e.getAction();
		ItemStack is = e.getItem();
		Player ply = e.getPlayer();
		
		if (a == Action.PHYSICAL || is == null || a == Action.LEFT_CLICK_AIR)
			return;
		
		if (CustomItems.Reselector.isValid(is))
		{
			ply.getInventory().remove(is);
			
			ClassSelector.showSelectorGUI(ply, true);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		/*Player ply = e.getPlayer();
		if (ClassManager.hasPlayerClass(ply))
			ClassManager.setPlayerClass(ply, RPGClass.NONE);*/
	}
	
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent e)
	{
		Player ply = (Player) e.getPlayer();
		String invname = ChatColor.stripColor(e.getInventory().getName());
		
		if (invname.equals(lm.getString("srt_class_rechoose")))
			CustomItems.Reselector.setReselecting(ply, true);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e)
	{
		String invname = ChatColor.stripColor(e.getInventory().getName());
		
		if (!invname.equals(lm.getString("srt_class_choose")) && !invname.equals(lm.getString("srt_class_rechoose")))
			return;

		Player ply = (Player) e.getWhoClicked();
		e.setCancelled(true);
		
		/*if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR
				|| !e.getCurrentItem().hasItemMeta())
		{
			ply.closeInventory();
			return;
		}*/
		
		if (e.getCurrentItem() == null)
			return;

		CustomItems.Reselector.setReselecting(ply, false);
		
		switch (e.getCurrentItem().getType()) {
		case WOOD_SWORD: // VIKING
			ply.sendMessage(lm.format("srt_choosed", RPGClass.VIKING.getFriendlyNameWithColor()));
			ClassSelector.selectClass(ply, RPGClass.VIKING);
			ply.playSound(ply.getLocation(), Sound.BLOCK_DISPENSER_LAUNCH, 0.1F, 1);
			break;
		case BOW: // ELF
			ply.sendMessage(lm.format("srt_choosed", RPGClass.ELF.getFriendlyNameWithColor()));
			ClassSelector.selectClass(ply, RPGClass.ELF);
			ply.playSound(ply.getLocation(), Sound.BLOCK_DISPENSER_LAUNCH, 0.1F, 1);
			break;
		case COMPASS: // EXIT
			ply.kickPlayer(lm.getString("srt_kick_no_choose"));
		default:
			//ply.closeInventory();
			break;
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e)
	{
		final Player ply = (Player) e.getPlayer();
		String invname = ChatColor.stripColor(e.getInventory().getName());
		
		if (invname.equals(lm.getString("srt_class_choose")) && !ClassManager.hasPlayerClass(ply))
			SimpleRPGMain.plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				public void run() {
					ply.sendMessage(ChatColor.RED + lm.getString("srt_please_choose"));
					ClassSelector.showSelectorGUI(ply);
				}
			}, 2L);
		
		if (invname.equals(lm.getString("srt_class_rechoose")) && CustomItems.Reselector.isReselecting(ply))
		{
			ply.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + lm.getString("srt_regive_reselector"));
			CustomItems.Reselector.give(ply);
			CustomItems.Reselector.setReselecting(ply, false);
		}
	}
}
