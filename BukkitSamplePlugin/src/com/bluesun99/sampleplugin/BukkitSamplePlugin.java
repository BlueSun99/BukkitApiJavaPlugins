package com.bluesun99.sampleplugin;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitSamplePlugin extends JavaPlugin {
	@Override
	public void onEnable()
	{
		File h2File = new File("lib", "h2-1.4.191.jar");
		if ((!h2File.exists()) && (!getConfig().getBoolean("mysql.enabled"))) {
			this.getLogger().severe("-------------------------------");
			this.getLogger().severe("H2 library missing!");
			this.getLogger().severe("");
			this.getLogger().severe("Please follow the instructions at my dev.bukkit project page");
			this.getLogger().severe("http://dev.bukkit.org/server-mods/xAuth/pages/required-dependencies/");
			this.getLogger().severe("-------------------------------");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}
}
