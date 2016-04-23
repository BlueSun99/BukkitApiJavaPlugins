package com.bluesun99.simplerpg;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.bluesun99.simplerpg.ClassManager.RPGClass;

public class CommandManager implements CommandExecutor {
	private static List<String> allowedCommands = Arrays.asList("simplerpg", "sr");
	
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args)
	{
		if (!allowedCommands.contains(arg))
			return true;
		
		if (args.length == 0 || !(sender instanceof Player))
		{
			CommandHelp.execute(sender);
			return true;
		}
		
		Player ply = (Player) sender;
		
		/*String subcommand = "";
		
		for (int i = 0; i < args.length; i++)
		{
			subcommand += args[i];
			if (i != (args.length - 1))
				subcommand += " ";
		}*/
		
		if (args[0].equalsIgnoreCase("give"))
			CommandGive.execute(ply, args);
		else if (args[0].equalsIgnoreCase("admin"))
			CommandAdmin.execute(ply);
		else if (args[0].equalsIgnoreCase("class"))
			CommandClass.execute(ply);
		else if (args[0].equalsIgnoreCase("clear"))
			CommandClear.execute(ply, args);
		
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
			if (!ply.hasPermission("simplerpg.admin"))
			{
				ply.sendMessage(SimpleRPGMain.lm.getString("srt_admin_only"));
				return;
			}
			
			ply.sendMessage(SimpleRPGMain.lm.format("srt_commandhelp", "give"));
			ply.sendMessage(SimpleRPGMain.lm.format("srt_commandhelp", "clear"));
		}
	}
	
	private static class CommandClass
	{
		static void execute(Player ply)
		{
			sendDefaultMessage(ply);
		}
		
		static void sendDefaultMessage(Player ply)
		{
			RPGClass rc = ClassManager.getPlayerClass(ply);
			ply.sendMessage(SimpleRPGMain.lm.format("srt_check_class", rc.getFriendlyNameWithColor()));
		}
	}
	
	private static class CommandClear
	{
		static void execute(Player ply, String... s)
		{
			if (!ply.hasPermission("simplerpg.admin"))
			{
				ply.sendMessage(SimpleRPGMain.lm.getString("srt_admin_only"));
				return;
			}
			
			if (s.length != 2 || s[1].equalsIgnoreCase("help"))
			{
				sendHelp(ply);
				return;
			}
			
			if (s[1].equals("all"))
			{
				/*for (OfflinePlayer p : SimpleRPGMain.plugin.getServer().getOfflinePlayers())
					ClassManager.setPlayerClass(p.getPlayer(), RPGClass.NONE);*/
				for (Player p : SimpleRPGMain.plugin.getServer().getOnlinePlayers())
				{
					ClassManager.setPlayerClass(p, RPGClass.NONE);
					p.kickPlayer(SimpleRPGMain.lm.getString("srt_kick_due_to_rechoose"));
				}
			}
			else if (s[1] != null)
				for (Player p : SimpleRPGMain.plugin.getServer().getOnlinePlayers())
					if (p.getName().equalsIgnoreCase(s[1]))
					{
						ClassManager.setPlayerClass(p, RPGClass.NONE);
						p.kickPlayer(SimpleRPGMain.lm.getString("srt_kick_due_to_rechoose"));
					}
					else
						ply.sendMessage(SimpleRPGMain.lm.getString("srt_cannot_find"));
		}
		
		private static void sendHelp(Player ply)
		{
			ply.sendMessage(SimpleRPGMain.lm.getString("srt_usable_commands"));
			ply.sendMessage(SimpleRPGMain.lm.getString("srt_command_clear_usage"));
					/*"/sr clear [닉네임] - 해당 유저의 클래스를 초기화합니다.");
			ply.sendMessage("/sr clear all - 접속한 모든 유저의 클래스를 초기화합니다.");*/
			return;
		}
	}
	
	private static class CommandHelp
	{
		static void execute(CommandSender sender)
		{
			if (sender instanceof ConsoleCommandSender)
				SimpleRPGMain.logger.info(SimpleRPGMain.lm.getString("srt_cannot_use_on_console"));
			
			if (sender instanceof Player)
				sendDefaultMessage((Player) sender);
		}
		
		static void sendDefaultMessage(Player ply)
		{
			ply.sendMessage(SimpleRPGMain.lm.getString("srt_command_class_usage"));
			ply.sendMessage(SimpleRPGMain.lm.getString("srt_command_admin_usage"));
		}
	}
	
	private static class CommandGive
	{
		static void execute(Player ply, String... s)
		{
			if (!ply.hasPermission("simplerpg.admin"))
			{
				ply.sendMessage(SimpleRPGMain.lm.getString("srt_admin_only"));
				return;
			}
			
			if (s.length <= 2 || s.length >= 4 || s[1].equalsIgnoreCase("help"))
			{
				sendHelp(ply);
				return;
			}
			
			for (Class<?> c : CustomItems.class.getDeclaredClasses())
				if (c.getSimpleName().equalsIgnoreCase(s[1]))
				{
					for (Player p : SimpleRPGMain.plugin.getServer().getOnlinePlayers())
						if (p.getName().equalsIgnoreCase(s[2]))
						{
							for (Method m : c.getDeclaredMethods())
								if (m.getName() == "give")
									try {
										m.invoke(null, ply);
									} catch (Exception e) {}
						}
						else
						{
							ply.sendMessage(SimpleRPGMain.lm.getString("srt_cannot_find"));
						}
				}
				else
				{
					sendHelp(ply);
				}
		}
		
		private static void sendHelp(Player ply)
		{
			ply.sendMessage(SimpleRPGMain.lm.getString("srt_usable_commands"));
			for (Class<?> c : CustomItems.class.getDeclaredClasses())
				try {
					ply.sendMessage(SimpleRPGMain.lm.format("srt_command_give_usage", c.getSimpleName(), (String)c.getDeclaredField("desc").get(null)));
				} catch (Exception e) {}
			return;
		}
	}
}
