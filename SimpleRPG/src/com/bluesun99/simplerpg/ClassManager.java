package com.bluesun99.simplerpg;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ClassManager {
	private static Connection conn = SimpleRPGMain.dbm.getConnection();
	
	public enum RPGClass
	{
		NONE(0),
		VIKING(1),
		ELF(2);
		
		private final int id;
		
		RPGClass(int id)
		{
			this.id = id;
		}
		
		int getId()
		{
			return this.id;
		}
		
		static RPGClass getById(int id)
		{
			return RPGClass.values()[id];
		}
		
		String getName() // It returns like Viking or Elf.
		{
			String s = this.name().toLowerCase();
			return s.substring(0, 1).toUpperCase() + s.substring(1);
		}
		
		String getDeclaredName() // It returns like VIKING or ELF.
		{
			return this.name();
		}
		
		String getFriendlyName()
		{
			return SimpleRPGMain.lm.getString("srt_class_" + this.name().toLowerCase());
		}
		
		String getFriendlyNameWithColor()
		{
			return getClassColor() + SimpleRPGMain.lm.getString("srt_class_" + this.name().toLowerCase()) + ChatColor.WHITE;
		}
		
		ChatColor getClassColor()
		{
			switch (this) {
			case VIKING:
				return ChatColor.GOLD;
			case ELF:
				return ChatColor.AQUA;
			default:
				return ChatColor.WHITE;
			}
		}
	}
	
	static boolean hasPlayerClass(Player ply)
	{
		try {
			Statement st = conn.createStatement();
			st.executeUpdate("USE SimpleRPG;");
			
			ResultSet rs = st.executeQuery("SELECT * FROM playerclass;");
			
			while (rs.next())
			{
				if (rs.getString("UUID").equals(ply.getUniqueId().toString()))
					return true;
			}
			
			st.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		/*return ply.hasMetadata("SimpleRPG_Class") &&
				(ply.getMetadata("SimpleRPG_Class").get(0).asInt() == RPGClass.VIKING.getId()
				|| ply.getMetadata("SimpleRPG_Class").get(0).asInt() == RPGClass.ELF.getId());*/
	}
	
	static RPGClass getPlayerClass(Player ply)
	{
		if (!hasPlayerClass(ply))
			return RPGClass.NONE;
		
		try {
			Statement st = conn.createStatement();
			st.executeUpdate("USE SimpleRPG;");
			
			ResultSet rs = st.executeQuery("SELECT * FROM playerclass;");
			
			while (rs.next())
				if (rs.getString("UUID").equals(ply.getUniqueId().toString()))
					return RPGClass.getById(rs.getInt("class"));
			
			st.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*if (hasPlayerClass(ply))
			return RPGClass.getById(ply.getMetadata("SimpleRPG_Class").get(0).asInt());*/
		
		return RPGClass.NONE;
	}
	
	static boolean setPlayerClass(Player ply, RPGClass rc)
	{
		try {
			Statement st = conn.createStatement();
			st.executeUpdate("USE SimpleRPG;");

			try {
				st.executeUpdate("DELETE FROM playerclass WHERE UUID='" + ply.getUniqueId().toString() + "';");
			} catch (Exception e) {}
			
			if (rc == RPGClass.NONE)
				return true;
			
			st.executeUpdate("INSERT INTO playerclass values('" + ply.getUniqueId().toString() + "'," + String.valueOf(rc.getId()) + ");");
			
			st.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
		/*if (ply.hasMetadata("SimpleRPG_Class"))
			ply.removeMetadata("SimpleRPG_Class", SimpleRPGMain.plugin);
		
		switch (rc) {
		case VIKING:
		case ELF:
			ply.setMetadata("SimpleRPG_Class", new FixedMetadataValue(SimpleRPGMain.plugin, rc.getId()));
			return true;
		default:
			return false;
		}*/
	}

	static void teleportToClassSpawn(Player ply)
	{
		if (getPlayerClass(ply) == RPGClass.NONE)
			return;
		
		ply.teleport(ClassManager.getSpawnLocation(ply));
	}

	static boolean setEffectToPlayer(Player ply)
	{
		if (!hasPlayerClass(ply))
			return false;
		
		RPGClass rc = getPlayerClass(ply);
		
		java.util.List<String> buffs = SimpleRPGMain.plugin.getConfig().getStringList("Class." + rc.getName() + ".Effect");
		for (String s : buffs)
		{
			String[] buff = s.split(" ");
			ply.addPotionEffect(new PotionEffect(PotionEffectType.getByName(buff[0]), Integer.MAX_VALUE, Integer.parseInt(buff[1])));
		}
		
		ply.sendMessage(SimpleRPGMain.lm.format("srt_got_buff", rc.getFriendlyNameWithColor()));
		
		return true;
	}

	static void stripEffects(Player ply)
	{
		for (PotionEffect pe : ply.getActivePotionEffects())
			if (pe.getDuration() >= 2115947647)
				ply.removePotionEffect(pe.getType());
	}

	public static Location getSpawnLocation(Player ply)
	{
		String configName = "Class." + getPlayerClass(ply).getName() + ".Spawn.";
		
		return new Location(ply.getWorld(),
				SimpleRPGMain.plugin.getConfig().getDouble(configName + "X"),
				SimpleRPGMain.plugin.getConfig().getDouble(configName + "Y"),
				SimpleRPGMain.plugin.getConfig().getDouble(configName + "Z"));
	}
}
