package com.bluesun99.sampleplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class SpigotSamplePlugin extends JavaPlugin {
	@Override
	public void onEnable()
	{
		this.getLogger().info("[SSP] SpigotSamplePlugin Enabled :D Have Fun!");
		this.getCommand("kit").setExecutor(new CommandKit());
		this.getLogger().info("[SSP] Registered Some Commands :D");
	}
	
	@Override
	public void onDisable()
	{
		this.getLogger().info("[SSP] SpigotSamplePlugin Disabled :(");
	}
}
