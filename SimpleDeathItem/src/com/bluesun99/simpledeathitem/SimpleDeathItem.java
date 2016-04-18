package com.bluesun99.simpledeathitem;

import org.bukkit.plugin.java.JavaPlugin;

public class SimpleDeathItem extends JavaPlugin {
	public static java.util.logging.Logger logger;
	
	@Override
	public void onEnable()
	{
		SimpleDeathItem.logger = this.getLogger();
		
		this.getLogger().info("SimpleDeathItem �÷������� Ȱ��ȭ �Ǿ����ϴ�.");
		
		try {
			this.getServer().getPluginManager().registerEvents(new SimpleEventHandler(), this);
			this.getLogger().info("SimpleDeathItem �÷����ο��� �̺�Ʈ�� ���������� ����߽��ϴ�.");
		} catch (Exception e) {
			this.getLogger().info("SimpleDeathItem �÷����ο��� �̺�Ʈ�� ����ϴµ� �����߽��ϴ�.");
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onDisable()
	{
		this.getLogger().info("SimpleDeathItem �÷������� ��-Ȱ��ȭ �Ǿ����ϴ�.");
	}
}
