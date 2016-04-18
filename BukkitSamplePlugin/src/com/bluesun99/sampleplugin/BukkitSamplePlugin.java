package com.bluesun99.sampleplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitSamplePlugin extends JavaPlugin {
	@Override
	public void onEnable()
	{
		this.getLogger().info("Ayy, You Stupid!");
	}
}
