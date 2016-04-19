package com.bluesun99.simplerpg;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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
	
	@Override
	public void onEnable()
	{
		plugin = this;
		logger = this.getLogger();
		
		if (!loadEconomy())
			return;
		
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getCommand("gimme").setExecutor(new CommandManager());
	}
	
	private boolean loadEconomy()
	{
		if (this.getServer().getPluginManager().getPlugin("Vault") == null)
		{
			logger.severe("Vault 플러그인을 찾지 못했습니다. 플러그인을 중단합니다.");
			return false;
		}
		
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null || rsp.getProvider() == null) {
			logger.severe("Economy 호환 플러그인을 찾지 못했습니다. 플러그인을 중단합니다.");
            return false;
        }
        
		economy = rsp.getProvider();
		
		return true;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		Player ply = e.getPlayer();
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
			SimpleRPGMain.logger.info("입장한 " + ply.getName() + "의 클래스는 " + rc.getKoreanName() + "입니다.");
			ClassManager.setEffectToPlayer(ply);
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e)
	{
		Player ply = e.getPlayer();
		RPGClass rc = ClassManager.getPlayerClass(ply);
		
		ply.sendMessage(rc.getClassColor() + rc.getKoreanName() + ChatColor.WHITE + "에 맞는 버프를 지급받고 스폰 장소로 텔레포트 되었습니다.");
		ClassManager.setEffectToPlayer(ply);
		e.setRespawnLocation(ClassManager.getSpawnLocation(ply));
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		Action a = e.getAction();
		ItemStack is = e.getItem();
		Player ply = e.getPlayer();
		
		if (a == Action.PHYSICAL || is == null || a == Action.LEFT_CLICK_AIR)
			return;
		
		if (CustomItems.Reselector.isReselector(is))
		{
			ply.getInventory().remove(is);
			
			ClassSelector.showSelectorGUI(ply, true);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		Player ply = e.getPlayer();
		if (ClassManager.hasPlayerClass(ply))
			ClassManager.setPlayerClass(ply, RPGClass.NONE);
	}
	
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent e)
	{
		Player ply = (Player) e.getPlayer();
		String invname = ChatColor.stripColor(e.getInventory().getName());
		
		if (invname.equals("클래스 재선택"))
			CustomItems.Reselector.setReselecting(ply, true);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e)
	{
		String invname = ChatColor.stripColor(e.getInventory().getName());
		
		if (!invname.equals("클래스 선택") && !invname.equals("클래스 재선택"))
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
		
		switch (e.getCurrentItem().getType()) {
		case WOOD_SWORD: // VIKING
			ply.sendMessage(ChatColor.GOLD + "바이킹" + ChatColor.WHITE + " 클래스를 선택하셨습니다.");
			ClassSelector.selectClass(ply, RPGClass.VIKING);
			ply.playSound(ply.getLocation(), Sound.BLOCK_DISPENSER_LAUNCH, 0.1F, 1);
			ply.closeInventory();
			break;
		case BOW: // ELF
			ply.sendMessage(ChatColor.AQUA + "엘프" + ChatColor.WHITE + " 클래스를 선택하셨습니다.");
			ClassSelector.selectClass(ply, RPGClass.ELF);
			ply.playSound(ply.getLocation(), Sound.BLOCK_DISPENSER_LAUNCH, 0.1F, 1);
			ply.closeInventory();
			break;
		case COMPASS: // EXIT
			ply.kickPlayer("안녕히 가세요.");
		default:
			ply.closeInventory();
			break;
		}
		
		CustomItems.Reselector.setReselecting(ply, false);
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e)
	{
		Player ply = (Player) e.getPlayer();
		String invname = ChatColor.stripColor(e.getInventory().getName());
		
		if (invname.equals("클래스 선택") && !ClassManager.hasPlayerClass(ply))
			SimpleRPGMain.plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				public void run() {
					ply.sendMessage(ChatColor.RED + "클래스를 선택해주세요.");
					ClassSelector.showSelectorGUI(ply);
				}
			}, 2L);

		SimpleRPGMain.logger.info(Boolean.toString(CustomItems.Reselector.isReselecting(ply)));
		
		if (invname.equals("클래스 재선택") && CustomItems.Reselector.isReselecting(ply))
		{
			ply.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "직업 초기화권을 사용하지 않으셨군요. 다시 생각하고 사용해주세요!");
			CustomItems.Reselector.giveReselector(ply);
			CustomItems.Reselector.setReselecting(ply, false);
		}
	}
}
