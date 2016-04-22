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

import net.md_5.bungee.api.ChatColor;

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
				ply.sendMessage("어드민 명령어는 어드민만 사용할 수 있습니다.");
				return;
			}
			
			ply.sendMessage("/sr give help - 아이템을 지급 커맨드 도움말을 봅니다.");
			ply.sendMessage("/sr clear help - 클래스 초기화 커맨드 도움말을 봅니다.");
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
			ply.sendMessage("당신의 클래스는 " +  rc.getClassColor() + rc.getKoreanName() + ChatColor.WHITE + "입니다.");
		}
	}
	
	private static class CommandClear
	{
		static void execute(Player ply, String... s)
		{
			if (!ply.hasPermission("simplerpg.admin"))
			{
				ply.sendMessage("어드민 명령어는 어드민만 사용할 수 있습니다.");
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
					p.kickPlayer("클래스 초기화 작업으로 인한 킥");
				}
			}
			else if (s[1] != null)
				for (Player p : SimpleRPGMain.plugin.getServer().getOnlinePlayers())
					if (p.getName().equalsIgnoreCase(s[1]))
					{
						ClassManager.setPlayerClass(p, RPGClass.NONE);
						p.kickPlayer("클래스 초기화 작업으로 인한 킥");
					}
					else
						ply.sendMessage("해당 유저를 찾을 수 없습니다.");
		}
		
		private static void sendHelp(Player ply)
		{
			ply.sendMessage("사용 가능한 명령어들");
			ply.sendMessage("/sr clear [닉네임] - 해당 유저의 클래스를 초기화합니다.");
			ply.sendMessage("/sr clear all - 접속한 모든 유저의 클래스를 초기화합니다.");
			ply.sendMessage("조심하십시오. 이 명령어는 초기화 후 유저를 킥합니다.");
			return;
		}
	}
	
	private static class CommandHelp
	{
		static void execute(CommandSender sender)
		{
			if (sender instanceof ConsoleCommandSender)
				SimpleRPGMain.logger.info("콘솔에서는 사용할 수 없습니다.");
			
			if (sender instanceof Player)
				sendDefaultMessage((Player) sender);
		}
		
		static void sendDefaultMessage(Player ply)
		{
			ply.sendMessage("/sr class - 자신의 클래스를 확인합니다.");
			ply.sendMessage("/sr admin - 어드민 명령어를 봅니다.");
		}
	}
	
	private static class CommandGive
	{
		static void execute(Player ply, String... s)
		{
			if (!ply.hasPermission("simplerpg.admin"))
			{
				ply.sendMessage("어드민 명령어는 어드민만 사용할 수 있습니다.");
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
							ply.sendMessage("해당 유저를 찾을 수 없습니다.");
						}
				}
				else
				{
					sendHelp(ply);
				}
		}
		
		private static void sendHelp(Player ply)
		{
			ply.sendMessage("사용 가능한 명령어들");
			for (Class<?> c : CustomItems.class.getDeclaredClasses())
				try {
					ply.sendMessage("/sr give " + c.getSimpleName() + " [닉네임] - " + (String)c.getDeclaredField("desc").get(null));
				} catch (Exception e) {}
			return;
		}
	}
}
