package com.bluesun99.simpleteam;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SimpleEventHandler implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		Player ply = e.getPlayer();
		TeamManager.leaveTeamSilence(ply);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		Player ply = e.getPlayer();
		TeamManager.leaveTeamSilence(ply);
	}
}
