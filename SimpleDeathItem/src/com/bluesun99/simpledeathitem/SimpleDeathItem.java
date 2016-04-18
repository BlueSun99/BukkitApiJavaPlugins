package com.bluesun99.simpledeathitem;

import org.bukkit.plugin.java.JavaPlugin;

public class SimpleDeathItem extends JavaPlugin {
	public static java.util.logging.Logger logger;
	
	@Override
	public void onEnable()
	{
		SimpleDeathItem.logger = this.getLogger();
		
		this.getLogger().info("SimpleDeathItem 플러그인이 활성화 되었습니다.");
		
		try {
			this.getServer().getPluginManager().registerEvents(new SimpleEventHandler(), this);
			this.getLogger().info("SimpleDeathItem 플러그인에서 이벤트를 성공적으로 등록했습니다.");
		} catch (Exception e) {
			this.getLogger().info("SimpleDeathItem 플러그인에서 이벤트를 등록하는데 실패했습니다.");
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onDisable()
	{
		this.getLogger().info("SimpleDeathItem 플러그인이 비-활성화 되었습니다.");
	}
}
