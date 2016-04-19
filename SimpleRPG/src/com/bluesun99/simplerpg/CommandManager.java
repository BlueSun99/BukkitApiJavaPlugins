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
		case "����":
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
				ply.sendMessage("���� ��ɾ�� ���θ� ����� �� �ֽ��ϴ�.");
			else
			{
				ply.sendMessage("/simplerpg give reselector - �ش� �������� ���� �ʱ�ȭ���� �����մϴ�.");
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
