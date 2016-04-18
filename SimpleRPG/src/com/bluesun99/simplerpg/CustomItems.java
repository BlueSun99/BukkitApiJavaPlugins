package com.bluesun99.simplerpg;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

class CustomItems {
	static class Reselector {
		static void giveReselector(Player ply)
		{
			ply.getInventory().addItem(getReselector());
		}
		
		static ItemStack getReselector()
		{
			String iname = SimpleRPGMain.plugin.getConfig().getString("Misc.Reselector.ItemName");
			ItemStack is = new ItemStack(Material.PAPER);
			ItemMeta im = is.getItemMeta();
			im.addEnchant(Enchantment.LURE, 65535, true);
			if (iname == null || iname == "")
				im.setDisplayName("Class Reselector");
			else
				im.setDisplayName(iname);
			java.util.List<String> ilore = new java.util.ArrayList<String>();
			ilore.add("사용 시 직업을 재 선택할 수 있습니다.");
			im.setLore(ilore);
			is.setItemMeta(im);
			return is;
		}
		
		static boolean isReselector(ItemStack is)
		{
			return is.getType() == Material.PAPER
					&& is.getItemMeta().hasEnchant(Enchantment.LURE)
					&& is.getItemMeta().getEnchantLevel(Enchantment.LURE) == 65535;
		}
		
		static boolean isReselecting(Player ply)
		{
			return ply.hasMetadata("SimpleRPG_Reselecting")
					&& ply.getMetadata("SimpleRPG_Reselecting").get(0).asBoolean();
		}
		
		static void setReselecting(Player ply, boolean reselecting)
		{
			if (reselecting)
				ply.setMetadata("SimpleRPG_Reselecting", new FixedMetadataValue(SimpleRPGMain.plugin, true));
			else
				if (isReselecting(ply)) ply.removeMetadata("SimpleRPG_Reselecting", SimpleRPGMain.plugin);
		}
	}
}
