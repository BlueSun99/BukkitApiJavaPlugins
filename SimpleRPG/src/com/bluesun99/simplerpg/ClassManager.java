package com.bluesun99.simplerpg;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ClassManager {
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
		
		String getRealName() // It returns like VIKING or ELF.
		{
			return this.name();
		}
		
		String getKoreanName()
		{
			switch (this) {
			case VIKING:
				return "바이킹";
			case ELF:
				return "엘프";
			default:
				return "";
			}
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
		return ply.hasMetadata("SimpleRPG_Class") &&
				(ply.getMetadata("SimpleRPG_Class").get(0).asInt() == RPGClass.VIKING.getId()
				|| ply.getMetadata("SimpleRPG_Class").get(0).asInt() == RPGClass.ELF.getId());
	}
	
	static RPGClass getPlayerClass(Player ply)
	{
		if (hasPlayerClass(ply))
			return RPGClass.getById(ply.getMetadata("SimpleRPG_Class").get(0).asInt());
		
		return RPGClass.NONE;
	}
	
	static boolean setPlayerClass(Player ply, RPGClass rc)
	{
		if (ply.hasMetadata("SimpleRPG_Class"))
			ply.removeMetadata("SimpleRPG_Class", SimpleRPGMain.plugin);
		
		switch (rc) {
		case VIKING:
		case ELF:
			ply.setMetadata("SimpleRPG_Class", new FixedMetadataValue(SimpleRPGMain.plugin, rc.getId()));
			return true;
		default:
			return false;
		}
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
		
		ply.sendMessage(rc.getClassColor() + rc.getKoreanName() + " 직업군에 맞는 버프가 지급되었습니다.");
		
		return true;
	}

	static void stripEffects(Player ply)
	{
		ply.getActivePotionEffects().clear();
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
