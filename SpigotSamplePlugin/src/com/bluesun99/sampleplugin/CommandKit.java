package com.bluesun99.sampleplugin;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class CommandKit implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender cs, Command arg0, String arg1, String[] arg2)
	{
		if (cs instanceof Player) {
			Player player = (Player) cs;
			
			ItemStack diamond = new ItemStack(Material.DIAMOND);
			
			ItemStack bricks = new ItemStack(Material.BRICK);
			
			bricks.setAmount(20);
			
			player.getInventory().addItem(diamond, bricks);
		}
		
		return true;
	}
}
