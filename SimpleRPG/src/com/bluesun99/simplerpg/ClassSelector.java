package com.bluesun99.simplerpg;

import java.util.ArrayList;

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
		String title = !isReselect ? "클래스 선택" : "클래스 재선택";
		
		// Inventory with size 9 (An line).
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + title);
		
		/* VIKING CLASS SELECTOR */
		ItemStack viking = new ItemStack(Material.WOOD_SWORD);
		ItemMeta vikingmeta = viking.getItemMeta();
		vikingmeta.setDisplayName(ChatColor.GOLD + "바이킹");
		java.util.List<String> vikinglore = new ArrayList<String>();
		vikinglore.add("바이킹 직업군을 선택합니다.");
		vikinglore.add("");
		if (isReselect)
		{
			vikinglore.add(ChatColor.BOLD + "" + ChatColor.RED + "클릭과 동시에 직업이 초기화됩니다.");
			vikinglore.add(ChatColor.BOLD + "" + ChatColor.RED + "신중하게 선택 해주세요.");
		}
		else
			vikinglore.add(ChatColor.BOLD + "" + ChatColor.RED + "한 번 선택한 직업군은 되돌릴 수 없습니다.");
		vikingmeta.setLore(vikinglore);
		viking.setItemMeta(vikingmeta);
		
		/* ELF CLASS SELECTOR */
		ItemStack elf = new ItemStack(Material.BOW);
		ItemMeta elfmeta = elf.getItemMeta();
		elfmeta.setDisplayName(ChatColor.AQUA + "엘프");
		java.util.List<String> elflore = new ArrayList<String>();
		elflore.add("엘프 직업군을 선택합니다.");
		elflore.add("");
		if (isReselect)
		{
			elflore.add(ChatColor.BOLD + "" + ChatColor.RED + "클릭과 동시에 직업이 초기화됩니다.");
			elflore.add(ChatColor.BOLD + "" + ChatColor.RED + "신중하게 선택 해주세요.");
		}
		else
			elflore.add(ChatColor.BOLD + "" + ChatColor.RED + "한 번 선택한 직업군은 되돌릴 수 없습니다.");
		elfmeta.setLore(elflore);
		elf.setItemMeta(elfmeta);
		
		/* EXIT */
		ItemStack exit = new ItemStack(Material.COMPASS);
		ItemMeta exitmeta = exit.getItemMeta();
		exitmeta.setDisplayName(ChatColor.RED + "응 안해");
		java.util.List<String> exitlore = new ArrayList<String>();
		exitlore.add("선택도 하기 싫다!");
		exitlore.add("만사가 귀찮아! 잠깐 나가고 싶다!");
		exitlore.add("아 몰랑");
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
	
	static void selectClass(Player ply, RPGClass rc)
	{
		ClassManager.setPlayerClass(ply, rc);
		ClassManager.teleportToClassSpawn(ply);
		ClassManager.setEffectToPlayer(ply);
	}

	static void unselectClass(Player ply)
	{
		ClassManager.stripEffects(ply);
	}
}
