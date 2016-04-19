package com.bluesun99.simplerpg;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args)
	{
		if (!arg.equalsIgnoreCase("simplerpg") || !arg.equalsIgnoreCase("sr") || !(sender instanceof Player))
			return true;
		
		Player ply = (Player) sender;
		String subcommand = "";
		
		for (int i = 0; i < args.length; i++)
		{
			subcommand += args[i];
			if (i != (args.length - 1))
				subcommand += " ";
		}
		
		switch (subcommand) {
		case "admin":
			CommandAdmin.execute(ply);
		case "help":
		case "도움말":
		default:
			CommandHelp.execute(ply);
			break;
		}
		
		return true;
	}
	
	private static class CommandAdmin
	{
		static void execute(Player ply)
		{
			sendDefaultMessage(ply);
		}
		
		static void sendDefaultMessage(Player ply)
		{
			if (ply.hasPermission("simplerpg.admin"))
				ply.sendMessage("어드민 명령어는 어드민만 사용할 수 있습니다.");
			else
			{
				ply.sendMessage("/simplerpg give reselector - 해당 유저에게 직업 초기화권을 지급합니다.");
			}
		}
	}
	
	private static class CommandHelp
	{
		static void execute(Player ply)
		{
			
		}
	}
}
