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
			logger.severe("Vault �÷������� ã�� ���߽��ϴ�. �÷������� �ߴ��մϴ�.");
			return false;
		}
		
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null || rsp.getProvider() == null) {
			logger.severe("Economy ȣȯ �÷������� ã�� ���߽��ϴ�. �÷������� �ߴ��մϴ�.");
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
			SimpleRPGMain.logger.info("������ " + ply.getName() + "�� Ŭ������ " + rc.getKoreanName() + "�Դϴ�.");
			ClassManager.setEffectToPlayer(ply);
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e)
	{
		Player ply = e.getPlayer();
		RPGClass rc = ClassManager.getPlayerClass(ply);
		
		ply.sendMessage(rc.getClassColor() + rc.getKoreanName() + ChatColor.WHITE + "�� �´� ������ ���޹ް� ���� ��ҷ� �ڷ���Ʈ �Ǿ����ϴ�.");
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
		
		if (invname.equals("Ŭ���� �缱��"))
			CustomItems.Reselector.setReselecting(ply, true);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e)
	{
		String invname = ChatColor.stripColor(e.getInventory().getName());
		
		if (!invname.equals("Ŭ���� ����") && !invname.equals("Ŭ���� �缱��"))
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
			ply.sendMessage(ChatColor.GOLD + "����ŷ" + ChatColor.WHITE + " Ŭ������ �����ϼ̽��ϴ�.");
			ClassSelector.selectClass(ply, RPGClass.VIKING);
			ply.playSound(ply.getLocation(), Sound.BLOCK_DISPENSER_LAUNCH, 0.1F, 1);
			ply.closeInventory();
			break;
		case BOW: // ELF
			ply.sendMessage(ChatColor.AQUA + "����" + ChatColor.WHITE + " Ŭ������ �����ϼ̽��ϴ�.");
			ClassSelector.selectClass(ply, RPGClass.ELF);
			ply.playSound(ply.getLocation(), Sound.BLOCK_DISPENSER_LAUNCH, 0.1F, 1);
			ply.closeInventory();
			break;
		case COMPASS: // EXIT
			ply.kickPlayer("�ȳ��� ������.");
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
		
		if (invname.equals("Ŭ���� ����") && !ClassManager.hasPlayerClass(ply))
			SimpleRPGMain.plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				public void run() {
					ply.sendMessage(ChatColor.RED + "Ŭ������ �������ּ���.");
					ClassSelector.showSelectorGUI(ply);
				}
			}, 2L);

		SimpleRPGMain.logger.info(Boolean.toString(CustomItems.Reselector.isReselecting(ply)));
		
		if (invname.equals("Ŭ���� �缱��") && CustomItems.Reselector.isReselecting(ply))
		{
			ply.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "���� �ʱ�ȭ���� ������� �����̱���. �ٽ� �����ϰ� ������ּ���!");
			CustomItems.Reselector.giveReselector(ply);
			CustomItems.Reselector.setReselecting(ply, false);
		}
	}
}
