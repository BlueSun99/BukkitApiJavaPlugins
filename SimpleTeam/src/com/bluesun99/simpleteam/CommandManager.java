package com.bluesun99.simpleteam;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.bluesun99.simpleteam.TeamManager.TEAMS;

public class CommandManager implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command arg0, String arg1, String[] arg2)
	{
		/*if (!(sender instanceof ConsoleCommandSender) && !(sender instanceof Player))
			return true;*/
		
		ParseSubcommand(sender, arg2);
		
		return true;
	}
	
	private void SendMessage(CommandSender target, String msg)
	{
		if (target instanceof ConsoleCommandSender)
			SimpleTeam.logger.info(msg);
		else if (target instanceof Player)
			((Player) target).sendMessage(msg);
	}
	
	private void SendMessage(CommandSender target, String[] msgs)
	{
		if (target instanceof ConsoleCommandSender)
			for (int i = 0; i < msgs.length; i++)
				SimpleTeam.logger.info(msgs[i]);
		else if (target instanceof Player)
			for (int i = 0; i < msgs.length; i++)
				((Player) target).sendMessage(msgs[i]);
	}
	
	private void ParseSubcommand(CommandSender sender, String[] subcommands)
	{
		String subcommand;

		try {
			subcommand = subcommands[0];
		} catch (Exception e) { subcommand = "";}

		Player ply = (Player) sender;;
		
		switch (subcommand) {
		case "?":
			SendMessage(sender, getHelpMsg());
			break;
		case "����":
			if (hasAdminRights(sender))
				SendMessage(sender, getAdminHelpMsg());
			else
				SendMessage(sender, "���θ� ��� ������ ��ɾ��Դϴ�.");
			break;
		case "���":
			TEAMS team = TEAMS.getByKoreanName(subcommands[1]);
			
			if (team == TEAMS.NONE)
				ply.sendMessage("�߸��� �� �̸� �Դϴ�.");
			else
				TeamManager.joinTeam((Player) sender, team);
			break;
		case "Ż��":
			TeamManager.leaveTeam(ply);
			break;
		case "ä��":
			TeamManager.broadcastTeamMessage(ply, subcommands[1]);
			break;
		case "Ƽ��":
			if (!hasAdminRights(sender))
				break;
			
			if (subcommands.length > 5)
				ply.sendMessage("�μ��� �ʹ� �����ϴ�.");
			else if (subcommands.length > 2)
				try {
					TeamManager.teleportTeam(ply, TEAMS.getByKoreanName(subcommands[1]),
							new Location(ply.getWorld(), Double.parseDouble(subcommands[2]),
									Double.parseDouble(subcommands[3]), Double.parseDouble(subcommands[4])));
				} catch (Exception e) {
					ply.sendMessage("�μ��� �м��ϴµ� ������ �߻��߽��ϴ�.");
				}
			else
				TeamManager.teleportTeam(ply, TEAMS.getByKoreanName(subcommands[1]), ply.getLocation());
			break;
		/*case "ų":
			
			break;*/
		default:
			SendMessage(sender, getNoCommandMsg());
		}
	}
	
	private String[] getHelpMsg()
	{
		return new String[] {
				ChatColor.WHITE + "����: ",
				
				ChatColor.WHITE + "/�� ��� [" +
				ChatColor.RED + "����" + ChatColor.WHITE + ", " +
				ChatColor.BLUE + "�Ķ�" + ChatColor.WHITE + ", " +
				ChatColor.YELLOW + "���" + ChatColor.WHITE + ", " +
				ChatColor.GREEN + "�ʷ�" + ChatColor.WHITE + "] - ���� �����մϴ�.",

				ChatColor.WHITE + "/�� ��� [" +
				ChatColor.RED + "����" + ChatColor.WHITE + ", " +
				ChatColor.BLUE + "���" + ChatColor.WHITE + ", " +
				ChatColor.YELLOW + "����" + ChatColor.WHITE + ", " +
				ChatColor.GREEN + "�׸�" + ChatColor.WHITE + "] - ���� �����ϴ�.",
				
				ChatColor.WHITE + "/�� Ż�� - ������ ���ɴϴ�.",
				
				ChatColor.WHITE + "/�� ä�� [�� ��] - ������ ä���մϴ�.",
				
				/* ChatColor.WHITE + "/�� ��� - ���� ����� ���ϴ�.", */

				ChatColor.WHITE + "/�� ���� - ���� ��ɾ ���ϴ�.",
		};
	}
	
	public static String getNoCommandMsg()
	{
		return "������ \"/�� ?\"�� �Է� �� �ּ���.";
	}
	
	private String[] getAdminHelpMsg()
	{
		return new String[] {
				ChatColor.WHITE + "/�� Ƽ�� [" +
				ChatColor.RED + "����" + ChatColor.WHITE + ", " +
				ChatColor.BLUE + "�Ķ�" + ChatColor.WHITE + ", " +
				ChatColor.YELLOW + "���" + ChatColor.WHITE + ", " +
				ChatColor.GREEN + "�ʷ�" + ChatColor.WHITE + "] - �ڽ� ��ġ�� �ش� ���� ���� �����̵���ŵ�ϴ�.",

				ChatColor.WHITE + "/�� Ƽ�� [" +
				ChatColor.RED + "����" + ChatColor.WHITE + ", " +
				ChatColor.BLUE + "���" + ChatColor.WHITE + ", " +
				ChatColor.YELLOW + "����" + ChatColor.WHITE + ", " +
				ChatColor.GREEN + "�׸�" + ChatColor.WHITE + "] - ���� �����ϴ�.",
				
				ChatColor.WHITE + "/�� Ƽ�� [" +
				ChatColor.RED + "����" + ChatColor.WHITE + ", " +
				ChatColor.BLUE + "�Ķ�" + ChatColor.WHITE + ", " +
				ChatColor.YELLOW + "���" + ChatColor.WHITE + ", " +
				ChatColor.GREEN + "�ʷ�" + ChatColor.WHITE + "] " + 
				ChatColor.UNDERLINE + "[��ǥ]" + " - ��ǥ�� �ش� ���� ���� �����̵� ��ŵ�ϴ�.",
				
				ChatColor.WHITE + "/�� Ƽ�� [" +
				ChatColor.RED + "����" + ChatColor.WHITE + ", " +
				ChatColor.BLUE + "�Ķ�" + ChatColor.WHITE + ", " +
				ChatColor.YELLOW + "���" + ChatColor.WHITE + ", " +
				ChatColor.GREEN + "�ʷ�" + ChatColor.WHITE + "] " +
				ChatColor.UNDERLINE + "[��ǥ]" + " - ���� �����ϴ�.",
				
				/* WORK IN PROGRESS */
				/*
				ChatColor.WHITE + "/�� ų [" +
				ChatColor.RED + "����" + ChatColor.WHITE + ", " +
				ChatColor.BLUE + "�Ķ�" + ChatColor.WHITE + ", " +
				ChatColor.YELLOW + "���" + ChatColor.WHITE + ", " +
				ChatColor.GREEN + "�ʷ�" + ChatColor.WHITE + "] - �ش� ���� ���� ���Դϴ�.",

				ChatColor.WHITE + "/�� ų [" +
				ChatColor.RED + "����" + ChatColor.WHITE + ", " +
				ChatColor.BLUE + "���" + ChatColor.WHITE + ", " +
				ChatColor.YELLOW + "����" + ChatColor.WHITE + ", " +
				ChatColor.GREEN + "�׸�" + ChatColor.WHITE + "] - ���� �����ϴ�.",
				
				ChatColor.WHITE + "/�� ��ü��� - ��ü ���� ������ ���ϴ�.",
				
				ChatColor.WHITE + "/�� üũ [�г���] - �ش� ������ ���� Ȯ���մϴ�.",
				*/
		};
	}
	
	private boolean hasAdminRights(CommandSender sender)
	{
		if (((sender instanceof Player) && ((Player) sender).isOp() || ((Player) sender).hasPermission("simpleteam.admin")) || sender instanceof ConsoleCommandSender)
			return true;
		
		return false;
	}
}
