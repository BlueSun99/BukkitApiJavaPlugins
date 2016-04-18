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
		String title = !isReselect ? "Ŭ���� ����" : "Ŭ���� �缱��";
		
		// Inventory with size 9 (An line).
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + title);
		
		/* VIKING CLASS SELECTOR */
		ItemStack viking = new ItemStack(Material.WOOD_SWORD);
		ItemMeta vikingmeta = viking.getItemMeta();
		vikingmeta.setDisplayName(ChatColor.GOLD + "����ŷ");
		java.util.List<String> vikinglore = new ArrayList<String>();
		vikinglore.add("����ŷ �������� �����մϴ�.");
		vikinglore.add("");
		if (isReselect)
		{
			vikinglore.add(ChatColor.BOLD + "" + ChatColor.RED + "Ŭ���� ���ÿ� ������ �ʱ�ȭ�˴ϴ�.");
			vikinglore.add(ChatColor.BOLD + "" + ChatColor.RED + "�����ϰ� ���� ���ּ���.");
		}
		else
			vikinglore.add(ChatColor.BOLD + "" + ChatColor.RED + "�� �� ������ �������� �ǵ��� �� �����ϴ�.");
		vikingmeta.setLore(vikinglore);
		viking.setItemMeta(vikingmeta);
		
		/* ELF CLASS SELECTOR */
		ItemStack elf = new ItemStack(Material.BOW);
		ItemMeta elfmeta = elf.getItemMeta();
		elfmeta.setDisplayName(ChatColor.AQUA + "����");
		java.util.List<String> elflore = new ArrayList<String>();
		elflore.add("���� �������� �����մϴ�.");
		elflore.add("");
		if (isReselect)
		{
			elflore.add(ChatColor.BOLD + "" + ChatColor.RED + "Ŭ���� ���ÿ� ������ �ʱ�ȭ�˴ϴ�.");
			elflore.add(ChatColor.BOLD + "" + ChatColor.RED + "�����ϰ� ���� ���ּ���.");
		}
		else
			elflore.add(ChatColor.BOLD + "" + ChatColor.RED + "�� �� ������ �������� �ǵ��� �� �����ϴ�.");
		elfmeta.setLore(elflore);
		elf.setItemMeta(elfmeta);
		
		/* EXIT */
		ItemStack exit = new ItemStack(Material.COMPASS);
		ItemMeta exitmeta = exit.getItemMeta();
		exitmeta.setDisplayName(ChatColor.RED + "�� ����");
		java.util.List<String> exitlore = new ArrayList<String>();
		exitlore.add("���õ� �ϱ� �ȴ�!");
		exitlore.add("���簡 ������! ��� ������ �ʹ�!");
		exitlore.add("�� ����");
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
