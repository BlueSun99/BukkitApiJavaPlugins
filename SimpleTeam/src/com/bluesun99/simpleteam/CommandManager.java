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
		case "어드민":
			if (hasAdminRights(sender))
				SendMessage(sender, getAdminHelpMsg());
			else
				SendMessage(sender, "어드민만 사용 가능한 명령어입니다.");
			break;
		case "등록":
			TEAMS team = TEAMS.getByKoreanName(subcommands[1]);
			
			if (team == TEAMS.NONE)
				ply.sendMessage("잘못된 팀 이름 입니다.");
			else
				TeamManager.joinTeam((Player) sender, team);
			break;
		case "탈퇴":
			TeamManager.leaveTeam(ply);
			break;
		case "채팅":
			TeamManager.broadcastTeamMessage(ply, subcommands[1]);
			break;
		case "티피":
			if (!hasAdminRights(sender))
				break;
			
			if (subcommands.length > 5)
				ply.sendMessage("인수가 너무 많습니다.");
			else if (subcommands.length > 2)
				try {
					TeamManager.teleportTeam(ply, TEAMS.getByKoreanName(subcommands[1]),
							new Location(ply.getWorld(), Double.parseDouble(subcommands[2]),
									Double.parseDouble(subcommands[3]), Double.parseDouble(subcommands[4])));
				} catch (Exception e) {
					ply.sendMessage("인수를 분석하는데 오류가 발생했습니다.");
				}
			else
				TeamManager.teleportTeam(ply, TEAMS.getByKoreanName(subcommands[1]), ply.getLocation());
			break;
		/*case "킬":
			
			break;*/
		default:
			SendMessage(sender, getNoCommandMsg());
		}
	}
	
	private String[] getHelpMsg()
	{
		return new String[] {
				ChatColor.WHITE + "사용법: ",
				
				ChatColor.WHITE + "/팀 등록 [" +
				ChatColor.RED + "빨강" + ChatColor.WHITE + ", " +
				ChatColor.BLUE + "파랑" + ChatColor.WHITE + ", " +
				ChatColor.YELLOW + "노랑" + ChatColor.WHITE + ", " +
				ChatColor.GREEN + "초록" + ChatColor.WHITE + "] - 팀에 가입합니다.",

				ChatColor.WHITE + "/팀 등록 [" +
				ChatColor.RED + "레드" + ChatColor.WHITE + ", " +
				ChatColor.BLUE + "블루" + ChatColor.WHITE + ", " +
				ChatColor.YELLOW + "옐로" + ChatColor.WHITE + ", " +
				ChatColor.GREEN + "그린" + ChatColor.WHITE + "] - 위와 같습니다.",
				
				ChatColor.WHITE + "/팀 탈퇴 - 팀에서 나옵니다.",
				
				ChatColor.WHITE + "/팀 채팅 [할 말] - 팀에게 채팅합니다.",
				
				/* ChatColor.WHITE + "/팀 목록 - 팀원 목록을 봅니다.", */

				ChatColor.WHITE + "/팀 어드민 - 어드민 명령어를 봅니다.",
		};
	}
	
	public static String getNoCommandMsg()
	{
		return "사용법은 \"/팀 ?\"을 입력 해 주세요.";
	}
	
	private String[] getAdminHelpMsg()
	{
		return new String[] {
				ChatColor.WHITE + "/팀 티피 [" +
				ChatColor.RED + "빨강" + ChatColor.WHITE + ", " +
				ChatColor.BLUE + "파랑" + ChatColor.WHITE + ", " +
				ChatColor.YELLOW + "노랑" + ChatColor.WHITE + ", " +
				ChatColor.GREEN + "초록" + ChatColor.WHITE + "] - 자신 위치로 해당 팀을 전부 순간이동시킵니다.",

				ChatColor.WHITE + "/팀 티피 [" +
				ChatColor.RED + "레드" + ChatColor.WHITE + ", " +
				ChatColor.BLUE + "블루" + ChatColor.WHITE + ", " +
				ChatColor.YELLOW + "옐로" + ChatColor.WHITE + ", " +
				ChatColor.GREEN + "그린" + ChatColor.WHITE + "] - 위와 같습니다.",
				
				ChatColor.WHITE + "/팀 티피 [" +
				ChatColor.RED + "빨강" + ChatColor.WHITE + ", " +
				ChatColor.BLUE + "파랑" + ChatColor.WHITE + ", " +
				ChatColor.YELLOW + "노랑" + ChatColor.WHITE + ", " +
				ChatColor.GREEN + "초록" + ChatColor.WHITE + "] " + 
				ChatColor.UNDERLINE + "[좌표]" + " - 좌표로 해당 팀을 전부 순간이동 시킵니다.",
				
				ChatColor.WHITE + "/팀 티피 [" +
				ChatColor.RED + "빨강" + ChatColor.WHITE + ", " +
				ChatColor.BLUE + "파랑" + ChatColor.WHITE + ", " +
				ChatColor.YELLOW + "노랑" + ChatColor.WHITE + ", " +
				ChatColor.GREEN + "초록" + ChatColor.WHITE + "] " +
				ChatColor.UNDERLINE + "[좌표]" + " - 위와 같습니다.",
				
				/* WORK IN PROGRESS */
				/*
				ChatColor.WHITE + "/팀 킬 [" +
				ChatColor.RED + "빨강" + ChatColor.WHITE + ", " +
				ChatColor.BLUE + "파랑" + ChatColor.WHITE + ", " +
				ChatColor.YELLOW + "노랑" + ChatColor.WHITE + ", " +
				ChatColor.GREEN + "초록" + ChatColor.WHITE + "] - 해당 팀을 전부 죽입니다.",

				ChatColor.WHITE + "/팀 킬 [" +
				ChatColor.RED + "레드" + ChatColor.WHITE + ", " +
				ChatColor.BLUE + "블루" + ChatColor.WHITE + ", " +
				ChatColor.YELLOW + "옐로" + ChatColor.WHITE + ", " +
				ChatColor.GREEN + "그린" + ChatColor.WHITE + "] - 위와 같습니다.",
				
				ChatColor.WHITE + "/팀 전체목록 - 전체 팀의 팀원을 봅니다.",
				
				ChatColor.WHITE + "/팀 체크 [닉네임] - 해당 유저의 팀을 확인합니다.",
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
