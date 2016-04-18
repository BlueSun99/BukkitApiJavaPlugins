package com.bluesun99.simplerpg;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args)
	{
		if (sender instanceof Player)
			CustomItems.Reselector.giveReselector((Player) sender); 
		
		return true;
	}
}
