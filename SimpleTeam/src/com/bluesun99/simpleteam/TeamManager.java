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
			case "����":
			case "����":
				return RED;
			case "�Ķ�":
			case "���":
				return BLUE;
			case "���":
			case "����":
				return YELLOW;
			case "�ʷ�":
			case "�׸�":
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
				return "����, ����";
			case BLUE:
				return "�Ķ�, ���";
			case YELLOW:
				return "���, ����";
			case GREEN:
				return "�ʷ�, �׸�";
			default:
				return "����";
			}
		}
	}
	
	public static void joinTeam(Player ply, TEAMS team)
	{
		if (hasTeam(ply))
			ply.sendMessage("���� �����ϱ� ���� ���� Ż�����ּ���.");
		else
		{
			ply.setMetadata("SimpleTeam_Team", new FixedMetadataValue(SimpleTeam.plugin, team.getId()));
			ply.sendMessage(TEAMS.getKoreanName(team) + "���� ���ԵǾ����ϴ�.");
			broadcastJoinMessage(ply, team);
		}
		
	}
	
	public static void leaveTeam(Player ply)
	{
		if (hasTeam(ply))
		{
			broadcastLeaveMessage(ply, getTeam(ply));
			ply.removeMetadata("SimpleTeam_Team", SimpleTeam.plugin);
			ply.sendMessage("���� Ż���߽��ϴ�.");
		}
		else
			ply.sendMessage("������ ���� �����ϴ�.");
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
				ply.sendMessage(joinedPlayer.getName() + "���� ���� �����߽��ϴ�.");
		}
	}
	
	public static void broadcastLeaveMessage(Player leftPlayer, TEAMS leftTeam)
	{
		Player[] onlinePlayers = SimpleTeam.plugin.getServer().getOnlinePlayers();
		
		for (int i = 0; i < onlinePlayers.length; i++)
		{
			Player ply = onlinePlayers[i];
			if (isPlayerInTeam(ply, leftTeam))
				ply.sendMessage(leftPlayer.getName() + "���� ������ �������ϴ�.");
		}
	}
	
	public static void broadcastTeamMessage(Player sender, String msg)
	{
		//[���̸�]<�г���>:<�ϴ¸�>
		
		if (msg == null)
		{
			sender.sendMessage(CommandManager.getNoCommandMsg());
			return;
		} else if (!(hasTeam(sender))) {
			sender.sendMessage("���� �����ϴ�.");
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
			sender.sendMessage("�ڷ���Ʈ�� ���������� �õ��߽��ϴٸ�... TP ��ų ������ �����ϴ�. ������ �Ʊ���!");
		else
			sender.sendMessage("���������� �ش� �������� TP �߽��ϴ�.");
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
