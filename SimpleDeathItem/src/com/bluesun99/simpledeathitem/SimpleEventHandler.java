package com.bluesun99.simpledeathitem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SimpleEventHandler implements Listener {
	@EventHandler
	public void onPlayerDeath(EntityDeathEvent e)
	{
		// Player Only.
		if (!(e.getEntity() instanceof Player) || !(e.getEntity().getKiller() instanceof Player))
			return;
		
		// Declare victim, killer as Player.
		Player victim = (Player) e.getEntity();
		Player killer = e.getEntity().getKiller();
		
		// Both victim and killer must have enchantment. And 15% chance to get item.
		if (HaveEnchantmentOnAllEquipments(victim, Enchantment.PROTECTION_ENVIRONMENTAL, 4) &&
			HaveEnchantmentOnAllEquipments(killer, Enchantment.PROTECTION_ENVIRONMENTAL, 4) &&
			new java.util.Random().nextInt(100) < 15)
		{
			// One coal stuff.
			ItemStack customcoal = new ItemStack(Material.COAL);
			
			// Get coal stuff's item meta.
			ItemMeta im = customcoal.getItemMeta();
			
			// [ Psv ] [ ¾àÅ»ÄÚÀÎ  ]
			im.setDisplayName(
			ChatColor.WHITE + "[ " + ChatColor.YELLOW + "Psv" + ChatColor.WHITE + " ] " + 
			ChatColor.WHITE + "[ " + ChatColor.BLACK + "¾àÅ»ÄÚÀÎ" + ChatColor.WHITE + " ]");
			
			// Set coal stuff's item meta.
			customcoal.setItemMeta(im);
			
			// Add coal stuff to killer's inventory.
			killer.getInventory().addItem(customcoal);
			
			SimpleDeathItem.logger.info("Given " + killer.getName() + " 1 Coal Coin by killed " + victim.getName());
		}
	}
	
	public boolean HaveEnchantmentOnAllEquipments(Player ply, Enchantment ench, int level)
	{
		// All Equipments.
		ItemStack[] is = { ply.getEquipment().getHelmet(), ply.getEquipment().getChestplate(),
				ply.getEquipment().getLeggings(), ply.getEquipment().getBoots() };
		
		// If no enchantment there. Set Nope to true.
		boolean Nope = false;
		
		for (int i = 0; i < is.length; i++)
		{
			ItemStack equipment = is[i];
			if (equipment.getEnchantmentLevel(ench) < level)
			{
				Nope = true;
				break;
			}
		}
		
		return Nope;
		
	}
}
