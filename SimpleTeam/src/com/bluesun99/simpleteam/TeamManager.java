package com.bluesun99.simpleteam;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class TeamManager {
	enum TEAMS {
		NONE(0),
		RED(1),
		BLUE(2),
		YELLOW(3),
		GREEN(4);
		
		private final int id;
		
		TEAMS(int id)
		{
			this.id = id;
		}
		
		public static TEAMS getById(int id)
		{
			return TEAMS.values()[id];
		}
		
		public int getId()
		{
			return this.id;
		}
		
		public static ChatColor getTeamColor(TEAMS team)
		{
			switch (team) {
			case RED:
				return ChatColor.RED;
			case BLUE:
				return ChatColor.BLUE;
			case YELLOW:
				return ChatColor.YELLOW;
			case GREEN:
				return ChatColor.GREEN;
			default:
				return ChatColor.WHITE;
			}
		}
		
		public static TEAMS getByKoreanName(String name)
		{
			if (name == null)
				return NONE;
			
			switch (name) {
			case "빨강":
			case "레드":
				return RED;
			case "파랑":
			case "블루":
				return BLUE;
			case "노랑":
			case "옐로":
				return YELLOW;
			case "초록":
			case "그린":
				return GREEN;
			default:
				return NONE;
			}
		}
		
		
		public static String getName(TEAMS team)
		{
			return team.name();
		}
		
		public static String getKoreanName(TEAMS team)
		{
			switch (team) {
			case RED:
				return "빨강, 레드";
			case BLUE:
				return "파랑, 블루";
			case YELLOW:
				return "노랑, 옐로";
			case GREEN:
				return "초록, 그린";
			default:
				return "없음";
			}
		}
	}
	
	public static void joinTeam(Player ply, TEAMS team)
	{
		if (hasTeam(ply))
			ply.sendMessage("팀에 참가하기 전에 팀을 탈퇴해주세요.");
		else
		{
			ply.setMetadata("SimpleTeam_Team", new FixedMetadataValue(SimpleTeam.plugin, team.getId()));
			ply.sendMessage(TEAMS.getKoreanName(team) + "팀에 가입되었습니다.");
			broadcastJoinMessage(ply, team);
		}
		
	}
	
	public static void leaveTeam(Player ply)
	{
		if (hasTeam(ply))
		{
			broadcastLeaveMessage(ply, getTeam(ply));
			ply.removeMetadata("SimpleTeam_Team", SimpleTeam.plugin);
			ply.sendMessage("팀을 탈퇴했습니다.");
		}
		else
			ply.sendMessage("참가한 팀이 없습니다.");
	}
	
	public static void leaveTeamSilence(Player ply)
	{
		if (hasTeam(ply))
			ply.removeMetadata("SimpleTeam_Team", SimpleTeam.plugin);
	}
	
	public static void broadcastJoinMessage(Player joinedPlayer, TEAMS joinedTeam)
	{
		Player[] onlinePlayers = SimpleTeam.plugin.getServer().getOnlinePlayers();
		
		for (int i = 0; i < onlinePlayers.length; i++)
		{
			Player ply = onlinePlayers[i];
			if (hasTeam(ply) && getTeam(ply) == joinedTeam)
				ply.sendMessage(joinedPlayer.getName() + "님이 팀에 참가했습니다.");
		}
	}
	
	public static void broadcastLeaveMessage(Player leftPlayer, TEAMS leftTeam)
	{
		Player[] onlinePlayers = SimpleTeam.plugin.getServer().getOnlinePlayers();
		
		for (int i = 0; i < onlinePlayers.length; i++)
		{
			Player ply = onlinePlayers[i];
			if (isPlayerInTeam(ply, leftTeam))
				ply.sendMessage(leftPlayer.getName() + "님이 팀에서 나갔습니다.");
		}
	}
	
	public static void broadcastTeamMessage(Player sender, String msg)
	{
		//[팀이름]<닉네임>:<하는말>
		
		if (msg == null)
		{
			sender.sendMessage(CommandManager.getNoCommandMsg());
			return;
		} else if (!(hasTeam(sender))) {
			sender.sendMessage("팀이 없습니다.");
			return;
		}
		
		Player[] onlinePlayers = SimpleTeam.plugin.getServer().getOnlinePlayers();
		TEAMS team = getTeam(sender);
		
		for (int i = 0; i < onlinePlayers.length; i++)
		{
			Player ply = onlinePlayers[i];
			if (isPlayerInTeam(ply, team))
				ply.sendMessage("[" + TEAMS.getTeamColor(team) + TEAMS.getName(team) + ChatColor.WHITE + "] <" + sender.getName() + "> " + msg);
		}
	}
	
	public static void teleportTeam(Player sender, TEAMS team, Location pos)
	{
		Player[] onlinePlayers = SimpleTeam.plugin.getServer().getOnlinePlayers();
		boolean noOneMember = true;
		
		for (int i = 0; i < onlinePlayers.length; i++)
		{
			Player ply = onlinePlayers[i];
			if (isPlayerInTeam(ply, team))
			{
				noOneMember = false;
				if (pos == null)
					ply.teleport(sender);
				else
					ply.teleport(pos);
			}
		}
		
		if (noOneMember)
			sender.sendMessage("텔레포트는 정상적으로 시도했습니다만... TP 시킬 팀원이 없습니다. 아이코 아까브라!");
		else
			sender.sendMessage("정상적으로 해당 팀원들을 TP 했습니다.");
	}
	
	private static boolean isPlayerInTeam(Player ply, TEAMS team)
	{
		return hasTeam(ply) && ply.getMetadata("SimpleTeam_Team").get(0).asInt() == team.getId();
	}
	
	static boolean hasTeam(Player ply)
	{
		return ply.hasMetadata("SimpleTeam_Team");
	}
	
	static TEAMS getTeam(Player ply)
	{
		if (!hasTeam(ply)) return TEAMS.NONE;
		
		return TEAMS.getById(ply.getMetadata("SimpleTeam_Team").get(0).asInt());
	}
}
