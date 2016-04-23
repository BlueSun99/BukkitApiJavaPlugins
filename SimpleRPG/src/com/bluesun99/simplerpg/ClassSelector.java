package com.bluesun99.simplerpg;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bluesun99.simplerpg.ClassManager.RPGClass;

public class ClassSelector {

	static void showSelectorGUI(Player ply)
	{
		ClassSelector.showSelectorGUI(ply, false);
	}
	
	static void showSelectorGUI(Player ply, boolean isReselect)
	{
		String title = !isReselect
				? SimpleRPGMain.lm.getString("srt_class_choose")
						: SimpleRPGMain.lm.getString("srt_class_rechoose");
		
		// Inventory with size 9 (An line).
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + title);
		
		/* VIKING CLASS SELECTOR */
		ItemStack viking = new ItemStack(Material.WOOD_SWORD);
		ItemMeta vikingmeta = viking.getItemMeta();
		vikingmeta.setDisplayName(RPGClass.VIKING.getFriendlyNameWithColor());
		vikingmeta.setLore(getLore(RPGClass.VIKING, isReselect));
		viking.setItemMeta(vikingmeta);
		
		/* ELF CLASS SELECTOR */
		ItemStack elf = new ItemStack(Material.BOW);
		ItemMeta elfmeta = elf.getItemMeta();
		elfmeta.setDisplayName(RPGClass.ELF.getFriendlyNameWithColor());
		elfmeta.setLore(getLore(RPGClass.ELF, isReselect));
		elf.setItemMeta(elfmeta);
		
		/* EXIT */
		ItemStack exit = new ItemStack(Material.COMPASS);
		ItemMeta exitmeta = exit.getItemMeta();
		exitmeta.setDisplayName(ChatColor.RED + SimpleRPGMain.lm.getString("srt_exit_name"));
		List<String> exitlore = new ArrayList<String>();
		exitlore.add(SimpleRPGMain.lm.getString("srt_desc_info_exit1"));
		exitlore.add(SimpleRPGMain.lm.getString("srt_desc_info_exit2"));
		exitlore.add(SimpleRPGMain.lm.getString("srt_desc_info_exit3"));
		exitmeta.setLore(exitlore);
		exit.setItemMeta(exitmeta);
		
		// 012345678
		inv.setItem(0, viking);
		inv.setItem(8, elf);
		if (!isReselect)
			inv.setItem(4, exit);
		
		// Open Inventory
		ply.openInventory(inv);
	}
	
	static List<String> getLore(RPGClass rc, boolean isReselect)
	{
		List<String> lore = new ArrayList<String>();
		lore.add(SimpleRPGMain.lm.format("srt_choose_as_your_class", rc.getFriendlyNameWithColor()));
		lore.add("");
		if (isReselect)
		{
			lore.add(ChatColor.BOLD + "" + ChatColor.RED + SimpleRPGMain.lm.getString("srt_desc_info_rechoose1"));
			lore.add(ChatColor.BOLD + "" + ChatColor.RED + SimpleRPGMain.lm.getString("srt_desc_info_rechoose2"));
		}
		else
			lore.add(ChatColor.BOLD + "" + ChatColor.RED + SimpleRPGMain.lm.getString("srt_desc_info"));
		return lore;
	}
	
	static void selectClass(Player ply, RPGClass rc)
	{
		unselectClass(ply);
		ClassManager.setPlayerClass(ply, rc);
		ClassManager.teleportToClassSpawn(ply);
		ClassManager.setEffectToPlayer(ply);
	}

	static void unselectClass(Player ply)
	{
		ClassManager.stripEffects(ply);
		ClassManager.setPlayerClass(ply, RPGClass.NONE);
	}
}
