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
				ply.sendMessage("���� ��ɾ�� ���θ� ����� �� �ֽ��ϴ�.");
				return;
			}
			
			ply.sendMessage("/sr give help - �������� ���� Ŀ�ǵ� ������ ���ϴ�.");
			ply.sendMessage("/sr clear help - Ŭ���� �ʱ�ȭ Ŀ�ǵ� ������ ���ϴ�.");
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
			ply.sendMessage("����� Ŭ������ " +  rc.getClassColor() + rc.getKoreanName() + ChatColor.WHITE + "�Դϴ�.");
		}
	}
	
	private static class CommandClear
	{
		static void execute(Player ply, String... s)
		{
			if (!ply.hasPermission("simplerpg.admin"))
			{
				ply.sendMessage("���� ��ɾ�� ���θ� ����� �� �ֽ��ϴ�.");
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
					p.kickPlayer("Ŭ���� �ʱ�ȭ �۾����� ���� ű");
				}
			}
			else if (s[1] != null)
				for (Player p : SimpleRPGMain.plugin.getServer().getOnlinePlayers())
					if (p.getName().equalsIgnoreCase(s[1]))
					{
						ClassManager.setPlayerClass(p, RPGClass.NONE);
						p.kickPlayer("Ŭ���� �ʱ�ȭ �۾����� ���� ű");
					}
					else
						ply.sendMessage("�ش� ������ ã�� �� �����ϴ�.");
		}
		
		private static void sendHelp(Player ply)
		{
			ply.sendMessage("��� ������ ��ɾ��");
			ply.sendMessage("/sr clear [�г���] - �ش� ������ Ŭ������ �ʱ�ȭ�մϴ�.");
			ply.sendMessage("/sr clear all - ������ ��� ������ Ŭ������ �ʱ�ȭ�մϴ�.");
			ply.sendMessage("�����Ͻʽÿ�. �� ��ɾ�� �ʱ�ȭ �� ������ ű�մϴ�.");
			return;
		}
	}
	
	private static class CommandHelp
	{
		static void execute(CommandSender sender)
		{
			if (sender instanceof ConsoleCommandSender)
				SimpleRPGMain.logger.info("�ֿܼ����� ����� �� �����ϴ�.");
			
			if (sender instanceof Player)
				sendDefaultMessage((Player) sender);
		}
		
		static void sendDefaultMessage(Player ply)
		{
			ply.sendMessage("/sr class - �ڽ��� Ŭ������ Ȯ���մϴ�.");
			ply.sendMessage("/sr admin - ���� ��ɾ ���ϴ�.");
		}
	}
	
	private static class CommandGive
	{
		static void execute(Player ply, String... s)
		{
			if (!ply.hasPermission("simplerpg.admin"))
			{
				ply.sendMessage("���� ��ɾ�� ���θ� ����� �� �ֽ��ϴ�.");
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
							ply.sendMessage("�ش� ������ ã�� �� �����ϴ�.");
						}
				}
				else
				{
					sendHelp(ply);
				}
		}
		
		private static void sendHelp(Player ply)
		{
			ply.sendMessage("��� ������ ��ɾ��");
			for (Class<?> c : CustomItems.class.getDeclaredClasses())
				try {
					ply.sendMessage("/sr give " + c.getSimpleName() + " [�г���] - " + (String)c.getDeclaredField("desc").get(null));
				} catch (Exception e) {}
			return;
		}
	}
}
