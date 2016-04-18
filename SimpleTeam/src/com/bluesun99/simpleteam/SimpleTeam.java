package com.bluesun99.simpleteam;

import org.bukkit.plugin.java.JavaPlugin;

public class SimpleTeam extends JavaPlugin {
	static java.util.logging.Logger logger;
	static org.bukkit.plugin.Plugin plugin;
	
	@Override
	public void onEnable()
	{
		SimpleTeam.plugin = this;
		
		this.getLogger().info("SimpleTeam �÷������� Ȱ��ȭ �Ǿ����ϴ�.");
		
		SimpleTeam.logger = this.getLogger();
		
		this.getCommand("��").setExecutor(new CommandManager());
		
		this.getServer().getPluginManager().registerEvents(new SimpleEventHandler(), this);
		
		this.getLogger().info("SimpleTeam �÷����ο��� Ŀ�ǵ�� �̺�Ʈ�� ����߽��ϴ�.");
	}
	
	@Override
	public void onDisable()
	{
		this.getLogger().info("SimpleTeam �÷������� ��-Ȱ��ȭ �Ǿ����ϴ�.");
	}
}
