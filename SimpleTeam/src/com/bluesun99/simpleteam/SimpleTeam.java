package com.bluesun99.simpleteam;

import org.bukkit.plugin.java.JavaPlugin;

public class SimpleTeam extends JavaPlugin {
	static java.util.logging.Logger logger;
	static org.bukkit.plugin.Plugin plugin;
	
	@Override
	public void onEnable()
	{
		SimpleTeam.plugin = this;
		
		this.getLogger().info("SimpleTeam 플러그인이 활성화 되었습니다.");
		
		SimpleTeam.logger = this.getLogger();
		
		this.getCommand("팀").setExecutor(new CommandManager());
		
		this.getServer().getPluginManager().registerEvents(new SimpleEventHandler(), this);
		
		this.getLogger().info("SimpleTeam 플러그인에서 커맨드와 이벤트를 등록했습니다.");
	}
	
	@Override
	public void onDisable()
	{
		this.getLogger().info("SimpleTeam 플러그인이 비-활성화 되었습니다.");
	}
}
